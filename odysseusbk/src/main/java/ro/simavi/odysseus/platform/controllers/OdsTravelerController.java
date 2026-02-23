package ro.simavi.odysseus.platform.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import eu.odysseys.dtc.parser.Dg1Data;
import eu.odysseys.dtc.parser.DtcParser;
import io.swagger.v3.oas.annotations.Operation;
import lombok.Getter;
import lombok.Setter;
import org.primefaces.PrimeFaces;
import org.primefaces.event.CaptureEvent;
import org.primefaces.event.FlowEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.data.domain.Example;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.multipart.MultipartFile;
import ro.simavi.odysseus.platform.dto.MrzDTO;
import ro.simavi.odysseus.platform.dto.TransactionDTO;
import ro.simavi.odysseus.platform.entities.*;
import ro.simavi.odysseus.platform.repositories.*;
import ro.simavi.odysseus.platform.services.OdsAuditLogService;
import ro.simavi.odysseus.platform.services.OdsTravelerService;
import ro.simavi.odysseus.platform.services.OdsWorkflowInstanceNodeService;
import ro.simavi.odysseus.platform.services.OdsWorkflowInstanceService;
import ro.simavi.odysseus.platform.util.ImageConversionJp2ToJpeg;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

@Setter
@Getter
@Component
@Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)
@RestController
public class OdsTravelerController {
    @Autowired
    private OdsTravelerService odsTravelerService;

    @Autowired
    private OdsTravelerRepository odsTravelerRepository;

    @Autowired
    private OdsDocumentRepository odsDocumentRepository;

    @Autowired
    private WorkflowInstanceController workflowInstanceController;

    @Autowired
    private OdsTravelRepository odsTravelRepository;

    @Autowired
    private OdsAuditLogService odsAuditLogService;

    @Autowired
    private OdsTransactionsRepository odsTransactionsRepository;

    @Autowired
    private OdsDocumentValidationRepository odsDocumentValidationRepository;

    private KeycloackRestCalls keycloackRestCalls;

    private ImageConversionJp2ToJpeg imageConversionJp2ToJpeg;


    private OdsTraveler odsTravelerSelected;

    private OdsTraveler odsTravelerSelectedForPreregister;

    private List<OdsDocument> odsDocumentListForTraveler;

    private List<OdsTraveler> travelerList;

    private List<OdsTraveler> odsTravelerListForBCP;

    private OdsTraveler odsTravelerSelectedForBCP;

    private byte[] imageFromDB;

    private byte[] pictureFromTravaler;

    private byte[] pictureFromTravalerBCP;

    @PostConstruct
    public void init(){
        odsTravelerSelected = new OdsTraveler();
        odsTravelerSelectedForPreregister = new OdsTraveler();
        odsTravelerSelectedForBCP = new OdsTraveler();
        travelerList = odsTravelerRepository.findAll();
        imageConversionJp2ToJpeg = new ImageConversionJp2ToJpeg();
    }


    public void settingOdsDocument() throws IOException {
        imageFromDB = null;
        odsDocumentListForTraveler = odsDocumentRepository.findAllByTravelerId(odsTravelerSelected.getId());
        FacesContext.getCurrentInstance().getExternalContext().redirect("verifyTraveler.xhtml");
    }

    public void settingImageFromDB(Integer documentId){
        if(!odsDocumentListForTraveler.isEmpty()){
            for(OdsDocument odsDocument : odsDocumentListForTraveler){
                if(odsDocument.getId() == documentId){
                    imageFromDB = odsDocument.getPicture();
                }

            }
        }
    }

    public StreamedContent getPicture() throws IOException {
        if(imageFromDB != null && imageConversionJp2ToJpeg.getImageContentType(imageFromDB).equals("image/jp2")){
            imageFromDB = imageConversionJp2ToJpeg.convertJP2ToJPEG(imageFromDB);
        }

        return DefaultStreamedContent.builder()
                .contentType("image/jpeg")
                .stream(() -> {
                    if (imageFromDB == null) {
                        return null;
                    }

                    try {
                        return new ByteArrayInputStream(imageFromDB);
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                        return null;
                    }
                })
                .build();
    }

