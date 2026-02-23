package ro.simavi.odysseus.platform.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "ods_files", schema = "odysseus_bk")
public class OdsFile {
    @Id
    @Column(name = "id", nullable = false)
    private Integer id;

    @Size(max = 1024)
    @Column(name = "name", length = 1024)
    private String name;

    @Size(max = 256)
    @Column(name = "type", length = 256)
    private String type;

    @Size(max = 256)
    @Column(name = "owner", length = 256)
    private String owner;

    @Column(name = "content")
    private byte[] content;

    @Lob
    @Column(name = "loid")
    private String loid;

    @Column(name = "updated_at")
    private Instant updatedAt;

    @Size(max = 2048)
    @Column(name = "file_path", length = 2048)
    private String filePath;

}