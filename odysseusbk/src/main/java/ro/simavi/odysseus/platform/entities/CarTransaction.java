package ro.simavi.odysseus.platform.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;


@Getter
@Setter
@Entity
@Table(name = "ods_transactions_cars", schema = "odysseus_bk")
public class CarTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "transaction_uuid")
    private String transactionUuid;

    @Column(name = "transaction_status")
    private String transactionStatus;

    @Column(name = "license_plate")
    private String licensePlate;

    @Column(name = "license_plate_confidence")
    private String licensePlateConfidence;

    @Column(name = "person_decision")
    private String personDecision;

    public CarTransaction() {
    }

    public CarTransaction(String transactionUuid, String transactionStatus, String licensePlate, String licensePlateConfidence, String personDecision) {
        this.transactionUuid = transactionUuid;
        this.transactionStatus = transactionStatus;
        this.licensePlate = licensePlate;
        this.licensePlateConfidence = licensePlateConfidence;
        this.personDecision = personDecision;
    }

    public CarTransaction(String transactionUuid, String transactionStatus, String licensePlate, String licensePlateConfidence) {
        this.transactionUuid = transactionUuid;
        this.transactionStatus = transactionStatus;
        this.licensePlate = licensePlate;
        this.licensePlateConfidence = licensePlateConfidence;
    }
}
