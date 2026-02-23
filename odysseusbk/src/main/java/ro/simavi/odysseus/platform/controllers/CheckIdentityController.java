package ro.simavi.odysseus.platform.controllers;

import eu.odysseys.dtc.parser.Dg1Data;
import eu.odysseys.dtc.parser.DtcParser;
import io.swagger.v3.oas.annotations.Operation;
import lombok.Getter;
import lombok.Setter;

import org.primefaces.PrimeFaces;
import org.primefaces.event.CaptureEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.http.*;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.multipart.MultipartFile;
import ro.simavi.odysseus.platform.entities.*;
import ro.simavi.odysseus.platform.repositories.*;
import ro.simavi.odysseus.platform.services.OdsAuditLogService;


import javax.faces.FacesException;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.imageio.stream.FileImageOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Base64;
import java.util.Objects;
import java.util.Random;
import java.util.UUID;


@Setter
@Getter
@Component
@Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)
@RestController
public class CheckIdentityController {
    @Autowired
    private WorkflowInstanceController workflowInstanceController;

    @Autowired
    private OdsAuditLogService odsAuditLogService;

    @Value("${verifyIdentityLink}")
    private String verifyIdentityLink;

    @Value("${resourceTokenUrl}")
    private String resourceTokenUrl;

    @Value("${clientId}")
    private String clientId;

    @Value("${username}")
    private String username;

    @Value("${password}")
    private String password;

    @Autowired
    private OdsNotRecognizedPicturesRepository odsNotRecognizedPicturesRepository;

    @Autowired
    private OdsDocumentValidationRepository odsDocumentValidationRepository;

    private String filename;
    private String nameForImage;
    private byte[] imageContent;

    boolean check;
    @Autowired
    private OdsTravelRepository odsTravelRepository;
    @Autowired
    private OdsTravelerRepository odsTravelerRepository;
    @Autowired
    private OdsTransactionsRepository odsTransactionsRepository;

    public void onPageLoad() {
        if (check)
            filename = null;
    }

