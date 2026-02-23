package ro.simavi.odysseus.platform.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ro.simavi.odysseus.platform.entities.OdsAuditLog;

import java.util.List;

public interface OdsAuditLogRepository extends JpaRepository<OdsAuditLog, Integer> {
    List<OdsAuditLog> findAllByShortTextContainingIgnoreCaseOrLogTextContainingIgnoreCaseOrLogTypeContainingIgnoreCaseOrAuditLogcolContainingIgnoreCaseOrCreatedByContainsIgnoreCaseOrOperationResultContainsIgnoreCaseOrDocumentIdContainsIgnoreCaseOrTravelerIdContainsIgnoreCase(@Param("short_text") String shortText, @Param("log_text") String logText, @Param("log_type") String logType, @Param("audit_logcol") String auditLogcol, @Param("created_by") String createdBy, @Param("operation_result") String operationResult, @Param("document_id") String documentId, @Param("traveler_id") String travelerId);

    @Query(value = "SELECT * FROM odysseus_bk.ods_audit_log ORDER BY creation_date DESC LIMIT 10000", nativeQuery = true)
    List<OdsAuditLog> findLast10000();
}