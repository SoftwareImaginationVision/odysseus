package ro.simavi.odysseus.platform.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "ods_travels", schema = "odysseus_bk")
public class OdsTravel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Size(max = 254)
    @Column(name = "travel_name", length = 254)
    private String travelName;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Size(max = 254)
    @Column(name = "bcp_planned", length = 254)
    private String bcpPlanned;

    @Column(name = "passing_date_planned")
    private LocalDate passingDatePlanned;

    @Size(max = 32)
    @Column(name = "means_of_transport_type", length = 32)
    private String meansOfTransportType;

    @Size(max = 254)
    @Column(name = "means_of_transport_identification", length = 254)
    private String meansOfTransportIdentification;

    @Column(name = "passenger_id")
    private String passengerId;

    @Size(max = 1027)
    @Column(name = "description", length = 1027)
    private String description;

    @Size(max = 32)
    @Column(name = "travel_status", length = 32)
    private String travelStatus;

    @Size(max = 254)
    @Column(name = "document_number", length = 254)
    private String documentNumber;
}