package ro.simavi.odysseus.platform.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "ods_travelers", schema = "odysseus_bk")
public class OdsTraveler {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Size(max = 127)
    @Column(name = "first_name", length = 127)
    private String firstName;

    @Size(max = 127)
    @Column(name = "last_name", length = 127)
    private String lastName;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Size(max = 254)
    @Column(name = "place_of_birth", length = 254)
    private String placeOfBirth;

    @Size(max = 255)
    @Column(name = "street")
    private String street;

    @Size(max = 254)
    @Column(name = "street_details", length = 254)
    private String streetDetails;

    @Size(max = 254)
    @Column(name = "city", length = 254)
    private String city;

    @Size(max = 254)
    @Column(name = "county", length = 254)
    private String county;

    @Size(max = 3)
    @Column(name = "country_code", length = 3)
    private String countryCode;

    @Size(max = 16)
    @Column(name = "zip_code", length = 16)
    private String zipCode;

    @Size(max = 254)
    @Column(name = "email", length = 254)
    private String email;

    @Size(max = 32)
    @Column(name = "phone", length = 32)
    private String phone;

    @Size(max = 254)
    @Column(name = "social_media", length = 254)
    private String socialMedia;

    @Size(max = 254)
    @Column(name = "additional_info", length = 254)
    private String additionalInfo;

    @Size(max = 32)
    @Column(name = "document_type", length = 32)
    private String documentType;

    @Size(max = 254)
    @Column(name = "document_number", length = 254)
    private String documentNumber;

    @Size(max = 254)
    @Column(name = "document_issuer", length = 254)
    private String documentIssuer;

    @Size(max = 254)
    @Column(name = "document_validity", length = 254)
    private String documentValidity;

    @Size(max = 32)
    @Column(name = "personal_id", length = 32)
    private String personalId;

    @Size(max = 254)
    @Column(name = "tuuid", length = 254)
    private String tuuid;

    @Column(name = "picture")
    private byte[] picture;

    @Column(name = "last_updated_at")
    private LocalDate lastUpdatedAt;

    @Column(name = "registered_user")
    private String registeredUser;



    public OdsTraveler() {
    }

    public OdsTraveler(String firstName, String lastName, LocalDate dateOfBirth, String placeOfBirth, String street, String streetDetails, String city, String county,
                       String countryCode, String zipCode, String email, String phone, String socialMedia, String additionalInfo, String documentType, String documentNumber,
                       String documentIssuer, String documentValidity, String personalId) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.placeOfBirth = placeOfBirth;
        this.street = street;
        this.streetDetails = streetDetails;
        this.city = city;
        this.county = county;
        this.countryCode = countryCode;
        this.zipCode = zipCode;
        this.email = email;
        this.phone = phone;
        this.socialMedia = socialMedia;
        this.additionalInfo = additionalInfo;
        this.documentType = documentType;
        this.documentNumber = documentNumber;
        this.documentIssuer = documentIssuer;
        this.documentValidity = documentValidity;
        this.personalId = personalId;
    }

}