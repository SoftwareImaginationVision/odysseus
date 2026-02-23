package ro.simavi.odysseus.platform.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "ods_notifications", schema = "odysseus_bk")
public class OdsNotification {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Size(max = 255)
    @Column(name = "short_content")
    private String shortContent;

    @Size(max = 4000)
    @Column(name = "full_content", length = 4000)
    private String fullContent;

    @Column(name = "severity")
    private Short severity;

    @Column(name = "creation_date")
    private Instant creationDate;

    @Size(max = 100)
    @Column(name = "created_by", length = 100)
    private String createdBy;

    @Size(max = 100)
    @Column(name = "application_name", length = 100)
    private String applicationName;

    @Column(name = "expiration_date")
    private Instant expirationDate;

    @Size(max = 255)
    @Column(name = "notification_status")
    private String notificationStatus;

    @Size(max = 100)
    @Column(name = "target", length = 100)
    private String target;

    @Size(max = 255)
    @Column(name = "url")
    private String url;

    @Column(name = "source_mission_id")
    private Integer sourceMissionId;

    @Size(max = 100)
    @Column(name = "task_name", length = 100)
    private String taskName;

    @Size(max = 1024)
    @Column(name = "area_name", length = 1024)
    private String areaName;

    @Size(max = 254)
    @Column(name = "position", length = 254)
    private String position;

    public OdsNotification() {
    }

    public OdsNotification(String shortContent, String fullContent, Short severity, Instant creationDate, String createdBy, String applicationName, Instant expirationDate, String notificationStatus, String target, String url, Integer sourceMissionId, String taskName, String areaName, String position) {
        this.shortContent = shortContent;
        this.fullContent = fullContent;
        this.severity = severity;
        this.creationDate = creationDate;
        this.createdBy = createdBy;
        this.applicationName = applicationName;
        this.expirationDate = expirationDate;
        this.notificationStatus = notificationStatus;
        this.target = target;
        this.url = url;
        this.sourceMissionId = sourceMissionId;
        this.taskName = taskName;
        this.areaName = areaName;
        this.position = position;
    }
}