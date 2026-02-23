package ro.simavi.odysseus.platform.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Size;

@Getter
@Setter
@Entity
@Table(name = "ods_communication_chanels", schema = "odysseus_bk")
public class OdsCommunicationChanel {
    @Id
    @Column(name = "id", nullable = false)
    private Integer id;

    @Size(max = 100)
    @Column(name = "chanel_name", length = 100)
    private String chanelName;

    @Size(max = 1000)
    @Column(name = "description", length = 1000)
    private String description;

    @Size(max = 1000)
    @Column(name = "input_address", length = 1000)
    private String inputAddress;

    @Size(max = 1000)
    @Column(name = "exposed_address", length = 1000)
    private String exposedAddress;

    @Size(max = 100)
    @Column(name = "content_format", length = 100)
    private String contentFormat;

    @Size(max = 45)
    @Column(name = "protocol", length = 45)
    private String protocol;

    @Column(name = "mission_id")
    private Integer missionId;

    @Size(max = 45)
    @Column(name = "chanel_status", length = 45)
    private String chanelStatus;

}