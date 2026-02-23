package ro.simavi.odysseus.platform.entities;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class PeopleCountingDTO {
    private String licensePlate;
    private Integer peopleCounting;
    private List<Double> peopleConfidence; // List to hold confidence values for each person
    private String peopleCountingImage; // Base64 encoded string for the image
    private String xaiPeopleCountingImage; // Base64 encoded string for the explainable AI image
    private LocalDateTime datetime;

    public PeopleCountingDTO() {
    }

    public PeopleCountingDTO(String licensePlate, Integer peopleCounting, List<Double> peopleConfidence, String peopleCountingImage, String xaiPeopleCountingImage, LocalDateTime datetime) {
        this.licensePlate = licensePlate;
        this.peopleCounting = peopleCounting;
        this.peopleConfidence = peopleConfidence;
        this.peopleCountingImage = peopleCountingImage;
        this.xaiPeopleCountingImage = xaiPeopleCountingImage;
        this.datetime = datetime;
    }
}
