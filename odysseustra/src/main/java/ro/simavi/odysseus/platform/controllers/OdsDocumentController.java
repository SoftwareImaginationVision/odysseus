package ro.simavi.odysseus.platform.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.swagger.v3.oas.annotations.Operation;
import lombok.Getter;
import lombok.Setter;
import org.kie.api.runtime.KieSession;
import org.primefaces.PrimeFaces;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.file.UploadedFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.multipart.MultipartFile;
import ro.simavi.odysseus.platform.entities.*;
import ro.simavi.odysseus.platform.repositories.OdsDocumentRepository;
import ro.simavi.odysseus.platform.repositories.OdsTravelerRepository;
import ro.simavi.odysseus.platform.repositories.OdsWorkflowInstanceNodeRepository;
import ro.simavi.odysseus.platform.repositories.OdsWorkflowInstanceRepository;
import ro.simavi.odysseus.platform.services.OdsDocumentService;
import ro.simavi.odysseus.platform.services.UserOidcService;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.*;

@Setter
@Getter
@Component
@Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)
@RestController
public class OdsDocumentController {
    @Autowired
    private OdsDocumentRepository odsDocumentRepository;

    @Autowired
    private OdsDocumentService odsDocumentService;

    @Autowired
    private OdsTravelerRepository odsTravelerRepository;

    @Autowired
    private WorkflowInstanceController workflowInstanceController;

    @Autowired
    private OdsWorkflowInstanceRepository odsWorkflowInstanceRepository;

    @Autowired
    private OdsWorkflowInstanceNodeRepository odsWorkflowInstanceNodeRepository;

    @Value("${verifyDocumentLink}")
    private String verifyDocumentLink;

    @Value("${resourceTokenUrl}")
    private String resourceTokenUrl;

    @Value("${clientId}")
    private String clientId;

    @Value("${username}")
    private String username;

    @Value("${password}")
    private String password;

    @Autowired
    private KieSession session;

    @Autowired
    private UserOidcService userOidcService;

    @Autowired
    private OdsTravelerRepository travelerRepository;

    private List<OdsDocument> documentList;

    private List<OdsDocument> documentRiskList;

    private OdsDocument odsDocumentSelected;

    private OdsDocument odsDocumentRiskSelected;

    private OdsDocument odsDocumentByTravelId;

    private OdsDocument odsDocumentSelectedForVerify;

    private String nameFilter;

    private String nameFilterForDocumentRisk;
    private Integer travelerId;

    private byte[] imageDB;

    private UploadedFile originalImage;

    @PostConstruct
    public void init() {
        String currentUsername = getAuthenticatedOidcUsername();
        if (currentUsername != null) {
            OdsTraveler traveler = odsTravelerRepository.findByRegisteredUser(currentUsername);
            if (traveler != null) {
                documentList = odsDocumentRepository.findAllByTravelerId(traveler.getId());
                travelerId = traveler.getId();
            }
        }
        if (documentList == null) {
            documentList = new ArrayList<>();
        }

    }


