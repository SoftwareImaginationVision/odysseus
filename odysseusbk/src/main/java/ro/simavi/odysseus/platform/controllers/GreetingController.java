package ro.simavi.odysseus.platform.controllers;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Logger;

import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ro.simavi.odysseus.platform.entities.Greeting;

@RestController

public class GreetingController {

    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();

    private MessageSource messageSource;

    //@Value("${keycloak.resource.token}")
    String resourceTokenURL;


    @GetMapping("/isalive")
    @PreAuthorize("hasAuthority('SCOPE_read_access')")
    public Greeting greeting(@RequestParam(value = "name", defaultValue = "World") String name) {
        return new Greeting(counter.incrementAndGet(), String.format(template, name));
    }

    @GetMapping(
            value = {"/getJwt"},
            produces = {"application/json", "application/xml"}
    )
    @ResponseBody
    public ResponseEntity<Object> getJwt(@RequestParam(name = "name", required = false, defaultValue = "ODYSSEUS") String name,
                                         @RequestHeader(name = "Accept-Language", required = false, defaultValue = "en") Locale locale,
                                         @RequestHeader(name = "client_id", required = false, defaultValue = "home-app") String clientId,
                                         @RequestHeader(name = "username", required = false, defaultValue = "simavi") String userName,
                                         @RequestHeader(name = "password", required = false) String pwd,
                                         @RequestHeader HttpHeaders headers) {
        this.messageSource.getMessage("is.user.jwt", (Object[]) null, locale);
        ResponseEntity<Object> reo = null;
        try {
            KeycloackRestCalls keycloackRestCalls = new KeycloackRestCalls();
            reo = keycloackRestCalls.getToken(this.resourceTokenURL, clientId, userName, pwd);
            Logger.getLogger(this.getClass().getName()).info("Url Token " +resourceTokenURL + " client id " + clientId);
            if (reo == null) {
                String ss1 = this.messageSource.getMessage("nok.user.authorized", (Object[]) null, locale);
                String cd = this.currentDate(locale == null ? "en" : locale.getLanguage());
                String ssf = String.format(ss1, userName)+cd;

                Logger.getLogger(this.getClass().getName()).warning(ssf);
                return null;
            }
        }
        catch(Exception ex1){
            Logger.getLogger(this.getClass().getName()).warning(ex1.getMessage());
            ex1.printStackTrace();
        }
        try {
            LinkedHashMap<String, Object> map = (LinkedHashMap) reo.getBody();
            String accessTokenString = null;
            if (map != null) {
                Logger.getLogger(this.getClass().getName()).info("Map not null");

                accessTokenString = (String) map.get("access_token");

                Logger.getLogger(this.getClass().getName()).info(accessTokenString);

                return reo;
            } else {
                String ss1 = this.messageSource.getMessage("nok.user.authorized", (Object[]) null, locale);
                String cd = this.currentDate(locale == null ? "en" : locale.getLanguage());
                String ssf = String.format(ss1, userName);
                 Logger.getLogger(this.getClass().getName()).warning(ssf);
                return null;
            }
        }
        catch(Exception ex1){
            Logger.getLogger(this.getClass().getName()).warning(ex1.getMessage());
            ex1.printStackTrace();
        }
        Logger.getLogger(this.getClass().getName()).warning("Null return");
        return null;
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
