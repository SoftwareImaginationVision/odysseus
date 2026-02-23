package ro.simavi.odysseus.platform.services;

import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import ro.simavi.odysseus.platform.entities.KafkaMessages;
import ro.simavi.odysseus.platform.entities.KafkaTopic;

import java.util.List;
import java.util.Set;

@Service
public interface OdsKafkaService {
    void sendMessage(String topic, String message);

    Flux<String> getMessages(String topic);

    void cleanTopic(String topic);

    List<KafkaMessages> getExistingMessages(String topic);

    KafkaConsumer<String, String> createConsumer(String topic);

    Set<KafkaTopic> listTopics();
}
