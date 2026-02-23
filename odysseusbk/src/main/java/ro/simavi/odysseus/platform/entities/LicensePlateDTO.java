package ro.simavi.odysseus.platform.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class LicensePlateDTO {
    @JsonProperty("car_color")
    private String carColor;
    @JsonProperty("color_confidence")
    private Double colorConfidence;
    @JsonProperty("color_reasoning")
    private String colorReasoning;
    @JsonProperty("car_type")
    private String carType;
    @JsonProperty("type_confidence")
    private Double typeConfidence;
    @JsonProperty("type_reasoning")
    private String typeReasoning;
    @JsonProperty("license_plate")
    private String licensePlate;
    @JsonProperty("license_plate_confidence")
    private Double licensePlateConfidence;
    @JsonProperty("license_plate_image")
    private String licensePlateImage; // Base64 encoded string
    @JsonProperty("xai_license_plate_image")
    private String xaiLicensePlateImage; // Base64 encoded string
    @JsonProperty("people_counting")
    private Integer peopleCounting;
    @JsonProperty("people_counting_confindece")
    private List<Double> peopleConfidence; // List to hold confidence values for each person
    @JsonProperty("people_counting_image")
    private String peopleCountingImage; // Base64 encoded string for the image
    @JsonProperty("xai_people_counting_image")
    private String xaiPeopleCountingImage; // Base64 encoded string for the explainable AI image
    @JsonProperty("datetime")
    private LocalDateTime datetime;

    public LicensePlateDTO() {
    }

}
