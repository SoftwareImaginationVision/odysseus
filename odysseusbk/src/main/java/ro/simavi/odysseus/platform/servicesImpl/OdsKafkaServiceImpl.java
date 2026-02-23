package ro.simavi.odysseus.platform.servicesImpl;

import org.apache.kafka.clients.admin.*;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.errors.WakeupException;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;
import ro.simavi.odysseus.platform.entities.KafkaMessages;
import ro.simavi.odysseus.platform.entities.KafkaTopic;
import ro.simavi.odysseus.platform.services.OdsKafkaService;

import javax.annotation.PreDestroy;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

@Service
public class OdsKafkaServiceImpl implements OdsKafkaService {
    private static final Logger log = LoggerFactory.getLogger(OdsKafkaServiceImpl.class);

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final AdminClient adminClient;
    private final Map<String, Sinks.Many<String>> topicSinks = new ConcurrentHashMap<>();
    private final Map<String, KafkaConsumer<String, String>> topicConsumers = new ConcurrentHashMap<>();
    private final ExecutorService executorService;

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    public OdsKafkaServiceImpl(KafkaTemplate<String, String> kafkaTemplate, AdminClient adminClient) {
        this.kafkaTemplate = kafkaTemplate;
        this.adminClient = adminClient;
        this.executorService = Executors.newCachedThreadPool();
    }

    @PreDestroy
    public void cleanup() {
        topicConsumers.values().forEach(KafkaConsumer::wakeup);
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(5000, TimeUnit.MILLISECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
        }
    }

    @Override
    public void sendMessage(String topic, String message) {
        try {
            kafkaTemplate.send(topic, message).get();
        } catch (Exception e) {
            log.error("Failed to send message to Kafka", e);
            throw new RuntimeException("Failed to send message to Kafka", e);
        }
    }

    @Override
    public Flux<String> getMessages(String topic) {
        log.info("Subscribing to topic: {}", topic);
        Sinks.Many<String> sink = topicSinks.computeIfAbsent(topic,
                k -> Sinks.many().multicast().onBackpressureBuffer());

        if (!topicConsumers.containsKey(topic)) {
            KafkaConsumer<String, String> consumer = createConsumer(topic);
            topicConsumers.put(topic, consumer);
            executorService.submit(() -> pollMessages(topic, consumer, sink));
        }

        return sink.asFlux()
                .doOnSubscribe(s -> log.info("New subscription to topic: {}", topic))
                .doOnNext(message -> log.info("Emitting message from topic {}: {}", topic, message))
                .doFinally(signalType -> {
                    log.info("Subscription to topic {} ended with signal: {}", topic, signalType);
                    unsubscribe(topic);
                });
    }
    @Override
    public void cleanTopic(String topic) {
        try {
            TopicDescription topicDescription = adminClient.describeTopics(Collections.singletonList(topic)).all().get().get(topic);
            Map<TopicPartition, RecordsToDelete> partitionToDeleteMap = new HashMap<>();
            topicDescription.partitions().forEach(partitionInfo -> {
                TopicPartition partition = new TopicPartition(topic, partitionInfo.partition());
                RecordsToDelete recordsToDelete = RecordsToDelete.beforeOffset(Long.MAX_VALUE);
                partitionToDeleteMap.put(partition, recordsToDelete);
            });

            DeleteRecordsResult deleteRecordsResult = adminClient.deleteRecords(partitionToDeleteMap);
            deleteRecordsResult.all().get();
            log.info("Cleaned all messages from topic: {}", topic);
        } catch (ExecutionException e) {
            if (e.getCause() instanceof OffsetOutOfRangeException) {
                log.warn("Topic {} is already empty or offsets have expired", topic);
            } else {
                log.error("Failed to clean messages from topic: {}", topic, e);
                throw new RuntimeException("Failed to clean messages from topic: " + topic, e);
            }
        } catch (InterruptedException e) {
            log.error("Failed to clean messages from topic: {}", topic, e);
            throw new RuntimeException("Failed to clean messages from topic: " + topic, e);
        }
    }

    @Override
    public List<KafkaMessages> getExistingMessages(String topic) {
        log.info("Retrieving existing messages from topic: {}", topic);
        List<KafkaMessages> messages = new ArrayList<>();
        KafkaConsumer<String, String> consumer = null;
        try {
            consumer = createConsumer(topic);
            consumer.subscribe(Collections.singletonList(topic));

            ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(5000));
            for (ConsumerRecord<String, String> record : records) {
                KafkaMessages kafkaMessage = new KafkaMessages();
                kafkaMessage.setMessage(record.value());
                messages.add(kafkaMessage);
                log.info("Retrieved message - Topic: {}, Partition: {}, Offset: {}, Key: {}, Value: {}",
                        record.topic(), record.partition(), record.offset(), record.key(), record.value());
            }
        } catch (Exception e) {
            log.error("Error retrieving messages from topic {}", topic, e);
        } finally {
            if (consumer != null) {
                consumer.close();
            }
        }

        log.info("Retrieved {} messages from topic {}", messages.size(), topic);
        return messages;
    }

    @Override
    public KafkaConsumer<String, String> createConsumer(String topic) {
        Properties props = new Properties();
        props.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.setProperty(ConsumerConfig.GROUP_ID_CONFIG, topic + "-group");
        props.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.setProperty(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
        props.setProperty(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, "100");

        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
        consumer.subscribe(Collections.singletonList(topic));
        return consumer;
    }

    private void pollMessages(String topic, KafkaConsumer<String, String> consumer, Sinks.Many<String> sink) {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
                for (ConsumerRecord<String, String> record : records) {
                    log.info("Received message - Topic: {}, Partition: {}, Offset: {}, Key: {}, Value: {}",
                            record.topic(), record.partition(), record.offset(), record.key(), record.value());
                    sink.tryEmitNext(record.value());
                }
                consumer.commitAsync();
            }
        } catch (WakeupException e) {
            log.info("Received shutdown signal for topic: {}", topic);
        } catch (Exception e) {
            log.error("Error while polling messages for topic: {}", topic, e);
        } finally {
            try {
                consumer.close();
            } catch (Exception e) {
                log.error("Error closing consumer for topic: {}", topic, e);
            }
            log.info("Consumer for topic {} has now gracefully closed.", topic);
        }
    }

    public void unsubscribe(String topic) {
        KafkaConsumer<String, String> consumer = topicConsumers.remove(topic);
        if (consumer != null) {
            consumer.wakeup();
        }
        Sinks.Many<String> sink = topicSinks.remove(topic);
        if (sink != null) {
            sink.tryEmitComplete();
        }
        log.info("Unsubscribed from topic: {}", topic);
    }

    @Override
    public Set<KafkaTopic> listTopics() {
        try {
            Set<String> topicNames = adminClient.listTopics(new ListTopicsOptions().timeoutMs(10000)).names().get();
            log.info("Retrieved {} topics from Kafka", topicNames.size());
            return topicNames.stream()
                    .map(name -> {
                        KafkaTopic kafkaTopic = new KafkaTopic();
                        kafkaTopic.setName(name);
                        return kafkaTopic;
                    })
                    .collect(Collectors.toSet());
        } catch (InterruptedException | ExecutionException e) {
            log.error("Failed to list Kafka topics", e);
            throw new RuntimeException("Failed to list Kafka topics", e);
        }
    }
}
