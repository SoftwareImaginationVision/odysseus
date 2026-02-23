package ro.simavi.odysseus.platform.controllers;

import org.keycloak.KeycloakPrincipal;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.keycloak.representations.IDToken;
import org.springframework.http.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.security.Principal;
import java.util.LinkedHashMap;

public class KeycloackRestCalls {
    public ResponseEntity<Object> getToken(String resourceTokenURL, String clientId, String userName, String pwd) {
        RestTemplate restTemplate = new RestTemplate();
        String accessTokenString = null;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> mapForm = new LinkedMultiValueMap<>();
        mapForm.add("grant_type", "password");
        mapForm.add("username", userName); //"simavi"
        mapForm.add("client_id", clientId); //"home-app"
        mapForm.add("password", pwd); //"Sm1le@2021"
        ResponseEntity<Object> response = null;

        try {
            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(mapForm, headers);

             response = restTemplate.exchange(resourceTokenURL, HttpMethod.POST, request, Object.class);
            LinkedHashMap<String, Object> map = (LinkedHashMap<String, Object>) response.getBody();

        if (map != null) {
            accessTokenString = (String) map.get("access_token");
        } else {
            System.out.println("Not a good user+password combination");
            //throw new BadCredentialsException("Conectarea la server Keycloack a esuat!");
            return null;
        }
        }
        catch (Exception ex){
            System.out.println(ex.getStackTrace());
        }

        return response;
    }


    private HttpHeaders getRequestHeaderBearer(String tokenValue) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization", "Bearer " + tokenValue);
        return headers;
    }

    public String getUsername(){
        String currentUser = "";
        KeycloakAuthenticationToken authentication = (KeycloakAuthenticationToken)
                SecurityContextHolder.getContext().getAuthentication();

        Principal principal = (Principal) authentication.getPrincipal();

        if (principal instanceof KeycloakPrincipal) {
            KeycloakPrincipal kPrincipal = (KeycloakPrincipal) principal;
            IDToken token = kPrincipal.getKeycloakSecurityContext().getIdToken();
            if(token != null)
                currentUser = token.getPreferredUsername();
            else
                currentUser = ((KeycloakPrincipal<?>) principal).getKeycloakSecurityContext().getToken().getPreferredUsername();
        }
        return currentUser;
    }

    public String getAccessToken(String resourceTokenURL, String clientId, String userName, String pwd) {
        RestTemplate restTemplate = new RestTemplate();
        String accessTokenString = null;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> mapForm = new LinkedMultiValueMap<>();
        mapForm.add("grant_type", "password");
        mapForm.add("username", userName); //"simavi"
        mapForm.add("client_id", clientId); //"home-app"
        mapForm.add("password", pwd); //"Sm1le@2021"
        ResponseEntity<Object> response = null;

        try {
            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(mapForm, headers);

            response = restTemplate.exchange(resourceTokenURL, HttpMethod.POST, request, Object.class);
            LinkedHashMap<String, Object> map = (LinkedHashMap<String, Object>) response.getBody();

            if (map != null) {
                accessTokenString = (String) map.get("access_token");
            } else {
                System.out.println("Not a good user+password combination");
                //throw new BadCredentialsException("Conectarea la server Keycloack a esuat!");
                return null;
            }
        }
        catch (Exception ex){
            System.out.println(ex.getStackTrace());
        }

        return accessTokenString;
    }

}
