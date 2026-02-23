package ro.simavi.odysseus.platform.config.keycloak.admin;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@RequiredArgsConstructor
public class KeycloakAccessConfiguration {
    public final KeycloakAdminClientConfigProperties keycloakAdminClientConfigProperties;

    @Bean
    public Keycloak keycloakAdminClient() {
        return KeycloakBuilder.builder()
                .serverUrl(keycloakAdminClientConfigProperties.getAuthServerUrl())
                .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
                .realm(keycloakAdminClientConfigProperties.getRealm())
                .clientId(keycloakAdminClientConfigProperties.getClient())
                .clientSecret(keycloakAdminClientConfigProperties.getSecret())
                .build();
    }

//    @Value("${keycloak.logout}")
//    private String keycloakLogout;
//    public void logout(String refreshToken) throws Exception {
//        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
//        map.add("client_id",keycloakAdminClientConfigProperties.getClient());
//        map.add("client_secret",keycloakAdminClientConfigProperties.getSecret());
//        map.add("refresh_token",refreshToken);
//
//        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, null);
//        restTemplate.postForObject(keycloakLogout, request, String.class);
//    }
}

