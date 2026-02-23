package ro.simavi.odysseus.platform.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "ods_workflow", schema = "odysseus_bk")
public class OdsWorkflow {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Size(max = 255)
    @NotNull
    @Column(name = "workflow_name", nullable = false)
    private String workflowName;

    @Column(name = "creation_date")
    private Instant creationDate;

    @Column(name = "instance_duration")
    private Integer instanceDuration;

    public OdsWorkflow() {
    }

    public OdsWorkflow(String workflowName, Instant creationDate, Integer instanceDuration) {
        this.workflowName = workflowName;
        this.creationDate = creationDate;
        this.instanceDuration = instanceDuration;
    }

    @Override
    public String toString() {
        return workflowName;
    }

}