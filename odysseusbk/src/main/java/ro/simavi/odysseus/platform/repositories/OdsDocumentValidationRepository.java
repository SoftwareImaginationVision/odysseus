package ro.simavi.odysseus.platform.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import ro.simavi.odysseus.platform.entities.OdsDocumentValidation;

public interface OdsDocumentValidationRepository extends JpaRepository<OdsDocumentValidation, Integer> {
    OdsDocumentValidation findFirstByTransactionUuid(@Param("transaction_uuid") String transactionUuid);
}
