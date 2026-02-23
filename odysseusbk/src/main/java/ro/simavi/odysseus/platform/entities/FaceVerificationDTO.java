package ro.simavi.odysseus.platform.entities;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FaceVerificationDTO {
    String subject;
    String image;
    String result;
}
