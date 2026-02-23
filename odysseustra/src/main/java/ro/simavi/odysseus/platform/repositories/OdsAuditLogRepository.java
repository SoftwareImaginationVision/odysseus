package ro.simavi.odysseus.platform.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ro.simavi.odysseus.platform.entities.OdsAuditLog;

public interface OdsAuditLogRepository extends JpaRepository<OdsAuditLog, Integer> {
}