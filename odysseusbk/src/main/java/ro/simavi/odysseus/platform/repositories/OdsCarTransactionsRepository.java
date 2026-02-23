package ro.simavi.odysseus.platform.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import ro.simavi.odysseus.platform.entities.CarTransaction;
import ro.simavi.odysseus.platform.entities.Transaction;

import java.util.List;

public interface OdsCarTransactionsRepository extends JpaRepository<CarTransaction, Integer> {
    CarTransaction findFirstByTransactionUuid(@Param("transaction_uuid") String transactionUuid);

    List<CarTransaction> findAllByTransactionStatusIsNotContainingIgnoreCase(@Param("transaction_status") String status);
    List<CarTransaction> findAllByTransactionUuid(@Param("transaction_uuid") String transactionUuid);
}