    public void settingPictureFromTraveler(){
        pictureFromTravaler = odsTravelerSelected.getPicture();
    }


    public StreamedContent getImage() throws IOException {
        if(pictureFromTravaler != null && imageConversionJp2ToJpeg.getImageContentType(pictureFromTravaler).equals("image/jp2")){
            pictureFromTravaler = imageConversionJp2ToJpeg.convertJP2ToJPEG(pictureFromTravaler);
        }

        return DefaultStreamedContent.builder()
                .contentType("image/jpeg")
                .stream(() -> {
                    if (pictureFromTravaler == null) {
                        return null;
                    }

                    try {
                        return new ByteArrayInputStream(pictureFromTravaler);
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                        return null;
                    }
                })
                .build();
    }

    public void settingPictureFromTravelerBCP(){
        pictureFromTravalerBCP = odsTravelerSelectedForBCP.getPicture();
    }


    public StreamedContent getImageBCP() throws IOException {

        if(pictureFromTravalerBCP != null && imageConversionJp2ToJpeg.getImageContentType(pictureFromTravalerBCP).equals("image/jp2")){
            pictureFromTravalerBCP = imageConversionJp2ToJpeg.convertJP2ToJPEG(pictureFromTravalerBCP);
        }

        return DefaultStreamedContent.builder()
                .contentType("image/jpeg")
                .stream(() -> {
                    if (pictureFromTravalerBCP == null) {
                        return null;
                    }

                    try {
                        return new ByteArrayInputStream(pictureFromTravalerBCP);
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                        return null;
                    }
                })
                .build();
    }

    public void oncapture(CaptureEvent captureEvent) {
        odsTravelerSelectedForPreregister.setPicture(captureEvent.getData());
        FacesMessage msg =new FacesMessage("Successfully captured!");
        FacesContext.getCurrentInstance().addMessage(null, msg);

    }

    public String onFlowProcess(FlowEvent event) {
        // Implement logic for each step transition
        return event.getNewStep();
    }

    public void saveTraveler() throws IOException {
        odsTravelerSelectedForPreregister.setTuuid(UUID.randomUUID().toString());

        OdsTraveler odsTraveler = this.odsTravelerService.saveOrEditOdsTraveler(odsTravelerSelectedForPreregister);
        odsAuditLogService.startTravelerProcess(this.getClass().getName(), "saveTraveler", odsTraveler.getPersonalId(), odsTraveler.getEmail(), odsTraveler.getLastName());
        odsTravelerSelectedForPreregister = new OdsTraveler();
        travelerList = odsTravelerRepository.findAll();
        PrimeFaces.current().ajax().update("odsTravelerForm:dataTableOdsTraveler");
        FacesMessage msg =new FacesMessage("Successfully saved!");
        FacesContext.getCurrentInstance().addMessage(null, msg);
        FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
        FacesContext.getCurrentInstance().getExternalContext().redirect("preregister.xhtml");
        PrimeFaces.current().ajax().update("wizardForm:wizardId");

        if(Objects.nonNull(odsTraveler.getId())){
           workflowInstanceController.createWorkflowInstance("preregister", odsTraveler.getTuuid());
        }
        odsAuditLogService.endTravelerProcess(this.getClass().getName(), "saveTraveler", odsTraveler.getPersonalId(), odsTraveler.getTuuid(),odsTraveler.getEmail(), odsTraveler.getLastName());
    }

