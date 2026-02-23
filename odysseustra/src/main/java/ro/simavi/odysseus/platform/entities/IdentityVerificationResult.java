package ro.simavi.odysseus.platform.entities;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class IdentityVerificationResult {
    private OdsTraveler odsTraveler;
    private boolean pass;
    private String result;
    private String controlledBy;
    private Date timneOfControll;
}
