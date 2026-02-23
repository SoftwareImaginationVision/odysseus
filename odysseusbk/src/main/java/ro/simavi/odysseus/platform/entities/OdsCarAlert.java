package ro.simavi.odysseus.platform.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "ods_cars_alerts", schema = "odysseus_bk")
public class OdsCarAlert {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "license_plate")
    private String licencePlate;
    @Column(name = "transaction_uuid")
    private String transactionUuid;
    @Column(name = "transaction_status")
    private String transactionStatus;
    @Column(name = "date_time_created")
    private String dateTimeCreated;

}
