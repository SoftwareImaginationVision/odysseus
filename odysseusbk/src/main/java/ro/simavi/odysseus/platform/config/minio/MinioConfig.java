package ro.simavi.odysseus.platform.config.minio;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "minio")
@Data
public class MinioConfig {

    private String url;
    private String accessKey;
    private String secretKey;

}