    private String getAuthenticatedOidcUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof OidcUser) {
            OidcUser oidcUser = (OidcUser) authentication.getPrincipal();
            return oidcUser.getEmail();
        }
        return null;
    }

    public void onPageLoad() {
        documentRiskList = odsDocumentRepository.findAllByHasRisk("true");
        if (odsDocumentRepository.findAll().size() != documentList.size())
            documentList = odsDocumentRepository.findAll();
    }

    public void saveOdsDocument() {
        if (Objects.nonNull(odsDocumentSelected)) {
            if (Objects.nonNull(originalImage))
                odsDocumentSelected.setPicture(originalImage.getContent());
            odsDocumentSelected.setTravelerId(travelerId);
            odsDocumentSelected = odsDocumentService.saveOrEditOdsDocument(odsDocumentSelected);
            documentList = odsDocumentRepository.findAllByTravelerId(travelerId);
            PrimeFaces.current().ajax().update("odsDocumentForm:dataTableOdsDocument");
            FacesMessage msg = new FacesMessage("Succesfully saved!");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            this.originalImage = null;
        }
    }

    public void settingDocument(Integer documentId) {
        if (!documentList.isEmpty()) {
            for (OdsDocument odsDocument : documentList) {
                if (Objects.equals(odsDocument.getId(), documentId))
                    imageDB = odsDocument.getPicture();
            }

        }

    }

    public StreamedContent getImageFromDB() {
        return DefaultStreamedContent.builder()
                .contentType("image/jpeg")
                .stream(() -> {
                    if (imageDB == null) {
                        return null;
                    }

                    try {
                        return new ByteArrayInputStream(imageDB);
                    } catch (Exception e) {
                        e.printStackTrace();
                        return null;
                    }
                })
                .build();
    }


    public void cancelOdsDocument() {
        this.originalImage = null;
    }

    public void handleFileUpload(FileUploadEvent event) {
        this.originalImage = null;
        UploadedFile file = event.getFile();
        if (file != null && file.getContent() != null && file.getContent().length > 0 && file.getFileName() != null) {
            this.originalImage = file;
            FacesMessage msg = new FacesMessage("Successful", this.originalImage.getFileName() + " is uploaded.");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }

    }


    public StreamedContent getImage() {
        return DefaultStreamedContent.builder()
                .contentType(originalImage == null ? null : originalImage.getContentType())
                .stream(() -> {
                    if (originalImage == null
                            || originalImage.getContent() == null
                            || originalImage.getContent().length == 0) {
                        return null;
                    }

                    try {
                        return new ByteArrayInputStream(originalImage.getContent());
                    } catch (Exception e) {
                        e.printStackTrace();
                        return null;
                    }
                })
                .build();
    }

    public void deleteOdsDocument() {
        if (Objects.nonNull(odsDocumentSelected)) {
            odsDocumentService.deleteOdsDocument(odsDocumentSelected);
            documentList.remove(odsDocumentSelected);
            odsDocumentSelected = null;
            PrimeFaces.current().ajax().update("odsDocumentForm:dataTableOdsDocument");
            FacesMessage msg = new FacesMessage("Successfully deleted!");
            FacesContext.getCurrentInstance().addMessage(null, msg);

        }
    }


    public void createNewOdsDocument() {
        this.odsDocumentSelected = new OdsDocument();
        this.originalImage = null;
    }

    public void filter() {
        if (nameFilter != null) {
            documentList = odsDocumentService.findOdsDocumentsByGivenNameContainsIgnoreCaseOrSurnameContainsIgnoreCase(nameFilter, nameFilter);
        } else {
            documentList = odsDocumentRepository.findAll();
        }
    }

    public void reset() {
        documentList = odsDocumentRepository.findAll();
        nameFilter = null;
    }

    public void filterDocumentRisk() {
        if (nameFilterForDocumentRisk != null) {
            documentRiskList = odsDocumentRepository.findOdsDocumentsByGivenNameContainsIgnoreCaseOrSurnameContainsIgnoreCaseAndHasRisk(nameFilterForDocumentRisk, nameFilterForDocumentRisk, "true");
        } else {
            documentRiskList = odsDocumentRepository.findAllByHasRisk("true");
        }
    }

    public void resetDocumentRisk() {
        documentRiskList = odsDocumentRepository.findAllByHasRisk("true");
        nameFilterForDocumentRisk = null;
    }
