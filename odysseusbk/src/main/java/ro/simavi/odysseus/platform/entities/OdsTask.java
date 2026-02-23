package ro.simavi.odysseus.platform.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "ods_tasks", schema = "odysseus_bk")
public class OdsTask {
    @Id
    @Column(name = "id", nullable = false)
    private Integer id;

    @Size(max = 100)
    @Column(name = "task_name", length = 100)
    private String taskName;

    @Size(max = 4000)
    @Column(name = "task_description", length = 4000)
    private String taskDescription;

    @Column(name = "starting_date")
    private Instant startingDate;

    @Column(name = "ending_date")
    private Instant endingDate;

    @Column(name = "created_on")
    private Instant createdOn;

    @Size(max = 100)
    @Column(name = "created_by", length = 100)
    private String createdBy;

    @Size(max = 1024)
    @Column(name = "guid", length = 1024)
    private String guid;

    @Column(name = "mission_id")
    private Integer missionId;

    @Size(max = 100)
    @Column(name = "task_status", length = 100)
    private String taskStatus;

    @Size(max = 100)
    @Column(name = "task_type", length = 100)
    private String taskType;

    @Size(max = 1024)
    @Column(name = "url", length = 1024)
    private String url;

}