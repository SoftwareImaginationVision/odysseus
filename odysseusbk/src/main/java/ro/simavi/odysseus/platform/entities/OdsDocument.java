package ro.simavi.odysseus.platform.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
@Entity
@Table(name = "ods_documents", schema = "odysseus_bk")
public class OdsDocument {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "traveler_id")
    private Integer travelerId;

    @Size(max = 8)
    @Column(name = "document_type", length = 8)
    private String documentType;

    @Size(max = 8)
    @NotNull
    @Column(name = "country_code", nullable = false, length = 8)
    private String countryCode;

    @Size(max = 16)
    @Column(name = "doc_number", length = 16)
    private String docNumber;

    @Size(max = 127)
    @Column(name = "surname", length = 127)
    private String surname;

    @Size(max = 127)
    @Column(name = "given_name", length = 127)
    private String givenName;

    @Size(max = 32)
    @Column(name = "citizenship", length = 32)
    private String citizenship;


    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Size(max = 127)
    @Column(name = "personal_id", length = 127)
    private String personalId;

    @Size(max = 10)
    @Column(name = "sex", length = 10)
    private String sex;

    @Size(max = 127)
    @Column(name = "place_of_birth", length = 127)
    private String placeOfBirth;


    @Column(name = "date_of_issuance")
    private LocalDate dateOfIssuance;

    @Size(max = 32)
    @Column(name = "authority", length = 32)
    private String authority;


    @Column(name = "date_of_expiry")
    private LocalDate dateOfExpiry;

    @Size(max = 64)
    @Column(name = "doc_full_id", length = 64)
    private String docFullId;

    @Column(name = "heigh")
    private Integer height;

    @Size(max = 32)
    @Column(name = "color_of_eyes", length = 32)
    private String colorOfEyes;

    @Size(max = 127)
    @Column(name = "residence", length = 127)
    private String residence;

    @Column(name = "picture")
    private byte[] picture;

    @Column(name = "has_risk")
    private String hasRisk;

    public OdsDocument(Integer travelerId, String documentType, String countryCode, String docNumber, String surname, String givenName, String citizenship, LocalDate dateOfBirth, String personalId, String sex, String placeOfBirth, LocalDate dateOfIssuance, String authority, LocalDate dateOfExpiry, String docFullId, Integer height, String colorOfEyes, String residence, byte[] picture) {
        this.travelerId = travelerId;
        this.documentType = documentType;
        this.countryCode = countryCode;
        this.docNumber = docNumber;
        this.surname = surname;
        this.givenName = givenName;
        this.citizenship = citizenship;
        this.dateOfBirth = dateOfBirth;
        this.personalId = personalId;
        this.sex = sex;
        this.placeOfBirth = placeOfBirth;
        this.dateOfIssuance = dateOfIssuance;
        this.authority = authority;
        this.dateOfExpiry = dateOfExpiry;
        this.docFullId = docFullId;
        this.height = height;
        this.colorOfEyes = colorOfEyes;
        this.residence = residence;
        this.picture = picture;
    }

    public OdsDocument() {

    }

    public boolean checkAgeBetween30And50(LocalDate dateOfBirth){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");


        LocalDate currentDate = LocalDate.now();
        int age = Period.between(dateOfBirth, currentDate).getYears();

        return (age >= 30 && age <= 50);
    }

    @Override
    public String toString() {
        return "OdsDocument{" +
                "id=" + id +
                ", travelerId=" + travelerId +
                ", documentType='" + documentType + '\'' +
                ", countryCode='" + countryCode + '\'' +
                ", docNumber='" + docNumber + '\'' +
                ", surname='" + surname + '\'' +
                ", givenName='" + givenName + '\'' +
                ", citizenship='" + citizenship + '\'' +
                ", dateOfBirth='" + dateOfBirth + '\'' +
                ", personalId='" + personalId + '\'' +
                ", sex='" + sex + '\'' +
                ", placeOfBirth='" + placeOfBirth + '\'' +
                ", dateOfIssuance='" + dateOfIssuance + '\'' +
                ", authority='" + authority + '\'' +
                ", dateOfExpiry='" + dateOfExpiry + '\'' +
                ", docFullId='" + docFullId + '\'' +
                ", height=" + height +
                ", colorOfEyes='" + colorOfEyes + '\'' +
                ", residence='" + residence + '\'' +
                '}';
    }
}