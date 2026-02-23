package ro.simavi.odysseus.platform.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.Instant;
import java.time.LocalTime;

@Getter
@Setter
@Entity
@Table(name = "ods_combined_risk_profile", schema = "odysseus_bk")
public class OdsCombinedRiskProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Size(max = 255)
    @Column(name = "profile_name")
    private String profileName;

    @Column(name = "simple_risk1_id")
    private Integer simpleRisk1Id;

    @Column(name = "simple_risk2_id")
    private Integer simpleRisk2Id;

    @Column(name = "complex_risk1_id")
    private Integer complexRisk1Id;

    @Column(name = "complex_risk2_id")
    private Integer complexRisk2Id;

    @Size(max = 32)
    @Column(name = "logical_operator", length = 32)
    private String logicalOperator;

    @Size(max = 1027)
    @Column(name = "full_description", length = 1027)
    private String fullDescription;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "valid_from")
    private Instant validFrom;

    @Column(name = "valid_to")
    private LocalTime validTo;

}