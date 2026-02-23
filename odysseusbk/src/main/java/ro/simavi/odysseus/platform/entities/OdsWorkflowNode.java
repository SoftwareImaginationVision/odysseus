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
@Table(name = "ods_workflow_nodes", schema = "odysseus_bk")
public class OdsWorkflowNode {
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

    @Size(max = 50)
    @Column(name = "workflow_id", length = 50)
    private String workflowId;

    @Size(max = 255)
    @Column(name = "drule_name")
    private String druleName;

    @Size(max = 127)
    @Column(name = "node_type", length = 127)
    private String nodeType;

    public OdsWorkflowNode() {
    }

    public OdsWorkflowNode(String nodeName, Instant createdOn, String nodeText, String workflowId) {
        this.nodeName = nodeName;
        this.createdOn = createdOn;
        this.nodeText = nodeText;
        this.workflowId = workflowId;
    }

    public OdsWorkflowNode(String nodeName, Instant createdOn, String nodeText, String workflowId, String nodeType) {
        this.nodeName = nodeName;
        this.createdOn = createdOn;
        this.nodeText = nodeText;
        this.workflowId = workflowId;
        this.nodeType = nodeType;
    }

    @Override
    public String toString() {
        return nodeName;
    }
}