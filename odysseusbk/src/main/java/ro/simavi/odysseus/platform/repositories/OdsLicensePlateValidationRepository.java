package ro.simavi.odysseus.platform.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import ro.simavi.odysseus.platform.entities.OdsLicensePlateValidation;

public interface OdsLicensePlateValidationRepository extends JpaRepository<OdsLicensePlateValidation, Integer> {
    OdsLicensePlateValidation findFirstByTransactionUuid(@Param("transaction_uuid") String transactionUuid);


    boolean existsByTransactionUuid(@Param("transaction_uuid") String transactionUuid);
}
