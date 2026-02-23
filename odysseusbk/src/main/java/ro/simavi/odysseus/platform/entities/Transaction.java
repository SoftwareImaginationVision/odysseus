package ro.simavi.odysseus.platform.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "ods_transactions", schema = "odysseus_bk")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "transaction_uuid")
    private String transactionUuid;

    @Column(name = "transaction_status")
    private String transactionStatus;

    @Column(name = "traveler_id")
    private Integer travelerId;

    @Column(name = "travel_id")
    private Integer travelId;

    @Column(name = "person_decision")
    private String personDecision;

    public Transaction() {
    }

    public Transaction(String transactionUuid, String transactionStatus, Integer travelerId, Integer travelId) {
        this.transactionUuid = transactionUuid;
        this.transactionStatus = transactionStatus;
        this.travelerId = travelerId;
        this.travelId = travelId;
    }

    public Transaction(String transactionUuid, String transactionStatus, Integer travelerId, Integer travelId, String personDecision) {
        this.transactionUuid = transactionUuid;
        this.transactionStatus = transactionStatus;
        this.travelerId = travelerId;
        this.travelId = travelId;
        this.personDecision = personDecision;
    }
}
