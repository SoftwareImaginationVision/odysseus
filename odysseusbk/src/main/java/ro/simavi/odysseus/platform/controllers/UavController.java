package ro.simavi.odysseus.platform.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.Getter;
import lombok.Setter;
import org.bouncycastle.util.encoders.Hex;
import org.primefaces.PrimeFaces;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.WebApplicationContext;
import ro.simavi.odysseus.platform.dto.BackgroundChecksDTO;
import ro.simavi.odysseus.platform.dto.BehaviourBiometricsDTO;
import ro.simavi.odysseus.platform.dto.PassengersDTO;
import ro.simavi.odysseus.platform.entities.*;
import ro.simavi.odysseus.platform.entities.OdsCarAlert;
import ro.simavi.odysseus.platform.repositories.*;
import ro.simavi.odysseus.platform.util.ImageConversionJp2ToJpeg;

import javax.annotation.PostConstruct;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;


@Setter
@Getter
@Component
@Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)
@RestController

public class UavController {
    @Autowired
    OdsTravelerRepository odsTravelerRepository;

    @Autowired
    private OdsTravelRepository odsTravelRepository;

    @Autowired
    private OdsTransactionsRepository odsTransactionsRepository;

    @Autowired
    private OdsCarTransactionsRepository odsCarTransactionsRepository;

    @Autowired
    private OdsDocumentValidationRepository odsDocumentValidationRepository;

    @Autowired
    private OdsLicensePlateValidationRepository odsLicensePlateValidationRepository;

    @Autowired
    private OdsTravellerAlertRepository odsTravellerAlertRepository;

    @Autowired
    private OdsCarAlertRepository odsCarAlertRepository;


    private List<CarTransaction> carTransactionList;
    private List<Transaction> transactionList;
    private List<OdsTraveler> travelerList = new ArrayList<>();

    private OdsTravel selectedTravel;
    private OdsLicensePlateValidation selectedCar;

    private OdsTraveler selectedTraveler;
    private OdsTraveler selectedPassenger;

    private ImageConversionJp2ToJpeg imageConversionJp2ToJpeg;

    private byte[] travelerImageBytes;

    private List<OdsTravellerAlert> alerts;
    private int alertCount;

    private List<OdsCarAlert> alertsCar;
    private int alertCountCars;
    @PostConstruct
    public void init() {
        this.carTransactionList = odsCarTransactionsRepository.findAll();
        this.transactionList = odsTransactionsRepository.findAll();
        imageConversionJp2ToJpeg = new ImageConversionJp2ToJpeg();
        alerts = odsTravellerAlertRepository.findAll();
        alertCount = alerts.size();

        alertsCar = odsCarAlertRepository.findAll();
        alertCountCars = alertsCar.size();
    }


