package ro.simavi.odysseus.platform.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.gson.JsonObject;
import io.swagger.v3.oas.annotations.Operation;
import lombok.Getter;
import lombok.Setter;
import org.primefaces.PrimeFaces;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.multipart.MultipartFile;
import ro.simavi.odysseus.platform.entities.KafkaMessages;
import ro.simavi.odysseus.platform.entities.KafkaTopic;
import ro.simavi.odysseus.platform.entities.OdsDevice;
import ro.simavi.odysseus.platform.entities.OdsDocument;
import ro.simavi.odysseus.platform.kafka.Greeting;
import ro.simavi.odysseus.platform.kafka.KafkaProducer;
import ro.simavi.odysseus.platform.repositories.OdsTravelerRepository;
import ro.simavi.odysseus.platform.services.OdsAuditLogService;
import ro.simavi.odysseus.platform.services.OdsDocumentService;
import ro.simavi.odysseus.platform.services.OdsKafkaService;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutionException;

@Component
@RestController
@Setter
@Getter
@Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class KafkaController {
    @Autowired
    private KafkaProducer kafkaProducer;

    @Autowired
    private OdsDocumentService odsDocumentService;

    @Autowired
    private OdsTravelerRepository odsTravelerRepository;

    @Autowired
    private WorkflowInstanceController workflowInstanceController;

    @Autowired
    private OdsKafkaService odsKafkaService;

    @Autowired
    private OdsAuditLogService odsAuditLogService;

    private static final Logger log = LoggerFactory.getLogger(KafkaController.class);

    private Set<KafkaTopic> kafkaTopicsSet;

    private KafkaTopic topicSelected;

    private KafkaMessages messageForTopic;

    private List<KafkaMessages> kafkaMessagesList;

    @PostConstruct
    private void postConstruct(){
        kafkaTopicsSet = odsKafkaService.listTopics();
        messageForTopic = new KafkaMessages();
        kafkaMessagesList = new ArrayList<>();
    }
    @Operation(summary = "Send a message to Kafka")
    @PostMapping("/testKafkaMessage")
    public void testProducerKafka(@RequestParam("payload") String payload, @RequestParam("topic") String topic){
        kafkaProducer.send(topic, payload);
    }
    @Operation(summary = "Send a greeting to Kafka (see if it ius working)")
    @PostMapping("/testKafkaGreeting")
    public void testProducerKafkaGreeting(@RequestBody Greeting payload, @RequestParam("topic") String topic){
        kafkaProducer.sendGreeting(topic, payload);
    }
    @Operation(summary = "Send a message to Kafka to check if a device is available (like a camera, or stand)")
    @PostMapping("/testKafkaDevice")
    public void testProducerKafkaDevice(@RequestBody OdsDevice payload, @RequestParam("topic") String topic){
        kafkaProducer.sendDevice(topic, payload);
    }
    @Operation(summary = "Send a message to Kafka trequest VDP verification")
    @PostMapping("/kafkaVerifyVDP")
    public void producerKafkaDocument(@RequestParam("document") String document, @RequestParam("document_image") MultipartFile documentImage, @Param("tuuid") String tuuid, @RequestParam("topic") String topic) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        OdsDocument odsDocument = objectMapper.readValue(document, OdsDocument.class);
        odsDocument.setPicture(documentImage.getBytes());
        if(Objects.nonNull(tuuid) && !tuuid.isEmpty())
            odsDocument.setTravelerId(odsTravelerRepository.findFirstByTuuid(tuuid).getId());
        odsAuditLogService.startDocProcess(this.getClass().getName(), "kafkaVerifyVDP", odsDocument.getDocNumber(), odsDocument.getDocumentType(), tuuid);
        kafkaProducer.sendOdsDocument(topic,odsDocument);
        odsAuditLogService.endDocProcess(this.getClass().getName(), "kafkaVerifyVDP", odsDocument.getDocNumber(), "kafka sent", odsDocument.getDocumentType(), tuuid);
    }
    @Operation(summary = "Send a message to Kafka to launch the Risk Assesmenty on a document")
    @PostMapping("/kafkaDocRiskCheckRequest")
    public ResponseEntity<String> kafkaDocRiskCheckRequest(@RequestParam("document") String document, @RequestParam("document_image") MultipartFile documentImage, @Param("tuuid") String tuuid, @RequestParam("topic") String topic) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        OdsDocument odsDocument = objectMapper.readValue(document, OdsDocument.class);
        odsDocument.setPicture(documentImage.getBytes());
        if(Objects.nonNull(tuuid) && !tuuid.isEmpty())
            odsDocument.setTravelerId(odsTravelerRepository.findFirstByTuuid(tuuid).getId());
        odsAuditLogService.startDocProcess(this.getClass().getName(), "kafkaDocRiskCheckRequest", odsDocument.getDocNumber(), odsDocument.getDocumentType(), tuuid);
        kafkaProducer.sendOdsDocument(topic,odsDocument);
        odsAuditLogService.endDocProcess(this.getClass().getName(), "kafkaDocRiskCheckRequest", odsDocument.getDocNumber(),"kafka sent", odsDocument.getDocumentType(), tuuid);
        String result = "OK";
        return ResponseEntity.ok(result);
    }
    @Operation(summary = "Send a message to Kafka to launch the get the Risk Assesmenty result on a document")
    @PostMapping("/kafkaIdentityRiskCheckRequest")
    public ResponseEntity<String> kafkaIdentityRiskCheckRequest(@RequestParam("document") String document, @RequestParam("document_image") MultipartFile documentImage, @Param("tuuid") String tuuid, @RequestParam("topic") String topic) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        OdsDocument odsDocument = objectMapper.readValue(document, OdsDocument.class);
        odsDocument.setPicture(documentImage.getBytes());
        if(Objects.nonNull(tuuid) && !tuuid.isEmpty())
            odsDocument.setTravelerId(odsTravelerRepository.findFirstByTuuid(tuuid).getId());
        odsAuditLogService.startDocProcess(this.getClass().getName(), "kafkaIdentityRiskCheckRequest", odsDocument.getDocNumber(), odsDocument.getDocumentType(), tuuid);
        kafkaProducer.sendOdsDocument(topic,odsDocument);
        odsAuditLogService.endDocProcess(this.getClass().getName(), "kafkaIdentityRiskCheckRequest", odsDocument.getDocNumber(),"kafka sent", odsDocument.getDocumentType(), tuuid);
        String result = "OK";
        return ResponseEntity.ok(result);
    }
    @Operation(summary = "Send a message to Kafka to launch the Scan of a car")
    @PostMapping("/kafkaScanRiskCheckRequest")
    public ResponseEntity<String> kafkaScanRiskCheckRequest(@RequestParam("document") String document, @RequestParam("document_image") MultipartFile documentImage, @Param("tuuid") String tuuid, @RequestParam("topic") String topic) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        OdsDocument odsDocument = objectMapper.readValue(document, OdsDocument.class);
        odsDocument.setPicture(documentImage.getBytes());
        if(Objects.nonNull(tuuid) && !tuuid.isEmpty())
            odsDocument.setTravelerId(odsTravelerRepository.findFirstByTuuid(tuuid).getId());
        odsAuditLogService.startDocProcess(this.getClass().getName(), "kafkaScanRiskCheckRequest", odsDocument.getDocNumber(), odsDocument.getDocumentType(), tuuid);
        kafkaProducer.sendOdsDocument(topic,odsDocument);
        odsAuditLogService.endDocProcess(this.getClass().getName(), "kafkaScanRiskCheckRequest", odsDocument.getDocNumber(),"kafka sent", odsDocument.getDocumentType(), tuuid);
        String result = "OK";
        return ResponseEntity.ok(result);
    }
    @Operation(summary = "Send a message to Kafka to launch the Risk Assesment on a document in train")
    @PostMapping("/kafkaVerifyVDPTrain")
    public ResponseEntity<String> producerKafkaDocumentTrain(@RequestParam("document") String document, @RequestParam("document_image") MultipartFile documentImage, @Param("tuuid") String tuuid, @RequestParam("topic") String topic) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        OdsDocument odsDocument = objectMapper.readValue(document, OdsDocument.class);
        odsDocument.setPicture(documentImage.getBytes());
        if(Objects.nonNull(tuuid) && !tuuid.isEmpty())
            odsDocument.setTravelerId(odsTravelerRepository.findFirstByTuuid(tuuid).getId());
        odsAuditLogService.startDocProcess(this.getClass().getName(), "kafkaVerifyVDPTrain", odsDocument.getDocNumber(), odsDocument.getDocumentType(), tuuid);
        String result = odsDocumentService.verifyDocumentForTrain(odsDocument);

        if(Objects.nonNull(result)){
            if(result.equalsIgnoreCase("Successfully")){
                workflowInstanceController.createWorkflowInstance("verifyVDP", null);
                result = "You successfully verified your document!";
            } else if (result.equalsIgnoreCase("Error")){
                result = "You couldn`t verified your document!";
            } else if (result.equalsIgnoreCase("Risk")) {
                odsDocument.setHasRisk("true");
                result = "Wait for a officer to verify your document!";
            }
        }
        kafkaProducer.sendOdsDocument(topic,odsDocument);
        odsAuditLogService.endDocProcess(this.getClass().getName(), "kafkaVerifyVDPTrain", odsDocument.getDocNumber(),"kafka sent", odsDocument.getDocumentType(), tuuid);
        return ResponseEntity.ok(result);
    }


    public void listTopics(){
        kafkaTopicsSet = odsKafkaService.listTopics();
        PrimeFaces.current().ajax().update("kafkaTopicsForm");
    }

    public void sendMessage() {
        try {
            odsKafkaService.sendMessage(topicSelected.getName(), messageForTopic.getMessage());
            messageForTopic = null;
            //succes messages

        } catch (Exception e) {
            // error messages
        }
    }

    public void getMessages() {
        kafkaMessagesList.clear();
        kafkaMessagesList = odsKafkaService.getExistingMessages(topicSelected.getName());
        log.info("Retrieved {} messages from topic {}", kafkaMessagesList.size(), topicSelected.getName());
    }

    public void uploadFile(MultipartFile file, String message, String topic) {
        try {
            // Parse the message and topic from JSON
            odsAuditLogService.auditAction(this.getClass().getName(), "START Upoload file", file.getOriginalFilename(), topic);
            ObjectMapper objectMapper = new ObjectMapper();
            String parsedMessage = objectMapper.readValue(message, String.class);
            String parsedTopic = objectMapper.readValue(topic, String.class);

            // Read the contents of the file
            byte[] fileContent = file.getBytes();
            JsonObject fileMessage = new JsonObject();
            fileMessage.addProperty("fileName", file.getOriginalFilename());
            fileMessage.addProperty("fileType", file.getContentType());
            fileMessage.addProperty("fileContent", Base64.getEncoder().encodeToString(fileContent));
            // Send the entire file as a single message to Kafka
            try {
                odsKafkaService.sendMessage(parsedTopic, new String(fileMessage.toString()));
            } catch (Exception e) {
                log.error("Failed to send file to Kafka topic '{}': {}", parsedTopic, e.getMessage());
            }

            // Send the separate message to Kafka
            try {
                odsKafkaService.sendMessage(parsedTopic, parsedMessage);
            } catch (Exception e) {
                log.error("Failed to send message to Kafka topic '{}': {}", parsedTopic, e.getMessage());
            }

            // success messages
        } catch (IOException e) {
            log.error("Failed to upload file: {}", e.getMessage());
            // error messages
        }
        odsAuditLogService.auditAction(this.getClass().getName(), "END Upoload file", file.getOriginalFilename(), topic);
    }
    public void cleanTopic(String topic) {
        try {
            odsKafkaService.cleanTopic(topic);
            // succes message
        } catch (Exception e) {
            // error message
        }
    }


}
