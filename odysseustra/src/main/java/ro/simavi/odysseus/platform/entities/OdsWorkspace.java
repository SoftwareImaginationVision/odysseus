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
@Table(name = "ods_workspaces", schema = "odysseus_bk")
public class OdsWorkspace {
    @Id
    @Column(name = "ws_id", nullable = false)
    private Integer id;

    @Size(max = 128)
    @Column(name = "ws_name", length = 128)
    private String wsName;

    @Size(max = 1024)
    @Column(name = "ws_description", length = 1024)
    private String wsDescription;

    @Size(max = 128)
    @Column(name = "ws_type", length = 128)
    private String wsType;

    @Size(max = 128)
    @Column(name = "ws_owner", length = 128)
    private String wsOwner;

    @Column(name = "ws_created_on")
    private Instant wsCreatedOn;

    @Size(max = 128)
    @Column(name = "ws_status", length = 128)
    private String wsStatus;

    @Size(max = 1024)
    @Column(name = "ws_role", length = 1024)
    private String wsRole;

}