    @Operation(summary = "Please send the license plate recognition along with the corresponding file from the XAI module. Create car transaction")
    @PostMapping(value = "/api/uav/lpr")
    public String licensePlateRecognition(@RequestBody LicensePlateDTO licensePlate){
        OdsLicensePlateValidation odsLicensePlateValidation = new OdsLicensePlateValidation();

        if(Objects.nonNull(licensePlate.getLicensePlate())){
            CarTransaction carTransaction = new CarTransaction();

            carTransaction.setLicensePlate(licensePlate.getLicensePlate());
            carTransaction.setLicensePlateConfidence(String.valueOf(licensePlate.getLicensePlateConfidence()));
            carTransaction.setTransactionUuid(UUID.randomUUID().toString());
            carTransaction.setTransactionStatus("ONGOING");

            odsCarTransactionsRepository.save(carTransaction);

            odsLicensePlateValidation.setCarColor(licensePlate.getCarColor());
            odsLicensePlateValidation.setColorConfidence(String.valueOf(licensePlate.getColorConfidence()));
            odsLicensePlateValidation.setColorReasoning(licensePlate.getColorReasoning());
            odsLicensePlateValidation.setCarType(licensePlate.getCarType());
            odsLicensePlateValidation.setTypeConfidence(String.valueOf(licensePlate.getTypeConfidence()));
            odsLicensePlateValidation.setTypeReasoning(licensePlate.getTypeReasoning());
            odsLicensePlateValidation.setLicensePlate(licensePlate.getLicensePlate());
            odsLicensePlateValidation.setLicensePlateConfidence(String.valueOf(licensePlate.getLicensePlateConfidence()));
            odsLicensePlateValidation.setLicensePlateImage(licensePlate.getLicensePlateImage());
            odsLicensePlateValidation.setXaiLicensePlateImage(licensePlate.getXaiLicensePlateImage());
            odsLicensePlateValidation.setPeopleCounting(String.valueOf(licensePlate.getPeopleCounting()));

            if(Objects.nonNull(licensePlate.getPeopleConfidence())) {
                String temp = "";
                for (Double s : licensePlate.getPeopleConfidence()) {
                    if(temp.isEmpty())
                        temp = String.valueOf(s);
                    else
                        temp = temp + "!@" + s;
                }

                odsLicensePlateValidation.setPeopleCountingConfidence(temp);//lista
            }
            odsLicensePlateValidation.setPeopleCountingImage(licensePlate.getPeopleCountingImage());
            odsLicensePlateValidation.setXaiPeopleCountingImage(licensePlate.getXaiPeopleCountingImage());
            odsLicensePlateValidation.setDatetime(licensePlate.getDatetime().toString());

            if(Objects.nonNull(licensePlate.getLicensePlate())) {
                List<OdsTravel> travels = odsTravelRepository.findAllByMeansOfTransportIdentification(licensePlate.getLicensePlate().trim().replace(" ",""));
                String transactionsUuid = "";
                if(Objects.nonNull(travels) && !travels.isEmpty()){
                    for(OdsTravel odsTravel : travels){
                        if(odsTravel.getPassingDatePlanned().isEqual(LocalDate.now())){
                            OdsTraveler odsTraveler = odsTravelerRepository.findFirstByTuuid(odsTravel.getPassengerId());
                            Transaction transaction = odsTransactionsRepository.findLastByTravelerIdNative(odsTraveler.getId());
                            if(transactionsUuid.isEmpty()){
                                if(Objects.nonNull(transaction))
                                    transactionsUuid = transaction.getTransactionUuid();
                            }else {
                                if(Objects.nonNull(transaction))
                                    transactionsUuid = transactionsUuid + "!!!" + transaction.getTransactionUuid();
                            }

                        }
                    }
                }


//                OdsTraveler odsTraveler = odsTravelerRepository.findFirstByLicensePlate(licensePlate.getLicensePlate());
//                Transaction transaction = odsTransactionsRepository.findLastByTravelerIdAndStatusDifClosedNative(odsTraveler.getId());
                odsLicensePlateValidation.setPassengers(transactionsUuid);
            }

            odsLicensePlateValidation.setTransactionUuid(carTransaction.getTransactionUuid());
            odsLicensePlateValidation.setTransactionStatus(carTransaction.getTransactionStatus());
            odsLicensePlateValidation.setLicensePlate(carTransaction.getLicensePlate());
            odsLicensePlateValidation.setLicensePlateConfidence(carTransaction.getLicensePlateConfidence());

            odsLicensePlateValidationRepository.save(odsLicensePlateValidation);
        }

        return odsLicensePlateValidation.getTransactionUuid();
    }

    @Operation(summary = "Please send the number of people along with the corresponding file from the XAI module")
    @PostMapping(value = "/api/uav/peopleCounting")
    public ResponseEntity<String> numberOfPeople(@RequestBody PeopleCountingDTO peopleCounting){
        if(peopleCounting != null)
            return new ResponseEntity<>("Received successfully", HttpStatus.OK);
        else
            return new ResponseEntity<>("Didn't receive successfully", HttpStatus.BAD_REQUEST);
    }

    @Operation(summary = "Please send the document number to get the corresponding traveler registration form")
    @GetMapping(value = "/api/uav/travelerRegistrationForm")
    public OdsTraveler getTravelerRegistrationForm(@RequestParam("document_number") String documentNumber){
        OdsTraveler odsTraveler = new OdsTraveler();
        if(documentNumber != null){
            if(odsTravelerRepository.existsByDocumentNumber(documentNumber)){
                odsTraveler = odsTravelerRepository.findFirstByDocumentNumber(documentNumber);
            }
        }
        return odsTraveler;
    }

    @Operation(summary = "Get all transactions with status different from CLOSED")
    @GetMapping(value = "/api/getAllOngoingTransactions")
    public List<Transaction> getAllTransactionsWithoutClosed(){
        List<Transaction> transactionsList = odsTransactionsRepository.findAllByTransactionStatusIsNotContainingIgnoreCase("CLOSED");

        if(transactionsList.isEmpty()){
            Transaction transaction1 = new Transaction(
                    "a1b2c3d4-e5f6-7g8h-9i0j-k1l2m3n4o5p6",
                    "ONGOING",
                    1001,
                    2001
            );
            odsTransactionsRepository.save(transaction1);
            Transaction transaction2 = new Transaction(
                    "z9y8x7w6-v5u4-t3s2-r1q0-p9o8n7m6l5k4",
                    "DECISION_PENDING",
                    1002,
                    2002
            );
            odsTransactionsRepository.save(transaction2);
            Transaction transaction3 = new Transaction(
                    "x1c2v3b4-n5m6-l7k8-j9h0-g5f4d3s2a1q0",
                    "DTC_PC_VERIFICATION_REQUIRED",
                    1003,
                    2003
            );
            odsTransactionsRepository.save(transaction3);
            Transaction transaction4 = new Transaction(
                    "m1n2b3v4-c5x6-z7y8-r9t0-q5w4e3d2f1a0",
                    "EMRTD_VERIFICATION_REQUIRED",
                    1004,
                    2004
            );
            odsTransactionsRepository.save(transaction4);
            Transaction transaction5 = new Transaction(
                    "l1k2j3h4-g5f6-d7s8-a9q0-w1e2r3t4y5u6",
                    "CLOSED",
                    1005,
                    2005
            );
            odsTransactionsRepository.save(transaction5);
        }

        transactionsList = odsTransactionsRepository.findAllByTransactionStatusIsNotContainingIgnoreCase("CLOSED");

        return transactionsList;
    }

