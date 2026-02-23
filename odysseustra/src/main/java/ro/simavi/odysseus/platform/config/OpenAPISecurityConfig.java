package ro.simavi.odysseus.platform.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPISecurityConfig {

    @Value("${spring.security.oauth2.client.provider.odysseus.authorization-uri}")
    private String authorizationUri;
    @Value("${spring.security.oauth2.client.provider.odysseus.token-uri}")
    private String tokenUri;

    private static final String OAUTH_SCHEME_NAME = "odysseus_oauth";

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .components(new Components()
                        .addSecuritySchemes(OAUTH_SCHEME_NAME, createOAuthScheme()))
                .addSecurityItem(new SecurityRequirement().addList(OAUTH_SCHEME_NAME))
                .info(new Info().title("Todos Management Service")
                        .description("A service providing todos.")
                        .version("1.0"));
    }

    private SecurityScheme createOAuthScheme() {
        OAuthFlows flows = createOAuthFlows();
        return new SecurityScheme().type(SecurityScheme.Type.OAUTH2)
                .flows(flows);
    }

    private OAuthFlows createOAuthFlows() {
        OAuthFlow authorizationCodeFlow = createAuthorizationCodeFlow();
        return new OAuthFlows().authorizationCode(authorizationCodeFlow);
    }

    private OAuthFlow createAuthorizationCodeFlow() {
        return new OAuthFlow()
                .authorizationUrl(authorizationUri)
                .tokenUrl(tokenUri)
                .scopes(new Scopes().addString("openid", "OpenID Connect scope")
                        .addString("profile", "Access user profile")
                        .addString("email", "Access user email")
                        .addString("address", "Access user address")
                        .addString("phone", "Access user phone"));
    }
}
