package ro.simavi.odysseus.platform.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "ods_events", schema = "odysseus_bk")
public class OdsEvent {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "end_date")
    private Instant endDate;

    @Size(max = 255)
    @Column(name = "event")
    private String event;

    @Column(name = "start_date")
    private Instant startDate;

    @Size(max = 255)
    @Column(name = "status")
    private String status;

    @Size(max = 255)
    @Column(name = "latitude")
    private String latitude;

    @Size(max = 255)
    @Column(name = "longitude")
    private String longitude;

    @Size(max = 255)
    @Column(name = "detected_target")
    private String detectedTarget;

    @Size(max = 255)
    @Column(name = "specific_area")
    private String specificArea;

    @Size(max = 64)
    @Column(name = "direction", length = 64)
    private String direction;

    @Size(max = 255)
    @Column(name = "approaching_target")
    private String approachingTarget;

    @Size(max = 255)
    @Column(name = "event_source")
    private String eventSource;

    @Size(max = 255)
    @Column(name = "speed")
    private String speed;

    @Size(max = 32)
    @Column(name = "event_type", length = 32)
    private String eventType;

    @Column(name = "severity")
    private Integer severity;

    @Column(name = "sequence")
    private Integer sequence;

    @Size(max = 32)
    @Column(name = "criticality", length = 32)
    private String criticality;

    public OdsEvent(){}
    public OdsEvent(String event, String status, String latitude, String longitude, String detectedTarget, String specificArea, String direction, String approachingTarget, String eventSource, String speed, String eventType, Integer severity, Integer sequence, String criticality) {
        this.endDate = endDate;
        this.event = event;
        this.startDate = startDate;
        this.status = status;
        this.latitude = latitude;
        this.longitude = longitude;
        this.detectedTarget = detectedTarget;
        this.specificArea = specificArea;
        this.direction = direction;
        this.approachingTarget = approachingTarget;
        this.eventSource = eventSource;
        this.speed = speed;
        this.eventType = eventType;
        this.severity = severity;
        this.sequence = sequence;
        this.criticality = criticality;
    }
}