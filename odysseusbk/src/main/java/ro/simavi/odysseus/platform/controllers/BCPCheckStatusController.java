package ro.simavi.odysseus.platform.controllers;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ro.simavi.odysseus.platform.entities.*;
import ro.simavi.odysseus.platform.services.OdsAuditLogService;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Logger;
//import io.swagger.annotations.Api;
//import io.swagger.annotations.ApiOperation;
//import io.swagger.annotations.ApiParam;

@RestController

public class BCPCheckStatusController {

    @Autowired
    OdsAuditLogService odsAuditLogService;

    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();

    private MessageSource messageSource;

    //@Value("${keycloak.resource.token}")
    String resourceTokenURL;
    //@ApiOperation(value = "Get the number of preregistered travelers at the border queue")
    @Operation(summary = "Get the number of preregistered travelers at the border queue")
    @GetMapping("/getTravelersPreregistered")
    public Long getTravelersPreregistered() {

        odsAuditLogService.startTravelerProcess(this.getClass().getName(),
                "getTravelersPreregistered",null
                );
        // TO DO
        odsAuditLogService.endTravelerProcess(this.getClass().getName(),
                "getTravelersPreregistered",null, "COMPLETED"
        );
        return 1L;
    }

    @Operation(summary = "Get the result of the verification for the current document")
    @GetMapping("/getCurrentDocumentVerified")
    public VerificationResult getCurrentDocument() {
        odsAuditLogService.startDocProcess(this.getClass().getName(), "getCurrentDocument", null);
        // TO DO
        VerificationResult vr= new VerificationResult();
        vr.setOdsDocument(null);
        vr.setPass(true);
        vr.setResult("Passed");
        vr.setTimneOfControll(new Date());
        vr.setControlledBy("DocumentController");
        odsAuditLogService.endDocProcess(this.getClass().getName(), "getCurrentDocument", null, vr.getResult());
        return vr;
    }
    @Operation(summary = "Get the document status of the document entered as body in the request")
    @PostMapping("/getDocumentStatus")
    public VerificationResult getDocumentStatus(@RequestParam("document") OdsDocument document) {
        if(document==null)
            return null;
        odsAuditLogService.startDocProcess(this.getClass().getName(), "getDocumentStatus", document.getDocNumber());
        // TO DO
        VerificationResult vr= new VerificationResult();
        vr.setOdsDocument(document);
        vr.setPass(true);
        vr.setResult("Passed");
        vr.setTimneOfControll(new Date());
        vr.setControlledBy("DocumentController");
        odsAuditLogService.endDocProcess(this.getClass().getName(), "getDocumentStatus", document.getDocNumber(), vr.getResult());
        return vr;
    }

    @Operation(summary = "Get the result of the verification for the identity of the taverelers specified in trhe body of the request")
    @PostMapping("/getIdentityStatus")
    public IdentityVerificationResult getIdentityStatus(@RequestParam("traveler") OdsTraveler traveler) {
        odsAuditLogService.startTravelerProcess(this.getClass().getName(), "getIdentityStatus", traveler.getPersonalId());
        // TO DO
        IdentityVerificationResult ivr = new IdentityVerificationResult();
        ivr.setOdsTraveler(traveler);
        ivr.setPass(true);
        ivr.setTimneOfControll(new Date());
        ivr.setControlledBy("Identity controller");
        ivr.setResult("Pass");
        odsAuditLogService.endTravelerProcess(this.getClass().getName(), "getIdentityStatus", traveler.getPersonalId(), ivr.getResult());
        return ivr;
    }

    @GetMapping("/currentIdentityVerification")
    @Operation(summary = "Get the result of the verification for the identity of the current selected traveler")
    public IdentityVerificationResult getCurrentIdentityChecked() {
        odsAuditLogService.startTravelerProcess(this.getClass().getName(), "getCurrentIdentityChecked", null);
        // TO DO
        IdentityVerificationResult ivr = new IdentityVerificationResult();
        ivr.setOdsTraveler(null);
        ivr.setPass(true);
        ivr.setTimneOfControll(new Date());
        ivr.setControlledBy("Identity controller");
        ivr.setResult("Pass");
        odsAuditLogService.endTravelerProcess(this.getClass().getName(), "getCurrentIdentityChecked", null, ivr.getResult());
        return ivr;
    }


    private String currentDate(String language) {
        Date dd = new Date();
        DateFormat dateFormat = null;
        if (language == null) {
            dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        } else {
            byte var5 = -1;
            switch (language.hashCode()) {
                case 3241:
                    if (language.equals("en")) {
                        var5 = 0;
                    }
                    break;
                case 3645:
                    if (language.equals("ro")) {
                        var5 = 2;
                    }
                    break;
                case 3742:
                    if (language.equals("us")) {
                        var5 = 1;
                    }
            }

            switch (var5) {
                case 0:
                case 1:
                    dateFormat = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
                    break;
                case 2:
                    dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                    break;
                default:
                    dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            }
        }

        String strDate = dateFormat.format(dd);
        return strDate;
    }

}
