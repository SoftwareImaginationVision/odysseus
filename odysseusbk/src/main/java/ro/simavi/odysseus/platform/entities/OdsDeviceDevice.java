package ro.simavi.odysseus.platform.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "ods_device_device", schema = "odysseus_bk")
public class OdsDeviceDevice {
    @Id
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "device_parent_id")
    private OdsDevice deviceParent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "device_child_id")
    private OdsDevice deviceChild;

}