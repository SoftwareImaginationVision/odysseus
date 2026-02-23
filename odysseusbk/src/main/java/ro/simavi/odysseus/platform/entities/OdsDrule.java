package ro.simavi.odysseus.platform.entities;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "ods_drules", schema = "odysseus_bk")
public class OdsDrule {
    @Id
    @Column(name = "id", nullable = false)
    private Integer id;

    @Size(max = 127)
    @Column(name = "owner", length = 127)
    private String owner;

    @Column(name = "registered_at")
    private Instant registeredAt;

    @Size(max = 45)
    @Column(name = "rule_code", length = 45)
    private String ruleCode;

    @Column(name = "rule_content")
    @Type(type = "org.hibernate.type.TextType")
    private String ruleContent;

    @Size(max = 1000)
    @Column(name = "rule_description", length = 1000)
    private String ruleDescription;

    @NotNull
    @Column(name = "rule_id", nullable = false)
    private Integer ruleId;

    @Size(max = 45)
    @Column(name = "rule_name", length = 45)
    private String ruleName;

    @Size(max = 45)
    @Column(name = "rule_status", length = 45)
    private String ruleStatus;

    @Size(max = 45)
    @Column(name = "rule_type", length = 45)
    private String ruleType;

    @Size(max = 45)
    @Column(name = "rule_version", length = 45)
    private String ruleVersion;

    @Column(name = "valid_from")
    private Instant validFrom;

    @Column(name = "valid_to")
    private Instant validTo;

}