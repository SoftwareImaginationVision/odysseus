package ro.simavi.odysseus.platform.dto;

import lombok.Getter;
import lombok.Setter;
import org.apache.james.mime4j.field.datetime.DateTime;

import java.time.LocalDateTime;

@Getter
@Setter
public class AlertsDTO {
    private String firstname;
    private String lastname;
    private String transactionUuid;
    private String transactionStatus;
    private LocalDateTime dateTime;
}
