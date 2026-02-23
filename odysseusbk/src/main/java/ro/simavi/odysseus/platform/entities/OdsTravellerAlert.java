package ro.simavi.odysseus.platform.entities;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "ods_travellers_alerts", schema = "odysseus_bk")
public class OdsTravellerAlert {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "first_name")
    private String firstname;

    @Column(name = "last_name")
    private String lastname;

    @Column(name = "transaction_uuid")
    private String transactionUuid;

    @Column(name = "transaction_status")
    private String transactionStatus;

    @Column(name = "date_time_created")
    private String dateTimeCreated;
}
