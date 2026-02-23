package ro.simavi.odysseus.platform.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PassengersDTO {
    private String transactionUuid;
    private String firstname;
    private String lastname;
    private String transactionStatus;
}