    @Operation(summary = "Please send the transaction id to get the corresponding status of transaction")
    @GetMapping(value = "/api/getTransaction")
    public Transaction getTransaction(@RequestParam("transaction_id") String transactionId){
        Transaction transaction = new Transaction();
        if(transactionId != null) {
            transaction = odsTransactionsRepository.findFirstByTransactionUuid(transactionId);
        }

        return transaction;
    }

    @Operation(summary = "Please send the transaction id, decision and id of person who made the decision(optional) to update the transaction status")
    @PostMapping(value = "/api/setTransactionDecision")
    public ResponseEntity<String> setTransactionDecision(@RequestParam("transaction_id") String transactionId, @RequestParam("decision") @Parameter(description = "ONGOING, DECISION_PENDING, DTC_PC_VERIFICATION_REQUIRED, EMRTD_VERIFICATION_REQUIRED, DETAILED_VERIFICATION_NEEDED or CLOSED") String decision, @RequestParam(value = "actor",required = false) String actor){
        Transaction transaction = new Transaction();
        OdsDocumentValidation odsDocumentValidation;
        if(transactionId != null && decision != null) {

            transaction = odsTransactionsRepository.findFirstByTransactionUuid(transactionId);
            odsDocumentValidation = odsDocumentValidationRepository.findFirstByTransactionUuid(transactionId);
            transaction.setTransactionStatus(decision);
            odsDocumentValidation.setTransactionStatus(decision);
            if(actor != null)
                transaction.setPersonDecision(actor);

            if(!decision.equalsIgnoreCase("CLOSED")){
                OdsTravellerAlert odsTravellerAlert = new OdsTravellerAlert();
                odsTravellerAlert.setFirstname(odsDocumentValidation.getVcGivenName());
                odsTravellerAlert.setLastname(odsDocumentValidation.getVcSurname());
                odsTravellerAlert.setTransactionUuid(transactionId);
                odsTravellerAlert.setTransactionStatus(decision);
                DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                odsTravellerAlert.setDateTimeCreated(LocalDateTime.now().format(dateTimeFormatter));

                odsTravellerAlertRepository.save(odsTravellerAlert);
                alerts.add(odsTravellerAlert);
            }
            alertCount = alerts.size();
            odsTransactionsRepository.save(transaction);
            odsDocumentValidationRepository.save(odsDocumentValidation);
        }

        return ResponseEntity.ok("Transaction status was successfully updated.");
    }

