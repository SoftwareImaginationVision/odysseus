package ro.simavi.odysseus.platform.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import ro.simavi.odysseus.platform.entities.OdsDevice;
import ro.simavi.odysseus.platform.entities.OdsDocument;


import java.util.logging.Logger;

@Component
public class KafkaProducer {

    private static final Logger LOGGER = Logger.getLogger(KafkaProducer.class.getName());

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private KafkaTemplate<String, Greeting> greetingKafkaTemplate;

    @Autowired
    private KafkaTemplate<String, OdsDevice> deviceKafkaTemplate;

    @Autowired
    private  KafkaTemplate<String, OdsDocument> odsDocumentKafkaTemplate;


    public void send(String topic, String payload) {
        LOGGER.info("sending payload='{}' to topic="+ payload+ " to: " + topic);
        kafkaTemplate.send(topic, payload);
    }

    public void sendGreeting(String topic, Greeting payload){
        LOGGER.info("sending payload='{}' to topic="+ payload+ " to: " + topic);
        greetingKafkaTemplate.send(topic, payload);
    }

    public void sendDevice(String topic, OdsDevice payload){
        LOGGER.info("sending payload='{}' to topic="+ payload+ " to: " + topic);
        deviceKafkaTemplate.send(topic, payload);
    }

    public void sendOdsDocument(String topic, OdsDocument payload){
        LOGGER.info("sending payload='{}' to topic="+ payload+ " to: " + topic);
        odsDocumentKafkaTemplate.send(topic, payload);
    }

}