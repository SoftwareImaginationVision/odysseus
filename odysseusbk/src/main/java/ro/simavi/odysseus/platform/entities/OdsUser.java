package ro.simavi.odysseus.platform.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "ods_users", schema = "odysseus_bk")
public class OdsUser {
    @Id
    @Column(name = "user_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Size(max = 256)
    @Column(name = "user_name", length = 256)
    private String userName;

    @Size(max = 128)
    @Column(name = "status", length = 128)
    private String status;

    @Size(max = 256)
    @Column(name = "email", length = 256)
    private String email;

    @Column(name = "created_on")
    private Instant createdOn;

    public OdsUser() {
    }

    public OdsUser(String userName, String status, String email, Instant createdOn) {
        this.userName = userName;
        this.status = status;
        this.email = email;
        this.createdOn = createdOn;
    }
}