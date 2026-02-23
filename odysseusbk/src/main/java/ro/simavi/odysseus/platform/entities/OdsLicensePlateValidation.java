package ro.simavi.odysseus.platform.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "ods_license_plate_validation", schema = "odysseus_bk")
public class OdsLicensePlateValidation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "car_color")
    private String carColor;
    @Column(name = "color_confidence")
    private String colorConfidence;
    @Column(name = "color_reasoning")
    private String colorReasoning;
    @Column(name = "car_type")
    private String carType;
    @Column(name = "type_confidence")
    private String typeConfidence;
    @Column(name = "type_reasoning")
    private String typeReasoning;
    @Column(name = "license_plate")
    private String licensePlate;
    @Column(name = "license_plate_confidence")
    private String licensePlateConfidence;
    @Column(name = "license_plate_image")
    private String licensePlateImage;
    @Column(name = "xai_license_plate_image")
    private String xaiLicensePlateImage;
    @Column(name = "people_counting")
    private String peopleCounting;
    @Column(name = "people_counting_confidence")
    private String peopleCountingConfidence;
    @Column(name = "people_counting_image")
    private String peopleCountingImage;
    @Column(name = "xai_people_counting_image")
    private String xaiPeopleCountingImage;
    @Column(name = "date_time")
    private String datetime;
    @Column(name = "passengers")
    private String passengers;
    @Column(name = "transaction_uuid")
    private String transactionUuid;
    @Column(name = "transaction_status")
    private String transactionStatus;
    @Column(name = "person_decision")
    private String personDecision;

}
