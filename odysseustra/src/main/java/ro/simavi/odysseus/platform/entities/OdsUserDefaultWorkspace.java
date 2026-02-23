package ro.simavi.odysseus.platform.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "ods_user_default_workspace", schema = "odysseus_bk")
public class OdsUserDefaultWorkspace {
    @Id
    @Column(name = "usr_def_ws_id", nullable = false)
    private Integer id;

    @Lob
    @Column(name = "user_name")
    private String userName;

    @Column(name = "ws_id")
    private Integer wsId;

    @Column(name = "updated_on")
    private Instant updatedOn;

    @Size(max = 128)
    @Column(name = "status", length = 128)
    private String status;

    @Lob
    @Column(name = "ws_roles")
    private String wsRoles;

}