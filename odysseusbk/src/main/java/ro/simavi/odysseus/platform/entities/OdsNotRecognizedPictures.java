package ro.simavi.odysseus.platform.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "ods_not_recognized_pictures", schema = "odysseus_bk")
public class OdsNotRecognizedPictures {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "subject")
    private String subject;

    @Column(name = "rec_status")
    private String recognitionStatus;

    @Column(name = "registration_date")
    private LocalDate registrationDate;

    @Column(name = "img_source")
    private String imageSource;

    @Column(name = "image")
    private byte[] image;

    @Column(name = "description")
    private String description;
}
