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
import org.springframework.web.bind.annotation.*;
import ro.simavi.odysseus.platform.entities.Greeting;

@RestController
public class GreetingController {

    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();

    private MessageSource messageSource;

    @GetMapping("/isalive")
    public Greeting greeting(@RequestParam(value = "name", defaultValue = "World") String name) {
        return new Greeting(counter.incrementAndGet(), String.format(template, name));
    }

    @GetMapping(
            value = {"/getJwt"},
            produces = {"application/json", "application/xml"}
    )
    @ResponseBody
    public ResponseEntity<Object> getJwt(@RequestParam(name = "name", required = false, defaultValue = "ODYSSEUS") String name,
                                         @RequestHeader(name = "Accept-Language", required = false, defaultValue = "en") Locale locale) {
        // Return a mock JWT response without requiring authentication
        LinkedHashMap<String, Object> responseMap = new LinkedHashMap<>();
        responseMap.put("access_token", "mockAccessToken"); // Placeholder token
        responseMap.put("name", name);
        responseMap.put("message", "Token generated successfully without authentication");

        Logger.getLogger(this.getClass().getName()).info("Generated mock token for: " + name);

        return ResponseEntity.ok(responseMap);
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
