package ro.simavi.odysseus.platform.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "ods_workflow_links", schema = "odysseus_bk")
public class OdsWorkflowLink {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Size(max = 127)
    @Column(name = "link_name", length = 127)
    private String linkName;

    @Column(name = "source_node")
    private Integer sourceNode;

    @Column(name = "dest_node")
    private Integer destNode;

    @Size(max = 45)
    @Column(name = "logical_operator", length = 45)
    private String logicalOperator;

    @Size(max = 1000)
    @Column(name = "compared_to", length = 1000)
    private String comparedTo;

    @Column(name = "created_on")
    private Instant createdOn;

    @Size(max = 255)
    @Column(name = "link_text")
    private String linkText;

    @Size(max = 50)
    @Column(name = "workflow_id", length = 50)
    private String workflowId;

}