//    @Operation(summary = "Upload a VDP in the system (image and aslo data)")
//    @PostMapping("/uploadVDP")
//    public void uploadDocument(@RequestParam("document") String document, @RequestParam("document_image") MultipartFile documentImage, @RequestParam("tuuid") String tuuid) throws IOException {
//        ObjectMapper objectMapper = new ObjectMapper();
//        objectMapper.registerModule(new JavaTimeModule());
//        OdsDocument odsDocument = objectMapper.readValue(document, OdsDocument.class);
//        odsDocument.setPicture(documentImage.getBytes());
//        odsDocument.setTravelerId(odsTravelerRepository.findFirstByTuuid(tuuid).getId());
//        odsDocumentService.saveOrEditOdsDocument(odsDocument);
//        documentList = odsDocumentRepository.findAll();
//    }

    public void verifyDocument() {
        if (odsDocumentSelectedForVerify != null) {
            String statusVerification = startDocumentVerificationTemp(odsDocumentSelectedForVerify);
            if (statusVerification.equalsIgnoreCase("Successfully")) {
                FacesMessage msg = new FacesMessage("You successfully verified your document!");
                FacesContext.getCurrentInstance().addMessage(null, msg);
                OdsWorkflowInstance odsWorkflowInstanceActive = odsWorkflowInstanceRepository.findFirstByInstanceNameAndWorkflowStatus(userOidcService.getCurrentUserEmail(), "Active");
                if (Objects.nonNull(odsWorkflowInstanceActive)) {
                    odsWorkflowInstanceActive.setResultWorkflow("pass");
                    odsWorkflowInstanceRepository.save(odsWorkflowInstanceActive);
                }
                workflowInstanceController.createWorkflowInstance("verifyVDP", odsTravelerRepository.getById(odsDocumentSelectedForVerify.getTravelerId()).getTuuid());
            } else if (statusVerification.equalsIgnoreCase("Error")) {
                FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "You couldn`t verified your document!", "");
                FacesContext.getCurrentInstance().addMessage(null, msg);
            } else if (statusVerification.equalsIgnoreCase("Risk")) {
                odsDocumentSelectedForVerify.setHasRisk("true");
                odsDocumentService.saveOrEditOdsDocument(odsDocumentSelectedForVerify);
                FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "Wait for a officer to verify your document!", "");
                FacesContext.getCurrentInstance().addMessage(null, msg);
            }
        }
    }

    public String startDocumentVerificationTemp(OdsDocument document) {
        session.insert(document);
        int ruleFiredCount = session.fireAllRules();

        if (ruleFiredCount != 0)
            return "Risk";


        Random random = new Random();

        double randomValue = random.nextDouble();

        if (randomValue < 0.95)
            return "Successfully";
        else
            return "Error";
    }


    @Operation(summary = "The traveler is requesting a self check on document")
    @PostMapping("/selfCheckTravelerDocument")
    public ResponseEntity<String> checkDocumentForTraveler(@RequestParam("document") String document) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        OdsDocument odsDocument = objectMapper.readValue(document, OdsDocument.class);

        session.insert(odsDocument);
        int ruleFiredCount = session.fireAllRules();

        if (ruleFiredCount != 0)
            return ResponseEntity.ok("Risk");


        Random random = new Random();

        double randomValue = random.nextDouble();

        if (randomValue < 0.95)
            return ResponseEntity.ok("Successfully");
        else
            return ResponseEntity.ok("Error");
    }
    @Operation(summary = "Get the result of a self check on the document given in the body of the request")
    @PostMapping("/selfCheckTravelerDocumentResult")
    public VerificationResult getTravelerCheckDocumentResult(@RequestParam("document") String document) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        OdsDocument odsDocument = objectMapper.readValue(document, OdsDocument.class);

        session.insert(odsDocument);
        int ruleFiredCount = session.fireAllRules();

        VerificationResult vr = new VerificationResult();
        vr.setOdsDocument(odsDocument);
        vr.setPass(true);
        vr.setResult("Passed");
        vr.setTimneOfControll(new Date());
        vr.setControlledBy("DocumentController");

        Random random = new Random();

        double randomValue = random.nextDouble();

        if (randomValue < 0.95)
            vr.setPass(true);
        else
            vr.setPass(false);
    return vr;
}

    public void verifyDocumentByOfficerPass(){
        odsDocumentRiskSelected.setHasRisk("false");
        odsDocumentService.saveOrEditOdsDocument(odsDocumentRiskSelected);
        OdsWorkflowInstance odsWorkflowInstanceActive = odsWorkflowInstanceRepository.findFirstByInstanceNameAndWorkflowStatus(userOidcService.getCurrentUserEmail(),"Active");
        if(Objects.nonNull(odsWorkflowInstanceActive)) {
            odsWorkflowInstanceActive.setResultWorkflow("pass");
            odsWorkflowInstanceRepository.save(odsWorkflowInstanceActive);
        }
        if(Objects.nonNull(odsDocumentRiskSelected.getTravelerId()))
            workflowInstanceController.createWorkflowInstance("verifyVDP", odsTravelerRepository.getById(odsDocumentRiskSelected.getTravelerId()).getTuuid());
        else
            workflowInstanceController.createWorkflowInstance("verifyVDP", null);
    }

    public void verifyDocumentByOfficerReject(){
        odsDocumentRiskSelected.setHasRisk(null);
        odsDocumentService.saveOrEditOdsDocument(odsDocumentRiskSelected);
        OdsWorkflowInstance odsWorkflowInstanceActive = odsWorkflowInstanceRepository.findFirstByInstanceNameAndWorkflowStatus(userOidcService.getCurrentUserEmail(),"Active");
        if(Objects.nonNull(odsWorkflowInstanceActive)) {
            odsWorkflowInstanceActive.setResultWorkflow("reject");
            odsWorkflowInstanceRepository.save(odsWorkflowInstanceActive);
        }
        if(Objects.nonNull(odsDocumentRiskSelected.getTravelerId()))
            workflowInstanceController.createWorkflowInstance("verifyVDP", odsTravelerRepository.getById(odsDocumentRiskSelected.getTravelerId()).getTuuid());
        else {
            workflowInstanceController.createWorkflowInstance("verifyVDP", null);
            OdsWorkflowInstance odsWorkflowInstanceActive1 = odsWorkflowInstanceRepository.findFirstByInstanceNameAndWorkflowStatus(userOidcService.getCurrentUserEmail(),"Active");
            if(Objects.nonNull(odsWorkflowInstanceActive1)) {
                odsWorkflowInstanceActive1.setResultWorkflow("reject");
                odsWorkflowInstanceActive1.setWorkflowStatus("Inactive");
                odsWorkflowInstanceRepository.save(odsWorkflowInstanceActive1);
                OdsWorkflowInstanceNode odsWorkflowInstanceNode = odsWorkflowInstanceNodeRepository.findFirstByWorkflowInstanceIdAndNodeStatusEqualsIgnoreCase(odsWorkflowInstanceActive1.getId(), "Active");
                odsWorkflowInstanceNode.setNodeStatus("Inactive");
                odsWorkflowInstanceNodeRepository.save(odsWorkflowInstanceNode);
            }
        }
    }


}