    @Operation(summary = "Please send the transaction id to get the corresponding data of transaction")
    @GetMapping(value = "/api/getTransactionData")
    public OdsDocumentValidationDTO getTransactionData(@RequestParam("transaction_id") String transactionId){
        OdsDocumentValidation odsDocumentValidation;
        OdsDocumentValidationDTO odsDocumentValidationDTO = new OdsDocumentValidationDTO();
        if(transactionId != null) {
            odsDocumentValidation = odsDocumentValidationRepository.findFirstByTransactionUuid(transactionId);
            if(odsDocumentValidation != null){
                try {
                    OdsTraveler odsTraveler = odsTravelerRepository.findLastByDocumentNumberNative(odsDocumentValidation.getVcNumber());
                    if(!Objects.nonNull(odsDocumentValidation.getTravelId()) && odsTravelRepository.existsOdsTravelByPassengerId(odsTraveler.getTuuid())) {
                        Transaction transaction = odsTransactionsRepository.findFirstByTransactionUuid(transactionId);
                        OdsDocumentValidation odsDocumentValidationTemp = odsDocumentValidationRepository.findFirstByTransactionUuid(transactionId);
                        List<OdsTravel> odsTravels = odsTravelRepository.findAllByPassengerId(odsTraveler.getTuuid());
                        for(OdsTravel odsTravel : odsTravels){
                            if(odsTravel.getPassingDatePlanned().isAfter(LocalDate.now()) || odsTravel.getPassingDatePlanned().isEqual(LocalDate.now())){
                                transaction.setTravelId(odsTravel.getId());
                                odsDocumentValidationTemp.setTravelId(String.valueOf(odsTravel.getId()));
                            }
                        }
                        odsDocumentValidation.setTravelId(odsDocumentValidationTemp.getTravelId());
                        odsTransactionsRepository.save(transaction);
                        odsDocumentValidationRepository.save(odsDocumentValidationTemp);
                    }

                }catch (Exception e){
                    System.out.println("Cannot set travel id");
                }

                odsDocumentValidationDTO = migrateDataodsDocumentValidationToDTO(odsDocumentValidation);

                if(Objects.nonNull(odsDocumentValidation.getTravelId()))
                    odsDocumentValidationDTO.getTransaction().setTravelId(odsDocumentValidation.getTravelId());

            //de adaugat datele de la Leoni
            BehaviourBiometricsDTO  behaviourBiometricsDTO = callQuadibleApi(odsDocumentValidationDTO.getDtoVC().getMrz());
            if(Objects.isNull(behaviourBiometricsDTO.getStatus())){
                behaviourBiometricsDTO = callExternalApi();
            }

                if(Objects.nonNull(behaviourBiometricsDTO.getStatus())){
                    odsDocumentValidationDTO.getBehaviourBiometrics().setAuthenticated(behaviourBiometricsDTO.isAuthenticated());
                    odsDocumentValidationDTO.getBehaviourBiometrics().setStatus(behaviourBiometricsDTO.getStatus());
                    for(BehaviourBiometricsDTO.Feature feature : behaviourBiometricsDTO.getMetadata().getFeatures()){
                        odsDocumentValidationDTO.getBehaviourBiometrics().getMetadata().getFeatures().add(feature);
                    }

                }

                //de adaugat datele de la George
                BackgroundChecksDTO backgroundChecksDTO = callBackgroundChecksApiMock(odsDocumentValidationDTO.getDtoVC());
                if(Objects.nonNull(backgroundChecksDTO.getPerson().getGivenName())){
                    odsDocumentValidationDTO.getBackgroundChecks().setPerson(backgroundChecksDTO.getPerson());
                    odsDocumentValidationDTO.getBackgroundChecks().setEvents(backgroundChecksDTO.getEvents());
                    odsDocumentValidationDTO.getBackgroundChecks().setItems(backgroundChecksDTO.getItems());
                }
            }
        }

        return odsDocumentValidationDTO;
    }

    private OdsDocumentValidationDTO migrateDataodsDocumentValidationToDTO(OdsDocumentValidation odsDocumentValidation){
        OdsDocumentValidationDTO odsDocumentValidationDTO = new OdsDocumentValidationDTO();
        odsDocumentValidationDTO.getDtoVC().setExpirationOn(odsDocumentValidation.getVcExpirationOn());
        odsDocumentValidationDTO.getDtoVC().setGender(odsDocumentValidation.getVcGender());
        odsDocumentValidationDTO.getDtoVC().setDateOfBirth(odsDocumentValidation.getVcDateOfBirth());
        odsDocumentValidationDTO.getDtoVC().setGivenName(odsDocumentValidation.getVcGivenName());
        odsDocumentValidationDTO.getDtoVC().setSurname(odsDocumentValidation.getVcSurname());
        odsDocumentValidationDTO.getDtoVC().setMrz(odsDocumentValidation.getVcMrz());
        //odsDocumentValidation.setVcElectronic();
        //odsDocumentValidation.setVcIssuedOn();
        odsDocumentValidationDTO.getDtoVC().setNationality(odsDocumentValidation.getVcNationality());
        odsDocumentValidationDTO.getDtoVC().setType(odsDocumentValidation.getVcType());
        odsDocumentValidationDTO.getDtoVC().setTypeId(odsDocumentValidation.getVcTypeId());
        odsDocumentValidationDTO.getDtoVC().setTypeName(odsDocumentValidation.getVcTypeName());
        odsDocumentValidationDTO.getDtoVC().setNumber(odsDocumentValidation.getVcNumber());
        //odsDocumentValidation.setVcIssuerState();
        //populate with transaction data
        odsDocumentValidationDTO.getTransaction().setTransactionStatus(odsDocumentValidation.getTransactionStatus());
        odsDocumentValidationDTO.getTransaction().setTravelerId(String.valueOf(odsDocumentValidation.getTravelerId()));
        odsDocumentValidationDTO.getTransaction().setTransactionUuid(odsDocumentValidation.getTransactionUuid());

        //populate with data from VISB
        odsDocumentValidationDTO.getValidationStatus().setDocumentAuthenticated(odsDocumentValidation.isDocumentAuthenticated());
        odsDocumentValidationDTO.getValidationStatus().setLivenessCheckPassed(odsDocumentValidation.isLivenessCheckPassed());
        odsDocumentValidationDTO.getValidationStatus().setBiometryMatched(odsDocumentValidation.isBiometryMatched());

        if(Objects.nonNull(odsDocumentValidation.getBiometrics())) {
            String[] biometrics = odsDocumentValidation.getBiometrics().split("!@#%");

            OdsDocumentValidationDTO.Biometric biometric = new OdsDocumentValidationDTO.Biometric();

            biometric.setPosition(biometrics[0]);
            biometric.setType(biometrics[1]);
            biometric.setSource(biometrics[2]);
            biometric.setImage(biometrics[3]);
            biometric.setFormat(biometrics[4]);

            odsDocumentValidationDTO.getBiometrics().add(biometric);
            odsDocumentValidationDTO.setLanguage(odsDocumentValidation.getLanguage());
        }


        return odsDocumentValidationDTO;
    }

