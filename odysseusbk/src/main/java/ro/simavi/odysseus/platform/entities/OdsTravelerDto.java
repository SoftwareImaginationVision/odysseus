package ro.simavi.odysseus.platform.entities;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
public class OdsTravelerDto {

    private String firstName;
    private String lastName;
    private String email;
    private LocalDate passingDatePlanned;
    private String bcpPlanned;
    private String registeredUser;

    private String subject;

    private String picture;

    public OdsTravelerDto() {
    }

    public OdsTravelerDto(String firstName, String lastName, String email, LocalDate passingDatePlanned, String bcpPlanned, String registeredUser, String subject, String picture) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.passingDatePlanned = passingDatePlanned;
        this.bcpPlanned = bcpPlanned;
        this.registeredUser = registeredUser;
        this.subject = subject;
        this.picture = picture;
    }
}
