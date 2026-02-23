package ro.simavi.odysseus.platform.util;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.ApplicationScope;

@Getter
@Component
@ApplicationScope
public class ApplicationBuildInfo {
    @Value("${application.build.version}")
    private String version;
    @Value("${application.build.number}")
    private String number;
    @Value("${application.build.time}")
    private String time;
}