    public void oncapture(CaptureEvent captureEvent) {
        filename = "photoCam";
        imageContent = captureEvent.getData();
        check = false;
        String currentUser = null;
        try {
            currentUser = FacesContext.getCurrentInstance().getExternalContext().getRemoteUser();
        } catch (Exception ex) {

        }
        odsAuditLogService.startTravelerProcess(this.getClass().getName(), "capture", currentUser);
        String statusVerification = startIdentityVerificationTemp(imageContent);
        if (statusVerification.equalsIgnoreCase("Successfully")) {
            FacesMessage msg = new FacesMessage("You successfully verified your identity!");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            workflowInstanceController.createWorkflowInstance("checkIdentity", null);
        } else if (statusVerification.equalsIgnoreCase("Error")) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "You couldn`t verified your identity!", "");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
        odsAuditLogService.endTravelerProcess(this.getClass().getName(), "capture", currentUser, statusVerification);
    }

    public String startIdentityVerificationTemp(byte[] image) {
        Random random = new Random();

        double randomValue = random.nextDouble();

        if (randomValue < 0.95)
            return "Successfully";
        else
            return "Error";
    }

    public String startIdentityVerification(byte[] image) {
        String apiUrl = verifyIdentityLink;
        String currentUser = null;
        try {
            currentUser = FacesContext.getCurrentInstance().getExternalContext().getRemoteUser();
        } catch (Exception ex) {

        }
        odsAuditLogService.startTravelerProcess(this.getClass().getName(), "startIdentityVerification", currentUser);

        KeycloackRestCalls keycloackRestCalls = new KeycloackRestCalls();
        String authToken = keycloackRestCalls.getAccessToken(resourceTokenUrl, clientId, username, password);

        // Create a RestTemplate instance
        RestTemplate restTemplate = new RestTemplate();

        // Create headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        headers.set("Authorization", "Bearer " + authToken);

        // Create the request body
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("image", image);

        // Set the chunked encoding streaming mode
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setBufferRequestBody(false); // Enable streaming
        restTemplate.setRequestFactory(requestFactory);

        // Create the HTTP entity with headers and body
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        // Send POST request
        ResponseEntity<String> responseEntity = restTemplate.exchange(apiUrl, HttpMethod.POST, requestEntity, String.class);

        // Print response
        System.out.println("API Check Identity Response: " + responseEntity.getBody());
        odsAuditLogService.endTravelerProcess(this.getClass().getName(), "startIdentityVerification", currentUser, "statusVerificationm", responseEntity.getBody(), null);
        return responseEntity.getBody();
    }

    @Operation(summary = "BCP is checking the identityb by entering the image")
    @PostMapping("/checkIdentityByBCP")
    public ResponseEntity<String> checkIdentityTraveler(@RequestParam("image") MultipartFile image, @RequestParam("tuuid") String uuid) throws IOException {
        String currentUser = null;
        try {
            currentUser = FacesContext.getCurrentInstance().getExternalContext().getRemoteUser();
        } catch (Exception ex) {

        }
        odsAuditLogService.startTravelerProcess(this.getClass().getName(), "checkIdentityByBCP", currentUser, uuid, image.getOriginalFilename());

        System.out.println(uuid);
        System.out.println(image.getOriginalFilename());
        odsAuditLogService.endTravelerProcess(this.getClass().getName(), "checkIdentityByBCP", currentUser, "statusVerificationm", "responseEntity.getBody()", image.getName());
        return ResponseEntity.ok("Successfully");
    }

    @Operation(summary = "BCP is searchin 1:n the identity by entering the image")
    @PostMapping("/searchIdentityTravelerByBCP")
    public ResponseEntity<String> checkIdentityForTraveler(@RequestParam("image") byte[] image) throws IOException {
        String currentUser = null;
        try {
            currentUser = FacesContext.getCurrentInstance().getExternalContext().getRemoteUser();
        } catch (Exception ex) {

        }
        odsAuditLogService.startTravelerProcess(this.getClass().getName(), "searchIdentityTravelerByBCP", currentUser);
        Random random = new Random();

        double randomValue = random.nextDouble();
        odsAuditLogService.endTravelerProcess(this.getClass().getName(), "searchIdentityTravelerByBCP", currentUser, "" + randomValue, "Mokup", null);
        if (randomValue < 0.95)
            return ResponseEntity.ok("Successfully");
        else
            return ResponseEntity.ok("Error");

    }

    public StreamedContent getImage() {
        check = true;
        return DefaultStreamedContent.builder()
                .contentType("image/jpeg")
                .stream(() -> {
                    if (imageContent == null) {
                        return null;
                    }

                    try {
                        return new ByteArrayInputStream(imageContent);
                    } catch (Exception e) {
                        e.printStackTrace();
                        return null;
                    }
                })
                .build();
    }

    @Operation(summary = "Please send the data when an image is recognized")
    @PostMapping("/api/faceRecognition")
    public ResponseEntity<String> faceRecognition(@RequestBody FaceVerificationDTO faceVerification) {
        String currentUser = null;
        try {
            currentUser = FacesContext.getCurrentInstance().getExternalContext().getRemoteUser();
        } catch (Exception ex) {

        }
        odsAuditLogService.startTravelerProcess(this.getClass().getName(), "get-faceRecognition", currentUser);

        if (faceVerification != null && faceVerification.getResult().replace(" ", "").equalsIgnoreCase("notok")) {
            OdsNotRecognizedPictures odsNotRecognizedPictures = new OdsNotRecognizedPictures();
            odsNotRecognizedPictures.setSubject(faceVerification.getSubject());
            odsNotRecognizedPictures.setImage(Base64.getDecoder().decode(faceVerification.getImage()));
            odsNotRecognizedPictures.setRecognitionStatus(faceVerification.getResult());
            odsNotRecognizedPictures.setRegistrationDate(LocalDate.now());
            odsNotRecognizedPictures.setDescription("The image wasn't recognized");
            odsNotRecognizedPictures.setImageSource(faceVerification.getImage());
            odsNotRecognizedPicturesRepository.save(odsNotRecognizedPictures);
        }

        odsAuditLogService.endTravelerProcess(this.getClass().getName(), "get-faceRecognition", currentUser, "" + faceVerification.getResult(), "Mokup", faceVerification.getSubject());
        return ResponseEntity.ok("Face recognition data received successfully.");
    }

    @Operation(summary = "Please send the data when an image is identified from N images")
    @PostMapping("/api/faceIdentification")
    public ResponseEntity<String> faceIdentification(@RequestBody FaceVerificationDTO faceVerification) {
        String currentUser = null;
        try {
            currentUser = FacesContext.getCurrentInstance().getExternalContext().getRemoteUser();
        } catch (Exception ex) {

        }
        odsAuditLogService.startTravelerProcess(this.getClass().getName(), "get-faceIdentification", currentUser);
        if (faceVerification != null && faceVerification.getResult().replace(" ", "").equalsIgnoreCase("notok")) {
            OdsNotRecognizedPictures odsNotRecognizedPictures = new OdsNotRecognizedPictures();
            odsNotRecognizedPictures.setSubject(faceVerification.getSubject());
            odsNotRecognizedPictures.setImage(Base64.getDecoder().decode(faceVerification.getImage()));
            odsNotRecognizedPictures.setRecognitionStatus(faceVerification.getResult());
            odsNotRecognizedPictures.setRegistrationDate(LocalDate.now());
            odsNotRecognizedPictures.setDescription("The image wasn't identified from N images");
            odsNotRecognizedPictures.setImageSource(faceVerification.getImage());
            odsNotRecognizedPicturesRepository.save(odsNotRecognizedPictures);
        }
        odsAuditLogService.endTravelerProcess(this.getClass().getName(), "get-faceRecognition", currentUser, "" + faceVerification.getResult(), "Mokup", faceVerification.getSubject());
        return ResponseEntity.ok("Face identification data received successfully.");
    }

    @Operation(summary = "Please send the data when an image is recognized")
    @PostMapping("/api/faceRecognitionVbox")
    public ResponseEntity<String> faceRecognitionVbox(@RequestBody CheckFaceVboxDTO faceVerification) {
        String currentUser = null;
        try {
            currentUser = FacesContext.getCurrentInstance().getExternalContext().getRemoteUser();
        } catch (Exception ex) {

        }
        odsAuditLogService.startTravelerProcess(this.getClass().getName(), "get-faceRecognition", currentUser);

        if (faceVerification != null && !faceVerification.getValidationStatus().isBiometryMatched()) {
            OdsNotRecognizedPictures odsNotRecognizedPictures = new OdsNotRecognizedPictures();
            odsNotRecognizedPictures.setSubject(faceVerification.getDocument().getMrz());
            odsNotRecognizedPictures.setImage(Base64.getDecoder().decode(faceVerification.getBiometrics().get(0).getImage()));
            odsNotRecognizedPictures.setRecognitionStatus(String.valueOf(faceVerification.getValidationStatus().isBiometryMatched()));
            odsNotRecognizedPictures.setRegistrationDate(LocalDate.now());
            odsNotRecognizedPictures.setDescription("The image wasn't recognized");
            odsNotRecognizedPictures.setImageSource(faceVerification.getBiometrics().get(0).getSource());
            odsNotRecognizedPicturesRepository.save(odsNotRecognizedPictures);
        }

        odsAuditLogService.endTravelerProcess(this.getClass().getName(), "get-faceRecognition", currentUser, "" + faceVerification.getValidationStatus().isBiometryMatched(), "Mokup", faceVerification.getDocument().getMrz());
        return ResponseEntity.ok("Face recognition data received successfully.");
    }

    @Operation(summary = "Please send the data when an image is identified from N images. Create transaction")
    @PostMapping("/api/faceIdentificationVbox")
    public String faceIdentificationVbox(@RequestBody CheckFaceVboxDTO faceVerification, @RequestParam("user_id") String userId) throws IOException {
        String currentUser = null;
        try {
            currentUser = FacesContext.getCurrentInstance().getExternalContext().getRemoteUser();
        } catch (Exception ex) {

        }
        odsAuditLogService.startTravelerProcess(this.getClass().getName(), "get-faceIdentification", currentUser);

        Transaction transaction = new Transaction();
        transaction.setTransactionStatus("ONGOING");
        transaction.setTransactionUuid(UUID.randomUUID().toString());
        OdsTraveler odsTraveler = odsTravelerRepository.findFirstByTuuid(userId);
        transaction.setTravelerId(odsTraveler.getId());

        odsTransactionsRepository.save(transaction);

        OdsDocumentValidation odsDocumentValidation = new OdsDocumentValidation();
        if(Objects.nonNull(odsTraveler.getDtcVC()) && faceVerification != null) {
            Dg1Data dg1Data = verifyDTCVC(odsTraveler.getDtcVC());
            odsDocumentValidation.setVcExpirationOn(dg1Data.getExpirationDate());
            odsDocumentValidation.setVcGender(dg1Data.getSex());
            odsDocumentValidation.setVcDateOfBirth(dg1Data.getDateOfBirth());
            odsDocumentValidation.setVcGivenName(dg1Data.getGivenName());
            odsDocumentValidation.setVcSurname(dg1Data.getLastName());
            odsDocumentValidation.setVcMrz(dg1Data.getMrz());
            //odsDocumentValidation.setVcElectronic();
            //odsDocumentValidation.setVcIssuedOn();
            odsDocumentValidation.setVcNationality(dg1Data.getNationality());
            odsDocumentValidation.setVcType(dg1Data.getDocumentType());
            odsDocumentValidation.setVcTypeId(dg1Data.getDocumentNumber());
            if(Objects.nonNull(dg1Data.getDocumentType()) && dg1Data.getDocumentType().substring(0,1).equalsIgnoreCase("p"))
                odsDocumentValidation.setVcTypeName("Passport");
            else if (Objects.nonNull(dg1Data.getDocumentType()) && dg1Data.getDocumentType().substring(0,1).equalsIgnoreCase("i")) {
                odsDocumentValidation.setVcTypeName("ID");
            }
            odsDocumentValidation.setVcNumber(dg1Data.getDocumentNumber());
            //odsDocumentValidation.setVcIssuerState();
            //populate with transaction data
            odsDocumentValidation.setTransactionStatus(transaction.getTransactionStatus());
            odsDocumentValidation.setTravelerId(String.valueOf(transaction.getTravelerId()));
            odsDocumentValidation.setTransactionUuid(transaction.getTransactionUuid());

            //populate with data from VISB
            odsDocumentValidation.setDocumentAuthenticated(true);
            odsDocumentValidation.setLivenessCheckPassed(faceVerification.getValidationStatus().isLivenessCheckPassed());
            odsDocumentValidation.setBiometryMatched(faceVerification.getValidationStatus().isBiometryMatched());


            String biometrics = faceVerification.getBiometrics().get(0).getPosition() + "!@#%" +
                    faceVerification.getBiometrics().get(0).getType() + "!@#%" +
                    faceVerification.getBiometrics().get(0).getSource() + "!@#%" +
                    faceVerification.getBiometrics().get(0).getImage() + "!@#%" +
                    faceVerification.getBiometrics().get(0).getFormat();

            odsDocumentValidation.setBiometrics(biometrics);

            odsDocumentValidation.setLanguage(faceVerification.getLanguage());

            odsDocumentValidationRepository.save(odsDocumentValidation);

        }
        if (faceVerification != null && !faceVerification.getValidationStatus().isBiometryMatched()) {
            OdsNotRecognizedPictures odsNotRecognizedPictures = new OdsNotRecognizedPictures();
            odsNotRecognizedPictures.setSubject(faceVerification.getDocument().getMrz());
            odsNotRecognizedPictures.setImage(Base64.getDecoder().decode(faceVerification.getBiometrics().get(0).getImage()));
            odsNotRecognizedPictures.setRecognitionStatus(String.valueOf(faceVerification.getValidationStatus().isBiometryMatched()));
            odsNotRecognizedPictures.setRegistrationDate(LocalDate.now());
            odsNotRecognizedPictures.setDescription("The image wasn't recognized");
            odsNotRecognizedPictures.setImageSource(faceVerification.getBiometrics().get(0).getSource());
            odsNotRecognizedPicturesRepository.save(odsNotRecognizedPictures);
        }
        odsAuditLogService.endTravelerProcess(this.getClass().getName(), "get-faceIdentification", currentUser, "" + faceVerification.getValidationStatus().isBiometryMatched(), "Mokup", faceVerification.getDocument().getMrz());
        return transaction.getTransactionUuid();
    }

    private Dg1Data verifyDTCVC(String dtcvc) throws IOException {
        byte[] dtc_vc = Base64.getDecoder().decode(dtcvc);
        DtcParser dtcParser = new DtcParser();
        Dg1Data dg1Data = dtcParser.decode(dtc_vc);

        return dg1Data;
    }


    @Operation(summary = "DTC-PC verification")
    @PostMapping("/api/addVerificationDTCPC")
    public ResponseEntity<String> verificationDTCPC(@RequestBody CheckFaceVboxDTO faceVerification, @RequestParam("transaction_id") String transactionId) {
        String currentUser = null;
        try {
            currentUser = FacesContext.getCurrentInstance().getExternalContext().getRemoteUser();
        } catch (Exception ex) {

        }
        odsAuditLogService.startTravelerProcess(this.getClass().getName(), "get-faceIdentification", currentUser);

        if (faceVerification != null && !faceVerification.getValidationStatus().isBiometryMatched()) {
            OdsNotRecognizedPictures odsNotRecognizedPictures = new OdsNotRecognizedPictures();
            odsNotRecognizedPictures.setSubject(faceVerification.getDocument().getMrz());
            odsNotRecognizedPictures.setImage(Base64.getDecoder().decode(faceVerification.getBiometrics().get(0).getImage()));
            odsNotRecognizedPictures.setRecognitionStatus(String.valueOf(faceVerification.getValidationStatus().isBiometryMatched()));
            odsNotRecognizedPictures.setRegistrationDate(LocalDate.now());
            odsNotRecognizedPictures.setDescription("The image wasn't recognized");
            odsNotRecognizedPictures.setImageSource(faceVerification.getBiometrics().get(0).getSource());
            odsNotRecognizedPicturesRepository.save(odsNotRecognizedPictures);
        }
        odsAuditLogService.endTravelerProcess(this.getClass().getName(), "get-faceRecognition", currentUser, "" + faceVerification.getValidationStatus().isBiometryMatched(), "Mokup", faceVerification.getDocument().getMrz());
        return ResponseEntity.ok("Data received successfully.");
    }

    @Operation(summary = "EMRTD verification")
    @PostMapping("/api/addVerificationEMRTD")
    public ResponseEntity<String> verificationEMRTD(@RequestBody CheckFaceVboxDTO faceVerification, @RequestParam("transaction_id") String transactionId) {
        String currentUser = null;
        try {
            currentUser = FacesContext.getCurrentInstance().getExternalContext().getRemoteUser();
        } catch (Exception ex) {

        }
        odsAuditLogService.startTravelerProcess(this.getClass().getName(), "get-faceIdentification", currentUser);

        if (faceVerification != null && !faceVerification.getValidationStatus().isBiometryMatched()) {
            OdsNotRecognizedPictures odsNotRecognizedPictures = new OdsNotRecognizedPictures();
            odsNotRecognizedPictures.setSubject(faceVerification.getDocument().getMrz());
            odsNotRecognizedPictures.setImage(Base64.getDecoder().decode(faceVerification.getBiometrics().get(0).getImage()));
            odsNotRecognizedPictures.setRecognitionStatus(String.valueOf(faceVerification.getValidationStatus().isBiometryMatched()));
            odsNotRecognizedPictures.setRegistrationDate(LocalDate.now());
            odsNotRecognizedPictures.setDescription("The image wasn't recognized");
            odsNotRecognizedPictures.setImageSource(faceVerification.getBiometrics().get(0).getSource());
            odsNotRecognizedPicturesRepository.save(odsNotRecognizedPictures);
        }
        odsAuditLogService.endTravelerProcess(this.getClass().getName(), "get-faceRecognition", currentUser, "" + faceVerification.getValidationStatus().isBiometryMatched(), "Mokup", faceVerification.getDocument().getMrz());
        return ResponseEntity.ok("Data received successfully.");
    }

}
