package ro.simavi.odysseus.platform.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import ro.simavi.odysseus.platform.entities.OdsTravellerAlert;

import java.util.List;

public interface OdsTravellerAlertRepository extends JpaRepository<OdsTravellerAlert, Integer> {

    List<OdsTravellerAlert> findAllByTransactionUuid(@Param("transaction_uuid") String transactionUuid);

}