    public BehaviourBiometricsDTO callExternalApi() {
        BehaviourBiometricsDTO behaviourBiometricsDTO = new BehaviourBiometricsDTO();
        try {
            String jsonPayload = "{"
                    + "\"externalUserId\": \"LeoniOdysseusTest-Free-Home-Standing-1 Hand-Phone\","
                    + "\"externalClientId\": \"7c402fd2-8569-4a8a-9050-b08dd001a730\""
                    + "}";

            // Create the HttpClient
            HttpClient client = HttpClient.newBuilder()
                    .followRedirects(HttpClient.Redirect.NORMAL) // Enable redirect handling
                    .build();

            // Build the HTTP request
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI("http://api.quadible.io/external/auth"))
                    .header("x-admin-api-key", "8440a20d82de18b0d83468333cc9b7abf462fade")
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
                    .build();

            // Send the request and get the response
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if(response.statusCode() == 201){
                ObjectMapper objectMapper = new ObjectMapper();
                behaviourBiometricsDTO = objectMapper.readValue(response.body(), BehaviourBiometricsDTO.class);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
        return behaviourBiometricsDTO;
    }

    public BehaviourBiometricsDTO callQuadibleApi(String mrzString) {
        BehaviourBiometricsDTO behaviourBiometricsDTO = new BehaviourBiometricsDTO();
        try {
            // Prepare the JSON payload
            byte[] mrz = mrzString.getBytes(StandardCharsets.UTF_16);
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            digest.update(mrz);
            String id =  Hex.toHexString(digest.digest());

            String jsonPayload = "{"
                    + "\"externalUserId\": \""+ id +"\","
                    + "\"externalClientId\": \"7c402fd2-8569-4a8a-9050-b08dd001a730\""
                    + "}";

            // Create the HttpClient
            HttpClient client = HttpClient.newBuilder()
                    .followRedirects(HttpClient.Redirect.NORMAL) // Enable redirect handling
                    .build();

            // Build the HTTP request
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI("http://api.quadible.io/external/auth"))
                    .header("x-admin-api-key", "8440a20d82de18b0d83468333cc9b7abf462fade")
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
                    .build();

            // Send the request and get the response
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if(response.statusCode() == 201){
                ObjectMapper objectMapper = new ObjectMapper();
                behaviourBiometricsDTO = objectMapper.readValue(response.body(), BehaviourBiometricsDTO.class);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
        return behaviourBiometricsDTO;
    }

    public BackgroundChecksDTO callBackgroundChecksApiMock(OdsDocumentValidationDTO.DTC dtcVc) {
        BackgroundChecksDTO backgroundChecksDTO = new BackgroundChecksDTO();
        try {
            String jsonPayload = dtcVc.toString();

            // Create an instance of RestTemplate
            RestTemplate restTemplate = new RestTemplate();

            // Create HttpHeaders and set the necessary headers
            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-Type", "text/plain");  // Set Content-Type as text/plain


            // Create HttpEntity with the headers and request body
            HttpEntity<String> entity = new HttpEntity<>(jsonPayload, headers);

            // Send POST request
            String url = "http://49.13.196.96:1880/checks";  // Endpoint URL
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);


            if(response.getStatusCode().is2xxSuccessful()){
                ObjectMapper objectMapper = new ObjectMapper();
                backgroundChecksDTO = objectMapper.readValue(response.getBody(), BackgroundChecksDTO.class);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

        return backgroundChecksDTO;
    }


    @Operation(summary = "Car : Get all cars transactions with status different from CLOSED")
    @GetMapping(value = "/api/getAllCarsTransactions")
    public List<CarTransaction> getAllCarsTransactionsWithoutClosed(){
        List<CarTransaction> carsTransactionsList = odsCarTransactionsRepository.findAllByTransactionStatusIsNotContainingIgnoreCase("CLOSED");

        if(carsTransactionsList.isEmpty()){
            CarTransaction transaction1 = new CarTransaction(
                    "a1b2c3d4-e5f6-7g8h-9i0j-k1l2m3n4o5p6",
                    "ONGOING",
                    "IS18UNG",
                    "0.96"
            );
            odsCarTransactionsRepository.save(transaction1);
            CarTransaction transaction2 = new CarTransaction(
                    "z9y8x7w6-v5u4-t3s2-r1q0-p9o8n7m6l5k4",
                    "DECISION_PENDING",
                    "B19ALB",
                    "0.92"
            );
            odsCarTransactionsRepository.save(transaction2);
        }

        carsTransactionsList = odsCarTransactionsRepository.findAllByTransactionStatusIsNotContainingIgnoreCase("CLOSED");

        return carsTransactionsList;
    }

    @Operation(summary = "Car : Please send the car transaction id, decision and id of person who made the decision(optional) to update the car transaction status")
    @PostMapping(value = "/api/setCarTransactionDecision")
    public ResponseEntity<String> setCarTransactionDecision(@RequestParam("transaction_id") String transactionId, @RequestParam("decision") @Parameter(description = "ONGOING, DECISION_PENDING or CLOSED") String decision, @RequestParam(value = "actor",required = false) String actor){
        CarTransaction carTransaction = new CarTransaction();
        OdsLicensePlateValidation odsLicensePlateValidation;
        if(transactionId != null && decision != null) {
            carTransaction = odsCarTransactionsRepository.findFirstByTransactionUuid(transactionId);
            odsLicensePlateValidation = odsLicensePlateValidationRepository.findFirstByTransactionUuid(transactionId);
            carTransaction.setTransactionStatus(decision);
            odsLicensePlateValidation.setTransactionStatus(decision);
            if(actor != null)
                carTransaction.setPersonDecision(actor);

            if(!decision.equalsIgnoreCase("CLOSED")){
                OdsCarAlert odsCarAlert = new OdsCarAlert();
                DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                odsCarAlert.setDateTimeCreated(LocalDateTime.now().format(dateTimeFormatter));
                odsCarAlert.setLicencePlate(odsLicensePlateValidation.getLicensePlate());
                odsCarAlert.setTransactionUuid(transactionId);
                odsCarAlert.setTransactionStatus(decision);

                odsCarAlertRepository.save(odsCarAlert);
                alertsCar.add(odsCarAlert);
            }
            alertCountCars = alertsCar.size();
            odsCarTransactionsRepository.save(carTransaction);
            odsLicensePlateValidationRepository.save(odsLicensePlateValidation);
        }

        return ResponseEntity.ok("Car transaction status was successfully updated.");
    }

    @Operation(summary = "Car : Please send the car transaction id to get the corresponding data of car transaction")
    @GetMapping(value = "/api/getCarTransactionData")
    public OdsLicensePlateValidationDTO getCarTransactionData(@RequestParam("transaction_id") String transactionId){
        OdsLicensePlateValidation odsLicensePlateValidation;
        OdsLicensePlateValidationDTO licensePlateValidationDTO = new OdsLicensePlateValidationDTO();
        if(transactionId != null) {
            odsLicensePlateValidation = odsLicensePlateValidationRepository.findFirstByTransactionUuid(transactionId);
            if(odsLicensePlateValidation != null){
                licensePlateValidationDTO = migrateDataFromOdsLicensePlateValidationToDTO(odsLicensePlateValidation);
            }
        }

        return licensePlateValidationDTO;
    }

    private OdsLicensePlateValidationDTO migrateDataFromOdsLicensePlateValidationToDTO(OdsLicensePlateValidation odsLicensePlateValidation){
        OdsLicensePlateValidationDTO licensePlateValidationDTO = new OdsLicensePlateValidationDTO();

        licensePlateValidationDTO.getPlateRecognition().setCarColor(odsLicensePlateValidation.getCarColor());
        licensePlateValidationDTO.getPlateRecognition().setColorConfidence(odsLicensePlateValidation.getColorConfidence());
        licensePlateValidationDTO.getPlateRecognition().setColorReasoning(odsLicensePlateValidation.getColorReasoning());
        licensePlateValidationDTO.getPlateRecognition().setCarType(odsLicensePlateValidation.getCarType());
        licensePlateValidationDTO.getPlateRecognition().setTypeConfidence(odsLicensePlateValidation.getTypeConfidence());
        licensePlateValidationDTO.getPlateRecognition().setTypeReasoning(odsLicensePlateValidation.getTypeReasoning());
        licensePlateValidationDTO.getPlateRecognition().setLicensePlate(odsLicensePlateValidation.getLicensePlate());
        licensePlateValidationDTO.getPlateRecognition().setLicensePlateConfidence(odsLicensePlateValidation.getLicensePlateConfidence());
        licensePlateValidationDTO.getPlateRecognition().setLicensePlateImage(odsLicensePlateValidation.getLicensePlateImage());
        licensePlateValidationDTO.getPlateRecognition().setXaiLicensePlateImage(odsLicensePlateValidation.getXaiLicensePlateImage());
        licensePlateValidationDTO.getPlateRecognition().setPeopleCounting(odsLicensePlateValidation.getPeopleCounting());

        if(Objects.nonNull(odsLicensePlateValidation.getPeopleCountingConfidence())) {
            String[] peopleCountingConfidenceArray = odsLicensePlateValidation.getPeopleCountingConfidence().split("!@");

            List<String> tempList = new ArrayList<>();
            for (String s : peopleCountingConfidenceArray) {
                tempList.add(s);
            }

            licensePlateValidationDTO.getPlateRecognition().setPeopleCountingConfidence(tempList);//lista
        }
        licensePlateValidationDTO.getPlateRecognition().setPeopleCountingImage(odsLicensePlateValidation.getPeopleCountingImage());
        licensePlateValidationDTO.getPlateRecognition().setXaiPeopleCountingImage(odsLicensePlateValidation.getXaiPeopleCountingImage());
        licensePlateValidationDTO.getPlateRecognition().setDatetime(odsLicensePlateValidation.getDatetime());

        if(Objects.nonNull(odsLicensePlateValidation.getPassengers())) {
            List<PassengersDTO> tempList2 = new ArrayList<>();
            if(!odsLicensePlateValidation.getPassengers().contains("!!!")){
                OdsDocumentValidation odsDocumentValidation = odsDocumentValidationRepository.findFirstByTransactionUuid(odsLicensePlateValidation.getPassengers());
                if(Objects.nonNull(odsDocumentValidation)) {
                    PassengersDTO passenger = new PassengersDTO();
                    passenger.setFirstname(odsDocumentValidation.getVcGivenName());
                    passenger.setLastname(odsDocumentValidation.getVcSurname());
                    passenger.setTransactionUuid(odsLicensePlateValidation.getPassengers());
                    passenger.setTransactionStatus(odsDocumentValidation.getTransactionStatus());
                    tempList2.add(passenger);
                }
            }else {
                String[] passangersArray = odsLicensePlateValidation.getPassengers().split("!!!");
                for(String p : passangersArray){
                    OdsDocumentValidation odsDocumentValidation = odsDocumentValidationRepository.findFirstByTransactionUuid(p);
                    if(Objects.nonNull(odsDocumentValidation)) {
                        PassengersDTO passenger = new PassengersDTO();
                        passenger.setFirstname(odsDocumentValidation.getVcGivenName());
                        passenger.setLastname(odsDocumentValidation.getVcSurname());
                        passenger.setTransactionUuid(p);
                        passenger.setTransactionStatus(odsDocumentValidation.getTransactionStatus());
                        tempList2.add(passenger);
                    }
                }
            }

            licensePlateValidationDTO.setPassengers(tempList2);//lista
        }
        licensePlateValidationDTO.getCarTransaction().setTransactionUuid(odsLicensePlateValidation.getTransactionUuid());
        licensePlateValidationDTO.getCarTransaction().setTransactionStatus(odsLicensePlateValidation.getTransactionStatus());
        licensePlateValidationDTO.getCarTransaction().setLicensePlate(odsLicensePlateValidation.getLicensePlate());
        licensePlateValidationDTO.getCarTransaction().setLicensePlateConfidence(odsLicensePlateValidation.getLicensePlateConfidence());
        licensePlateValidationDTO.getCarTransaction().setPersonDecision(odsLicensePlateValidation.getPersonDecision());


        return licensePlateValidationDTO;
    }

    public void loadTravelDetails(Integer travelId){
        if(travelId != null && odsTravelRepository.existsOdsTravelById(travelId)){
            selectedTravel = odsTravelRepository.getById(travelId);
            PrimeFaces.current().ajax().update("dialogTravelDetails");
        }else {
            selectedTravel = new OdsTravel();
            PrimeFaces.current().ajax().update("dialogTravelDetails");
        }
    }

    public void loadCarDetails(String transactionUuid) {
        if (transactionUuid != null && odsLicensePlateValidationRepository.existsByTransactionUuid(transactionUuid)) {
            selectedCar = odsLicensePlateValidationRepository.findFirstByTransactionUuid(transactionUuid);
            processPassengers(selectedCar.getPassengers());
            PrimeFaces.current().ajax().update("dialogCarDetails");
            PrimeFaces.current().ajax().update("dialogPassengersDetails");
        }
        else {
            selectedCar = new OdsLicensePlateValidation();
            PrimeFaces.current().ajax().update("dialogCarDetails");
        }
    }
    private void processPassengers(String uuid) {
        if (uuid != null && !uuid.isEmpty()) {

            String[] uuidIds = uuid.split("!!!");

            for (String uuidId : uuidIds) {

//                System.out.println("Transaction UUID: " + uuidId);
                Transaction transaction = odsTransactionsRepository.findFirstByTransactionUuid(uuidId);

                if (transaction != null) {
                    Optional<OdsTraveler> traveler = odsTravelerRepository.findById(transaction.getTravelerId());
                    if (traveler.isPresent()) {
//                        System.out.println("Passenger Details: " + traveler);
                        travelerList.add(traveler.get());
                    } else {
                        System.out.println("Passenger not found for UUID: " + uuidId);
                    }
                } else {
                    System.out.println("Transaction not found for UUID: " + uuidId);
                }
            }
        } else {
            System.out.println("No passengers found.");
        }
    }
    public void clearTravelerList() {
        travelerList.clear();
    }

    public void loadTravelerDetails(Integer travelerId) throws IOException {
        if(travelerId != null && odsTravelerRepository.existsById(travelerId)){
            selectedTraveler = odsTravelerRepository.getById(travelerId);
            travelerImageBytes = selectedTraveler.getPicture();
            PrimeFaces.current().ajax().update("dialogTravelerDetails");
        }else {
            selectedTraveler = new OdsTraveler();
            travelerImageBytes = null;
            getTravelerImage();
            PrimeFaces.current().ajax().update("dialogTravelerDetails");
        }
    }
    public void testPF() {
        System.out.println("testez functia");
        PrimeFaces.current().executeScript("PF('managePassengersDetails').show()");
    }
    public void loadPassengerDetails(Integer travelerId)  {
        System.out.println("Passenger id");
        if(travelerId != null && odsTravelerRepository.existsById(travelerId)){
            selectedPassenger = odsTravelerRepository.getById(travelerId);
            System.out.println("tewtwetewtew");

            PrimeFaces.current().ajax().update("dialogPassengersDetails");
        }else {
            selectedPassenger = new OdsTraveler();
            PrimeFaces.current().ajax().update("dialogPassengersDetails");
        }
    }

    public StreamedContent getTravelerImage() throws IOException {
        if(travelerImageBytes != null && imageConversionJp2ToJpeg.getImageContentType(travelerImageBytes).equals("image/jp2")){
            travelerImageBytes = imageConversionJp2ToJpeg.convertJP2ToJPEG(travelerImageBytes);
        }

        if (travelerImageBytes != null) {
            return DefaultStreamedContent.builder()
                    .stream(() -> new ByteArrayInputStream(travelerImageBytes))
                    .contentType("image/jpeg")
                    .build();
        }
        return null;
    }

    public void clearAlerts() {
        alerts.clear();
        alertCount = 0;
        odsTravellerAlertRepository.deleteAll();
        PrimeFaces.current().ajax().update("odsUserForm");
        PrimeFaces.current().ajax().update("alertsForm");
        PrimeFaces.current().executeScript("PF('alertsDialog').hide()");
    }
    public void clearCarAlerts() {
        alertsCar.clear();
        alertCountCars = 0;
        odsCarAlertRepository.deleteAll();
        PrimeFaces.current().ajax().update("odsCarTransactions");
        PrimeFaces.current().ajax().update("alertsCarForm");
        PrimeFaces.current().executeScript("PF('alertsCarDialog').hide()");
    }
    public void findTransactionsByUuid(OdsTravellerAlert alert){
        transactionList = odsTransactionsRepository.findAllByTransactionUuid(alert.getTransactionUuid());
        alerts.remove(alert);
        odsTravellerAlertRepository.deleteById(alert.getId());
        alertCount = alerts.size();
        PrimeFaces.current().ajax().update("odsUserForm");
    }

    public void resetPassengersPage(){
        alerts = odsTravellerAlertRepository.findAll();
        alertCount = alerts.size();
        transactionList = odsTransactionsRepository.findAll();
        PrimeFaces.current().ajax().update("odsUserForm");
        PrimeFaces.current().ajax().update("alertsForm");
    }

    public void handleAlert() {
        alerts = odsTravellerAlertRepository.findAll();
        alertCount = alerts.size();
    }

    // CAR PAGE

    public void findCarTransactionsByUuid(OdsCarAlert alert){
        carTransactionList = odsCarTransactionsRepository.findAllByTransactionUuid(alert.getTransactionUuid());
        alertsCar.remove(alert);
        odsCarAlertRepository.deleteById(alert.getId());
        alertCountCars = alertsCar.size();
        PrimeFaces.current().ajax().update("odsCarTransactions");
    }

    public void resetCarsPage(){
        alertsCar = odsCarAlertRepository.findAll();
        alertCountCars = alertsCar.size();
        carTransactionList = odsCarTransactionsRepository.findAll();
        PrimeFaces.current().ajax().update("odsCarTransactions");
        PrimeFaces.current().ajax().update("alertsCarForm");
    }

    public void handleCarAlert() {
        alertsCar = odsCarAlertRepository.findAll();
        alertCountCars = alertsCar.size();
    }


}
