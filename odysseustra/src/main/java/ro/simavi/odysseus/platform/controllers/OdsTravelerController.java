package ro.simavi.odysseus.platform.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.multipart.MultipartFile;
import ro.simavi.odysseus.platform.entities.*;
import ro.simavi.odysseus.platform.repositories.*;
import ro.simavi.odysseus.platform.services.*;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
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
    private UserOidcService userOidcService;

    @Autowired
    private OdsAuditLogService odsAuditLogService;

    private OdsTraveler odsTravelerSelected;

    private OdsTraveler odsTravelerSelectedForPreregister;

    private List<OdsDocument> odsDocumentListForTraveler;

    private List<OdsTraveler> travelerList;

    private byte[] imageFromDB;

    private byte[] pictureFromTravaler;

    @PostConstruct
    public void init(){
        odsTravelerSelected = new OdsTraveler();
        odsTravelerSelectedForPreregister = new OdsTraveler();
        travelerList = odsTravelerRepository.findAll();
        if(travelerList.isEmpty()) {
            odsTravelerRepository.save(new OdsTraveler(
                    "John", // firstName
                    "Doe", // lastName
                    LocalDate.of(1990, 5, 15), // dateOfBirth
                    "City", // placeOfBirth
                    "123 Main Street", // street
                    "", // streetDetails
                    "Metropolis", // city
                    "County", // county
                    "US", // countryCode
                    "12345", // zipCode
                    "john.doe@example.com", // email
                    "123-456-7890", // phone
                    "", // socialMedia
                    "Additional info", // additionalInfo
                    "Passport", // documentType
                    "ABC123", // documentNumber
                    "US Government", // documentIssuer
                    "2024-03-06", // documentValidity (în format string)
                    "1234567890" // personalId
            ));
            odsTravelerRepository.save(new OdsTraveler(
                    "Alice", // firstName
                    "Smith", // lastName
                    LocalDate.of(1985, 8, 20), // dateOfBirth
                    "Townsville", // placeOfBirth
                    "456 Elm Street", // street
                    "Apt 2B", // streetDetails
                    "Springfield", // city
                    "Countyshire", // county
                    "UK", // countryCode
                    "54321", // zipCode
                    "alice.smith@example.com", // email
                    "987-654-3210", // phone
                    "@alice_smith", // socialMedia
                    "No additional info", // additionalInfo
                    "Driver's License", // documentType
                    "XYZ789", // documentNumber
                    "UK Department of Transportation", // documentIssuer
                    "2025-02-28", // documentValidity (în format string)
                    "0987654321" // personalId
            ));
            travelerList = odsTravelerRepository.findAll();
        }

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

    public StreamedContent getPicture() {
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


    public StreamedContent getImage() {
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

    @Operation(summary = "The traveler preregister a travel. Data about travel and image is sent")
    @PostMapping("/preregister")
    public ResponseEntity<String> preregisterTraveler(@RequestParam("traveler") String traveler, @RequestParam("traveler_image") MultipartFile travelerImage) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        OdsTraveler odsTraveler = objectMapper.readValue(traveler, OdsTraveler.class);
        odsAuditLogService.startTravelProcess(this.getClass().getName(), "preregister", odsTraveler.getPersonalId());
        odsTraveler.setPicture(travelerImage.getBytes());
        odsTraveler.setTuuid(UUID.randomUUID().toString());
        odsTravelerService.saveOrEditOdsTraveler(odsTraveler);
        travelerList = odsTravelerRepository.findAll();
        odsAuditLogService.endTravelProcess(this.getClass().getName(), "preregister", odsTraveler.getPersonalId(), odsTraveler.getEmail(),odsTraveler.getLastName(), odsTraveler.getTuuid());
        return new ResponseEntity<>(odsTraveler.getTuuid(), HttpStatus.OK);
    }
    @Operation(summary = "The traveler is selfchecking the identity by giving data and image")
    @PostMapping("/selfCheckIdentity")
    public IdentityVerificationResult checkIdentityTraveler(@RequestParam("traveler") OdsTraveler traveler, @RequestParam("traveler_image") MultipartFile travelerImage) throws IOException {
        odsAuditLogService.startTravelProcess(this.getClass().getName(), "selfCheckIdentity", traveler.getPersonalId());
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        travelerList = odsTravelerRepository.findAll((Example<OdsTraveler>) traveler);
        // to do check identity here
        IdentityVerificationResult ivr = new IdentityVerificationResult();
        ivr.setOdsTraveler(traveler);
        ivr.setPass(true);
        ivr.setTimneOfControll(new Date());
        ivr.setControlledBy("Identity controller");
        ivr.setResult("Pass");
        odsAuditLogService.endTravelProcess(this.getClass().getName(), "selfCheckIdentity", traveler.getPersonalId(), ivr.getResult());
        return ivr;
    }

    public void isAlreadyPreregistered() throws IOException {
        if(odsTravelerRepository.existsByRegisteredUser(userOidcService.getCurrentUserEmail())){
            odsTravelerSelectedForPreregister = odsTravelerRepository.findFirstByRegisteredUser(userOidcService.getCurrentUserEmail());
            PrimeFaces.current().ajax().update("wizardForm:wizardId");
        }else{
            odsTravelerSelectedForPreregister = new OdsTraveler();
        }

        String path = FacesContext.getCurrentInstance().getExternalContext().getRequest().toString();

        if(path.contains("pages")){
            FacesContext.getCurrentInstance().getExternalContext().redirect("preregister.xhtml");
        }else {
            FacesContext.getCurrentInstance().getExternalContext().redirect("pages/preregister.xhtml");
        }
    }
}
