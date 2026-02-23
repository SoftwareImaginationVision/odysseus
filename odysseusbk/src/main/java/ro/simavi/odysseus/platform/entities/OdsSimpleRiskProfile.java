package ro.simavi.odysseus.platform.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "ods_simple_risk_profile", schema = "odysseus_bk")
public class OdsSimpleRiskProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Size(max = 255)
    @Column(name = "entity_name")
    private String entityName;

    @Size(max = 32)
    @Column(name = "binar_operator", length = 32)
    private String binarOperator;

    @Size(max = 32)
    @Column(name = "unar_operator", length = 32)
    private String unarOperator;

    @Size(max = 32)
    @Column(name = "ternar_operator", length = 32)
    private String ternarOperator;

    @Size(max = 32)
    @Column(name = "data_type", length = 32)
    private String dataType;

    @Size(max = 1027)
    @Column(name = "value1", length = 1027)
    private String value1;

    @Size(max = 1027)
    @Column(name = "value2", length = 1027)
    private String value2;

    @Size(max = 1027)
    @Column(name = "value3", length = 1027)
    private String value3;

    @Size(max = 1027)
    @Column(name = "full_description", length = 1027)
    private String fullDescription;

    @Size(max = 63)
    @Column(name = "profile_name", length = 63)
    private String profileName;

    @Column(name = "risk_value")
    private Float riskValue;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "valid_from")
    private Instant validFrom;

    @Column(name = "valid_to")
    private Instant validTo;

    @Size(max = 127)
    @Column(name = "column_name", length = 127)
    private String columnName;

}