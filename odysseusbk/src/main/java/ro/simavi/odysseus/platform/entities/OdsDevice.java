package ro.simavi.odysseus.platform.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Getter
@Setter
@Entity
@Table(name = "ods_devices", schema = "odysseus_bk")
public class OdsDevice {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Size(max = 1024)
    @Column(name = "guid", length = 1024)
    private String guid;

    @Size(max = 100)
    @Column(name = "device_name", length = 100)
    private String deviceName;

    @Size(max = 1000)
    @Column(name = "description", length = 1000)
    private String description;

    @Size(max = 45)
    @Column(name = "device_type", length = 45)
    private String deviceType;

    @Size(max = 45)
    @Column(name = "um", length = 45)
    private String um;

    @Column(name = "min_value")
    private Double minValue;

    @Column(name = "max_value")
    private Double maxValue;

    public OdsDevice(){}

    public OdsDevice(String guid, String deviceName, String description, String deviceType, String um, Double minValue, Double maxValue) {
        this.guid = guid;
        this.deviceName = deviceName;
        this.description = description;
        this.deviceType = deviceType;
        this.um = um;
        this.minValue = minValue;
        this.maxValue = maxValue;
    }

    @Override
    public String toString() {
        return "OdsDevice{" +
                "guid='" + guid + '\'' +
                ", deviceName='" + deviceName + '\'' +
                ", description='" + description + '\'' +
                ", deviceType='" + deviceType + '\'' +
                ", um='" + um + '\'' +
                ", minValue=" + minValue +
                ", maxValue=" + maxValue +
                '}';
    }
}