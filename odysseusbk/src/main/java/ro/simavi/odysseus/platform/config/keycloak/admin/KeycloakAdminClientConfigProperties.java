package ro.simavi.odysseus.platform.config.keycloak.admin;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "keycloak-admin-client")
public class KeycloakAdminClientConfigProperties {

    private String realm;
    private String authServerUrl;
    private String client;
    private String secret;

}