    public void saveOdsTraveler() {
        if(Objects.nonNull(odsTravelerSelected)) {
            odsTravelerSelected = odsTravelerService.saveOrEditOdsTraveler(odsTravelerSelected);
            travelerList = odsTravelerRepository.findAll();
            PrimeFaces.current().ajax().update("odsTravelerForm:dataTableOdsTraveler");
            FacesMessage msg = new FacesMessage("Successfully saved!");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
    }

    public void createNewOdsTraveler() {
        odsTravelerSelected = new OdsTraveler();
        odsTravelerSelected.setTuuid(UUID.randomUUID().toString());
    }

    public void deleteOdsTraveler() {
        if(Objects.nonNull(odsTravelerSelected)) {
            odsTravelerService.deleteOdsTraveler(odsTravelerSelected);
            travelerList.remove(odsTravelerSelected);
            odsTravelerSelected = null;
            PrimeFaces.current().ajax().update("odsTravelerForm:dataTableOdsTraveler");
            FacesMessage msg =new FacesMessage("Successfully deleted!");
            FacesContext.getCurrentInstance().addMessage(null, msg);

        }
    }
//    @Operation(summary = "The traveler preregister a traqvel. Data about travel and image is sent")
//    @PostMapping("/preregister")
//    public ResponseEntity<String> preregisterTraveler(@RequestParam("traveler") String traveler, @RequestParam("traveler_image") MultipartFile travelerImage) throws IOException {
//        ObjectMapper objectMapper = new ObjectMapper();
//        objectMapper.registerModule(new JavaTimeModule());
//        OdsTraveler odsTraveler = objectMapper.readValue(traveler, OdsTraveler.class);
//        odsTraveler.setPicture(travelerImage.getBytes());
//        odsTraveler.setTuuid(UUID.randomUUID().toString());
//        odsTravelerService.saveOrEditOdsTraveler(odsTraveler);
//        travelerList = odsTravelerRepository.findAll();
//        return new ResponseEntity<>(odsTraveler.getTuuid(), HttpStatus.OK);
//    }
//    @Operation(summary = "The traveler is selfchecking the identity by giving data and image")
//    @PostMapping("/selfCheckIdentity")
//    public IdentityVerificationResult checkIdentityTraveler(@RequestParam("traveler") OdsTraveler traveler, @RequestParam("traveler_image") MultipartFile travelerImage) throws IOException {
//        ObjectMapper objectMapper = new ObjectMapper();
//        objectMapper.registerModule(new JavaTimeModule());
//        travelerList = odsTravelerRepository.findAll((Example<OdsTraveler>) traveler);
//        // to do check identity here
//        IdentityVerificationResult ivr = new IdentityVerificationResult();
//        ivr.setOdsTraveler(traveler);
//        ivr.setPass(true);
//        ivr.setTimneOfControll(new Date());
//        ivr.setControlledBy("Identity controller");
//        ivr.setResult("Pass");
//        return ivr;
//    }

    @Operation(summary = "You send two dates in the follow format 'yyyy-MM-dd' and the method will return a list of travelers who have crossed the border between those dates.")
    @PostMapping("/travelersListWhoPassedBorder")
    public List<OdsTravelerDto> travelersListWhoPassedTheBorder(@RequestParam("start_date") String startDate, @RequestParam("end_date") String endDate){
        LocalDate startLocalDate = parseDate(startDate);
        LocalDate endLocalDate = parseDate(endDate);
        List<OdsTravel> odsTravelsList;
        List<OdsTravelerDto> odsTravelersDtoList = new ArrayList<>();
        if(startLocalDate != null && endLocalDate != null){
            odsAuditLogService.startTravelerProcess(this.getClass().getName(), "travelersListWhoPassedBorder", null);
            odsTravelsList = odsTravelRepository.findAllByPassingDatePlannedBetween(startLocalDate, endLocalDate);
            if(Objects.nonNull(odsTravelsList) && !odsTravelsList.isEmpty()){
                for(OdsTravel odsTravel : odsTravelsList){
                    if(Objects.nonNull(odsTravel.getPassengerId())){
                        OdsTraveler odsTraveler = odsTravelerRepository.findFirstByTuuid(odsTravel.getPassengerId());
                        if(Objects.nonNull(odsTraveler)) {
                            OdsTravelerDto odsTravelerDto = new OdsTravelerDto(odsTraveler.getFirstName(), odsTraveler.getLastName(), odsTraveler.getEmail(), odsTravel.getPassingDatePlanned(), odsTravel.getBcpPlanned(), odsTraveler.getRegisteredUser(), odsTraveler.getTuuid(), Base64.getEncoder().encodeToString(odsTraveler.getPicture()));
                            odsTravelersDtoList.add(odsTravelerDto);
                        }
                    }
                }
            }
        }
        odsAuditLogService.endTravelerProcess(this.getClass().getName(), "travelersListWhoPassedBorder", null, "No travelers :"+ odsTravelersDtoList.size());
        return odsTravelersDtoList;
    }

    @Operation(summary = "You send the date in the follow format 'yyyy-MM-dd' and the method will return a list of travelers who have planned to cross the border in that date.")
    @PostMapping("/travelersListWhoWillPassBorder")
    public List<OdsTravelerDto> travelersListWhoWillPassBorder(@RequestParam("date") String date){
        LocalDate startLocalDate = parseDate(date);

        List<OdsTravel> odsTravelsList;
        List<OdsTravelerDto> odsTravelersDtoList = new ArrayList<>();
        if(startLocalDate != null){
            odsAuditLogService.startTravelerProcess(this.getClass().getName(), "travelersListWhoPassedBorder", null);
            odsTravelsList = odsTravelRepository.findAllByPassingDatePlannedEquals(startLocalDate);
            if(Objects.nonNull(odsTravelsList) && !odsTravelsList.isEmpty()){
                for(OdsTravel odsTravel : odsTravelsList){
                    if(Objects.nonNull(odsTravel.getPassengerId())){
                        OdsTraveler odsTraveler = odsTravelerRepository.findFirstByTuuid(odsTravel.getPassengerId());
                        if(Objects.nonNull(odsTraveler)) {
                            OdsTravelerDto odsTravelerDto = new OdsTravelerDto(odsTraveler.getFirstName(), odsTraveler.getLastName(), odsTraveler.getEmail(), odsTravel.getPassingDatePlanned(), odsTravel.getBcpPlanned(), odsTraveler.getRegisteredUser(), odsTraveler.getTuuid(), Base64.getEncoder().encodeToString(odsTraveler.getPicture()));
                            odsTravelersDtoList.add(odsTravelerDto);
                        }
                    }
                }
            }
        }
        odsAuditLogService.endTravelerProcess(this.getClass().getName(), "travelersListWhoPassedBorder", null, "No travelers :"+ odsTravelersDtoList.size());
        return odsTravelersDtoList;
    }

    private LocalDate parseDate(String dateStr) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        try {
            LocalDate date = LocalDate.parse(dateStr, formatter);
            return date;
        } catch (DateTimeParseException e) {
            System.out.println("Invalid date format. Please use yyyy-MM-dd.");
            return null;
        }
    }

