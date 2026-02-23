package ro.simavi.odysseus.platform.controllers;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.multipart.MultipartFile;
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
import java.util.Random;


@Setter
@Getter
@Component
@Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)
@RestController
public class CheckIdentityController {
    @Autowired
    private WorkflowInstanceController workflowInstanceController;

    @Autowired
    OdsAuditLogService odsAuditLogService;

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

    private String filename;
    private String nameForImage;
    private byte[] imageContent;

    boolean check;

    public void onPageLoad(){
        if(check)
            filename = null;
    }

    public void oncapture(CaptureEvent captureEvent) {
        filename = "photoCam";
        imageContent = captureEvent.getData();
        check = false;
        String currentUser=null;
        try{
            currentUser = FacesContext.getCurrentInstance().getExternalContext().getRemoteUser();
        }
        catch(Exception ex){

        }
        String res="In progress";
        odsAuditLogService.startTravelerProcess(this.getClass().getName(), "capture", currentUser);
        String statusVerification = startIdentityVerificationTemp(imageContent);
        if(statusVerification.equalsIgnoreCase("Successfully")){
            FacesMessage msg =new FacesMessage("You successfully verified your identity!");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            workflowInstanceController.createWorkflowInstance("checkIdentity", null);
            res="Recognized";
        } else if (statusVerification.equalsIgnoreCase("Error")) {
            FacesMessage msg =new FacesMessage(FacesMessage.SEVERITY_ERROR,"You couldn`t verified your identity!","");
            res="Not Recognized";
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
        odsAuditLogService.endTravelerProcess(this.getClass().getName(), "capture", currentUser, res);
    }

    public String startIdentityVerificationTemp(byte[] image){
        Random random = new Random();

        double randomValue = random.nextDouble();

        if(randomValue < 0.95)
            return "Successfully";
        else
            return "Error";
    }

    public String startIdentityVerification(byte[] image) {
        String apiUrl = verifyIdentityLink;

        // Create a RestTemplate instance
        RestTemplate restTemplate = new RestTemplate();

        // Create headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        // Create the request body
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("image", image);

        // Create the HTTP entity with headers and body
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        // Send POST request
        ResponseEntity<String> responseEntity = restTemplate.exchange(apiUrl, HttpMethod.POST, requestEntity, String.class);

        // Print response
        System.out.println("API Check Identity Response: " + responseEntity.getBody());
        return responseEntity.getBody();
    }


    @Operation(summary = "BCP is checking the identityb by entering the image")
    @PostMapping("/checkIdentityByBCP")
    public ResponseEntity<String> checkIdentityTraveler(@RequestParam("image") MultipartFile image, @RequestParam("tuuid") String uuid) throws IOException {
        System.out.println(uuid);
        odsAuditLogService.startTravelerProcess(this.getClass().getName(), "checkIdentityByBCP", uuid);
        System.out.println(image.getOriginalFilename());
        odsAuditLogService.endTravelerProcess(this.getClass().getName(), "checkIdentityByBCP", uuid, image.getOriginalFilename());
        return ResponseEntity.ok("Successfully");
    }



    @Operation(summary = "BCP is searchin 1:n the identity by entering the image")
    @PostMapping("/searchIdentityTravelerByBCP")
    public ResponseEntity<String> checkIdentityForTraveler(@RequestParam("image") byte[] image) throws IOException {
        Random random = new Random();
        odsAuditLogService.startTravelerProcess(this.getClass().getName(), "searchIdentityTravelerByBCP", null);
        double randomValue = random.nextDouble();
        odsAuditLogService.endTravelerProcess(this.getClass().getName(), "checkIdentityByBCP", null, "Result: "+randomValue);
        if(randomValue < 0.95)
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
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                        return null;
                    }
                })
                .build();
    }
}
