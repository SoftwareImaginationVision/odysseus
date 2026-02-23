package ro.simavi.odysseus.platform.kafka;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.kie.api.runtime.KieSession;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;


import ro.simavi.odysseus.platform.entities.OdsDevice;
import ro.simavi.odysseus.platform.entities.OdsDocument;
import ro.simavi.odysseus.platform.services.OdsDocumentService;

import javax.annotation.PostConstruct;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.logging.Logger;


@Component
public class KafkaConsumer {

    private static final Logger LOGGER = Logger.getLogger(KafkaConsumer.class.getName());

    private CountDownLatch latch = new CountDownLatch(1);

    private String payload;

    @Autowired
    private OdsDocumentService odsDocumentService;



    @Autowired
    private KieSession session;


//    @Value(value = "${audit.kafka.consumer}")
//    private boolean auditKafkaConsumer;
//
//    @Value(value = "${ic00.topic.name}")
//    private String topic00;


    @PostConstruct
    public void init(){

    }

    @KafkaListener(topics = {
            "${test.topic}",
            "${test.topic2}"
    }, groupId = "${default.groupid}")
    public void receive(ConsumerRecord<?, ?> consumerRecord) throws JsonProcessingException {
        LOGGER.info("received payload=" + consumerRecord.toString());

        payload = consumerRecord.value().toString();
        String topic =consumerRecord.topic();

        if(payload.equals("Ionut")){
            Greeting greeting = new Greeting("Hello, ", "Ionut");
            session.insert(greeting);
            session.fireAllRules();
        }
        // Initialize ObjectMapper
        ObjectMapper objectMapper = new ObjectMapper();

        // Convert JSON string to entity object
        OdsDevice odsDevice = objectMapper.readValue(payload, OdsDevice.class);
        session.insert(odsDevice);
        session.fireAllRules();

        latch.countDown();
    }

    @KafkaListener(topics = {
            "${test.topic1}"
    }, groupId = "${default.groupid}")
    public void receiveDocument(ConsumerRecord<?, ?> consumerRecord) throws JsonProcessingException {
        LOGGER.info("received payload=" + consumerRecord.toString());

        payload = consumerRecord.value().toString();
        String topic =consumerRecord.topic();

        // Initialize ObjectMapper
        ObjectMapper objectMapper = new ObjectMapper();

        // Convert JSON string to entity object
        OdsDocument odsDocument = objectMapper.readValue(payload, OdsDocument.class);
        odsDocumentService.saveOrEditOdsDocument(odsDocument);

        latch.countDown();
    }

    @KafkaListener(topics = {
            "${test.topic3}"
    }, groupId = "${default.groupid}")
    public void receiveDocumentForTrain(ConsumerRecord<?, ?> consumerRecord) throws JsonProcessingException {
        LOGGER.info("received payload=" + consumerRecord.toString());

        payload = consumerRecord.value().toString();
        String topic =consumerRecord.topic();

        // Initialize ObjectMapper
        ObjectMapper objectMapper = new ObjectMapper();

        // Convert JSON string to entity object
        OdsDocument odsDocument = objectMapper.readValue(payload, OdsDocument.class);
        OdsDocument odsDocument1 = odsDocumentService.saveOrEditOdsDocument(odsDocument);

        latch.countDown();
    }

    public CountDownLatch getLatch() {
        return latch;
    }

    public void resetLatch() {
        latch = new CountDownLatch(1);
    }

    public String getPayload() {
        return payload;
    }
}