    public void createListOfTravelersForCrossingBorderToday() throws IOException {
        List<OdsTravel> odsTravelList = odsTravelRepository.findAllByPassingDatePlannedEquals(LocalDate.now());
        odsTravelerListForBCP = new ArrayList<>();
        if (Objects.nonNull(odsTravelList)) {
            for (OdsTravel odsTravel : odsTravelList) {
                if (Objects.nonNull(odsTravel.getPassengerId())) {
                    if (odsTravelerRepository.existsByTuuid(odsTravel.getPassengerId())) {
                        OdsTraveler odsTraveler = odsTravelerRepository.findByTuuid(odsTravel.getPassengerId());
                        odsTravelerListForBCP.add(odsTraveler);
                    }

                }
            }
        }
        String path = FacesContext.getCurrentInstance().getExternalContext().getRequest().toString();

        if(path.contains("pages")){
            FacesContext.getCurrentInstance().getExternalContext().redirect("checkIdentityForBCP.xhtml");
        }else {
            FacesContext.getCurrentInstance().getExternalContext().redirect("pages/checkIdentityForBCP.xhtml");
        }
    }

    public void verifyTravelerIdentityByBCP() {
        if (Objects.nonNull(odsTravelerSelectedForBCP)) {
            odsAuditLogService.startDocProcess(this.getClass().getName(), "verifyTravelerIdentityByBCP", odsTravelerSelectedForBCP.getDocumentNumber());
            String result = "";
            Random random = new Random();

            double randomValue = random.nextDouble();

            if (randomValue < 0.95)
                result = "Successfully";
            else
                result = "Error";

            if(result.equalsIgnoreCase("successfully")) {
                FacesMessage msg = new FacesMessage("Identity of traveler was successfully verified!");
                FacesContext.getCurrentInstance().addMessage(null, msg);
            }else {
                FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "Identity of traveler wasn't successfully verified!", "");
                FacesContext.getCurrentInstance().addMessage(null, msg);
            }
        }
    }


    @Operation(summary = "You send traveler image and the method will return the result of verification")
    @PostMapping(value = "/checkFaceVbox", consumes = { "multipart/form-data"})
    public CheckFaceVboxDTO checkFaceVbox(@RequestParam("traveler_image") MultipartFile travelerImage){
       CheckFaceVboxDTO checkFaceVboxDTO = new CheckFaceVboxDTO();
        // Populate language
        checkFaceVboxDTO.setLanguage("EN");

        // Populate Document
        CheckFaceVboxDTO.Document document = new CheckFaceVboxDTO.Document();
        document.setExpirationOn(LocalDateTime.of(2030, 12, 31, 23, 59));
        document.setGender("M");
        document.setDateOfBirth(LocalDateTime.of(1990, 1, 1, 0, 0));
        document.setGivenName("John");
        document.setSurname("Doe");
        document.setMrz("MRZ123456789");
        document.setElectronic("Yes");
        document.setIssuedOn("2020-01-01");
        document.setNationality("USA");
        document.setType("Passport");
        document.setTypeId("P");
        document.setTypeName("Personal Passport");
        document.setNumber("A12345678");
        document.setIssuerState("USA");
        checkFaceVboxDTO.setDocument(document);

        // Populate BoardingPass
        CheckFaceVboxDTO.BoardingPass boardingPass = new CheckFaceVboxDTO.BoardingPass();
        boardingPass.setFormatCode("M1");
        boardingPass.setType("Electronic");
        boardingPass.setNumberOfLegsEncoded(2);
        boardingPass.setPassengerName("John Doe");
        boardingPass.setElectronicTicketIndicator("Y");
        boardingPass.setRaw("RAW_DATA_STRING");
        boardingPass.setExpirationOn(LocalDateTime.of(2024, 12, 31, 23, 59));

        // Populate Legs
        List<CheckFaceVboxDTO.BoardingPass.Leg> legs = new ArrayList<>();
        CheckFaceVboxDTO.BoardingPass.Leg leg1 = new CheckFaceVboxDTO.BoardingPass.Leg();
        leg1.setFlightNumber("AA123");
        leg1.setFrequentFlyerNumber("FF123456");
        leg1.setDocumentSerialNumber("DSN123");
        leg1.setAirlineNumericCode("001");
        leg1.setPassengerStatus("Checked-In");
        leg1.setSequenceNumber("001");
        leg1.setSeatNumber("12A");
        leg1.setCompartmentCode("E");
        leg1.setFlightDate("2024-11-30");
        leg1.setLegOrder(1);
        leg1.setCarrierDesignator("AA");
        leg1.setDestinationAirport("JFK");
        leg1.setOriginAirport("LAX");
        leg1.setCarrierPnrCode("PNR123");
        leg1.setFrequentFlyerAirlineDesignator("AA");
        legs.add(leg1);
        boardingPass.setLegs(legs);
        checkFaceVboxDTO.setBoardingPass(boardingPass);

        // Populate Biometrics
        List<CheckFaceVboxDTO.Biometric> biometrics = new ArrayList<>();
        CheckFaceVboxDTO.Biometric biometric = new CheckFaceVboxDTO.Biometric();
        biometric.setPosition("Front");
        biometric.setType("Face");
        biometric.setSource("Camera");
        biometric.setImage("BASE64_IMAGE_STRING");
        biometric.setFormat("JPEG");
        biometrics.add(biometric);
        checkFaceVboxDTO.setBiometrics(biometrics);

        // Populate ValidationStatus
        CheckFaceVboxDTO.ValidationStatus validationStatus = new CheckFaceVboxDTO.ValidationStatus();
        validationStatus.setDocumentAuthenticated(true);
        validationStatus.setLivenessCheckPassed(true);
        validationStatus.setBiometryMatched(true);
        checkFaceVboxDTO.setValidationStatus(validationStatus);

        odsAuditLogService.endTravelerProcess(this.getClass().getName(), "checkFaceVbox", null, "Result :"+ checkFaceVboxDTO.getValidationStatus().isLivenessCheckPassed());
        return checkFaceVboxDTO;
    }

    @Operation(summary = "You send the traveler mrz and the method will create a transaction and returns the transaction UUID")
    @PostMapping(value = "/travelerIdentifyAfterMrz")
    public ResponseEntity<TransactionDTO> travelerIdentifyAfterMrz(@RequestBody MrzDTO mrzDTO) throws IOException {
        TransactionDTO transactionDTO = new TransactionDTO();
        DtcParser dtcParser = new DtcParser();
        if(Objects.nonNull(mrzDTO.getMrz())) {
            Dg1Data dg2Data = dtcParser.decodeMRZ(mrzDTO.getMrz());

            if(Objects.nonNull(dg2Data.getDocumentNumber()) && odsTravelerRepository.existsByDocumentNumber(dg2Data.getDocumentNumber())) {
                Transaction transaction = new Transaction();
                transaction.setTransactionStatus("ONGOING");
                transaction.setTransactionUuid(UUID.randomUUID().toString());
                OdsTraveler odsTraveler = odsTravelerRepository.findLastByDocumentNumberNative(dg2Data.getDocumentNumber());
                transaction.setTravelerId(odsTraveler.getId());

                odsTransactionsRepository.save(transaction);

                transactionDTO.setTransactionUuid(transaction.getTransactionUuid());

                OdsDocumentValidation odsDocumentValidation = new OdsDocumentValidation();
                if (Objects.nonNull(odsTraveler.getDtcVC())) {
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
                    if (Objects.nonNull(dg1Data.getDocumentType()) && dg1Data.getDocumentType().substring(0, 1).equalsIgnoreCase("p"))
                        odsDocumentValidation.setVcTypeName("Passport");
                    else if (Objects.nonNull(dg1Data.getDocumentType()) && dg1Data.getDocumentType().substring(0, 1).equalsIgnoreCase("i")) {
                        odsDocumentValidation.setVcTypeName("ID");
                    }
                    odsDocumentValidation.setVcNumber(dg1Data.getDocumentNumber());
                    //odsDocumentValidation.setVcIssuerState();
                    //populate with transaction data
                    odsDocumentValidation.setTransactionStatus(transaction.getTransactionStatus());
                    odsDocumentValidation.setTravelerId(String.valueOf(transaction.getTravelerId()));
                    odsDocumentValidation.setTransactionUuid(transaction.getTransactionUuid());


                    odsDocumentValidationRepository.save(odsDocumentValidation);
                }
            }
        }

        return ResponseEntity.ok(transactionDTO);
    }

    private Dg1Data verifyDTCVC(String dtcvc) throws IOException {
        byte[] dtc_vc = Base64.getDecoder().decode(dtcvc);
        DtcParser dtcParser = new DtcParser();
        Dg1Data dg1Data = dtcParser.decode(dtc_vc);

        return dg1Data;
    }

}
