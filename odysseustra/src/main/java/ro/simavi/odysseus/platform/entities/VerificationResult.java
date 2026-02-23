package ro.simavi.odysseus.platform.entities;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class VerificationResult {
    private OdsDocument odsDocument;
    private boolean pass;
    private String result;
    private String controlledBy;
    private Date timneOfControll;
}
