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
@Table(name = "ods_workflow_instance", schema = "odysseus_bk")
public class OdsWorkflowInstance {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "pool_id")
    private Long poolId;

    @Column(name = "lru_position")
    private Long lruPosition;

    @Size(max = 255)
    @Column(name = "workflow_name")
    private String workflowName;

    @Column(name = "wf_space")
    private Long wfSpace;

    @Size(max = 45)
    @Column(name = "workflow_status", length = 45)
    private String workflowStatus;

    @Column(name = "page_number")
    private Long pageNumber;

    @Column(name = "created_on")
    private Instant createdOn;

    @Size(max = 64)
    @Column(name = "page_type", length = 64)
    private String pageType;

    @Size(max = 255)
    @Column(name = "instance_name")
    private String instanceName;

    @Column(name = "flush_type")
    private Long flushType;

    @Column(name = "fix_count")
    private Long fixCount;

    @Size(max = 3)
    @Column(name = "is_hashed", length = 3)
    private String isHashed;

    @Column(name = "newest_modification")
    private Long newestModification;

    @Column(name = "oldest_modification")
    private Long oldestModification;

    @Column(name = "access_time")
    private Instant accessTime;

    @Size(max = 255)
    @Column(name = "traveler_uuid", length = 255)
    private String travelerUuid;

    @Size(max = 1024)
    @Column(name = "index_name", length = 1024)
    private String indexName;

    @Column(name = "number_records")
    private Long numberRecords;

    @Column(name = "data_size")
    private Long dataSize;

    @Column(name = "compressed_size")
    private Long compressedSize;

    @Column(name = "result_workflow")
    private String resultWorkflow;

    @Size(max = 64)
    @Column(name = "io_fix", length = 64)
    private String ioFix;

    @Size(max = 3)
    @Column(name = "is_old", length = 3)
    private String isOld;

    @Column(name = "free_page_clock")
    private Long freePageClock;

    @Override
    public String toString() {
        return workflowName;
    }
}