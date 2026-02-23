package ro.simavi.odysseus.platform.entities;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "ods_workflow_instance_nodes", schema = "odysseus_bk")
public class OdsWorkflowInstanceNode {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Size(max = 127)
    @Column(name = "node_name", length = 127)
    private String nodeName;

    @Column(name = "drule_id")
    private Integer druleId;

    @Column(name = "drule_parameters")
    @Type(type = "org.hibernate.type.TextType")
    private String druleParameters;

    @Column(name = "created_on")
    private Instant createdOn;

    @Size(max = 127)
    @Column(name = "node_text", length = 127)
    private String nodeText;

    @Column(name = "workflow_instance_id")
    private Integer workflowInstanceId;

    @Size(max = 255)
    @Column(name = "workflow_instance_name")
    private String workflowInstanceName;

    @Size(max = 255)
    @Column(name = "drule_name")
    private String druleName;

    @Column(name = "hit_rate")
    private Long hitRate;

    @Size(max=32)
    @Column(name = "node_status")
    private String nodeStatus;

    @Size(max = 127)
    @Column(name = "node_type", length = 127)
    private String nodeType;

    @Size(max=255)
    @Column(name = "traveler_uuid")
    private String travelerUuid;
}