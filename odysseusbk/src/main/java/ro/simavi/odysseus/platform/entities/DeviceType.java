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
@Table(name = "device_type", schema = "odysseus_bk")
public class DeviceType {
    @Id
    @Size(max = 32)
    @Column(name = "device_type", nullable = false, length = 32)
    private String deviceType;

    @Size(max = 145)
    @Column(name = "type_description", length = 145)
    private String typeDescription;

}