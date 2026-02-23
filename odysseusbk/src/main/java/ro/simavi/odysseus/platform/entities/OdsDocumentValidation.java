package ro.simavi.odysseus.platform.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "ods_document_verification", schema = "odysseus_bk")
public class OdsDocumentValidation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "language")
    private String language;

    // DTC-VC fields
    @Column(name = "vc_expiration_on")
    private String vcExpirationOn;
    @Column(name = "vc_gender")
    private String vcGender;
    @Column(name = "vc_date_of_birth")
    private String vcDateOfBirth;
    @Column(name = "vc_given_name")
    private String vcGivenName;
    @Column(name = "vc_surname")
    private String vcSurname;
    @Column(name = "vc_mrz")
    private String vcMrz;
    @Column(name = "vc_electronic")
    private String vcElectronic;
    @Column(name = "vc_issued_on")
    private String vcIssuedOn;
    @Column(name = "vc_nationality")
    private String vcNationality;
    @Column(name = "vc_type")
    private String vcType;
    @Column(name = "vc_type_id")
    private String vcTypeId;
    @Column(name = "vc_type_name")
    private String vcTypeName;
    @Column(name = "vc_number")
    private String vcNumber;
    @Column(name = "vc_issuer_state")
    private String vcIssuerState;

    // DTC-PC fields
    @Column(name = "pc_expiration_on")
    private String pcExpirationOn;
    @Column(name = "pc_gender")
    private String pcGender;
    @Column(name = "pc_date_of_birth")
    private String pcDateOfBirth;
    @Column(name = "pc_given_name")
    private String pcGivenName;
    @Column(name = "pc_surname")
    private String pcSurname;
    @Column(name = "pc_mrz")
    private String pcMrz;
    @Column(name = "pc_electronic")
    private String pcElectronic;
    @Column(name = "pc_issued_on")
    private String pcIssuedOn;
    @Column(name = "pc_nationality")
    private String pcNationality;
    @Column(name = "pc_type")
    private String pcType;
    @Column(name = "pc_type_id")
    private String pcTypeId;
    @Column(name = "pc_type_name")
    private String pcTypeName;
    @Column(name = "pc_number")
    private String pcNumber;
    @Column(name = "pc_issuer_state")
    private String pcIssuerState;

    // EMRTD fields
    @Column(name = "emrtd_expiration_on")
    private String emrtdExpirationOn;
    @Column(name = "emrtd_gender")
    private String emrtdGender;
    @Column(name = "emrtd_date_of_birth")
    private String emrtdDateOfBirth;
    @Column(name = "emrtd_given_name")
    private String emrtdGivenName;
    @Column(name = "emrtd_surname")
    private String emrtdSurname;
    @Column(name = "emrtd_mrz")
    private String emrtdMrz;
    @Column(name = "emrtd_electronic")
    private String emrtdElectronic;
    @Column(name = "emrtd_issued_on")
    private String emrtdIssuedOn;
    @Column(name = "emrtd_nationality")
    private String emrtdNationality;
    @Column(name = "emrtd_type")
    private String emrtdType;
    @Column(name = "emrtd_type_id")
    private String emrtdTypeId;
    @Column(name = "emrtd_type_name")
    private String emrtdTypeName;
    @Column(name = "emrtd_number")
    private String emrtdNumber;
    @Column(name = "emrtd_issuer_state")
    private String emrtdIssuerState;


    // Biometrics (Serialized as JSON String or Stored in a Separate Table)
    //@Lob
    @Column(name = "biometrics")
    private String biometrics; // Can store JSON array as text

    // Validation Status
    @Column(name = "document_authenticated")
    private boolean documentAuthenticated;
    @Column(name = "liveness_check_passed")
    private boolean livenessCheckPassed;
    @Column(name = "biometry_matched")
    private boolean biometryMatched;

    // Transaction
    @Column(name = "transaction_uuid")
    private String transactionUuid;
    @Column(name = "transaction_status")
    private String transactionStatus;
    @Column(name = "traveler_id")
    private String travelerId;
    @Column(name = "travel_id")
    private String travelId;
    @Column(name = "person_decision")
    private String personDecision;
}
