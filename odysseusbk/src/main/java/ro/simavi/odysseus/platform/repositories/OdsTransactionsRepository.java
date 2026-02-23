package ro.simavi.odysseus.platform.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ro.simavi.odysseus.platform.entities.OdsTraveler;
import ro.simavi.odysseus.platform.entities.Transaction;

import java.util.List;

public interface OdsTransactionsRepository extends JpaRepository<Transaction, Integer> {

    List<Transaction> findAllByTransactionStatus(@Param("transaction_status") String status);

    Transaction findFirstByTransactionUuid(@Param("transaction_uuid") String transactionUuid);

    List<Transaction> findAllByTransactionStatusIsNotContainingIgnoreCase(@Param("transaction_status") String status);

    @Query(value = "SELECT * FROM odysseus_bk.ods_transactions WHERE traveler_id = :traveler_id ORDER BY id DESC LIMIT 1", nativeQuery = true)
    Transaction findLastByTravelerIdNative(@Param("traveler_id") Integer travelerId);

    @Query(value = "SELECT * FROM odysseus_bk.ods_transactions WHERE traveler_id = :traveler_id AND transaction_status != 'CLOSED' ORDER BY id DESC LIMIT 1", nativeQuery = true)
    Transaction findLastByTravelerIdAndStatusDifClosedNative(@Param("traveler_id") Integer travelerId);

    List<Transaction> findAllByTransactionUuid(@Param("transaction_uuid") String transactionUuid);
}
