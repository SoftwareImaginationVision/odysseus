package ro.simavi.odysseus.platform.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "ods_audit_log", schema = "odysseus_bk")
public class OdsAuditLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Size(max = 2048)
    @Column(name = "short_text")
    private String shortText;

    @Size(max = 4096)
    @Column(name = "log_text")
    private String logText;

    @Column(name = "creation_date")
    private Instant creationDate;

    @Size(max = 256)
    @Column(name = "log_type")
    private String logType;

    @Size(max = 256)
    @Column(name = "audit_logcol")
    private String auditLogcol;

    @Size(max = 256)
    @Column(name = "created_by")
    private String createdBy;

    @Size(max = 256)
    @Column(name = "application")
    private String application;

    @Size(max = 2560)
    @Column(name = "operation_result")
    private String operationResult;

    @Size(max = 2560)
    @Column(name = "comments")
    private String comments;

    @Size(max = 256)
    @Column(name = "transaction_id", length = 256)
    private String transactionId;

    @Size(max = 256)
    @Column(name = "document_id", length = 256)
    private String documentId;

    @Size(max = 256)
    @Column(name = "traveler_id", length = 256)
    private String travelerId;

}