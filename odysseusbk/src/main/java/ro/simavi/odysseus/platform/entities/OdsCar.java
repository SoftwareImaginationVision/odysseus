package ro.simavi.odysseus.platform.entities;

import lombok.Getter;
import lombok.Setter;
import org.drools.core.io.impl.InputStreamResource;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.*;
import java.time.Instant;
import java.time.OffsetDateTime;

@Getter
@Setter
@Entity
@Table(name = "ods_cars", schema = "odysseus_bk")
public class OdsCar {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Size(max = 128)
    @Column(name = "reg_number", length = 128)
    private String regNumber;

    @Size(max = 128)
    @Column(name = "car_vin", length = 128)
    private String carVin;

    @Size(max = 128)
    @Column(name = "manufacturer", length = 128)
    private String manufacturer;

    @Size(max = 256)
    @Column(name = "type", length = 256)
    private String type;

    @Column(name = "first_reg_date")
    private OffsetDateTime firstRegDate;

    @Size(max = 256)
    @Column(name = "car_owner", length = 256)
    private String carOwner;

    @Size(max = 1024)
    @Column(name = "declared_destination", length = 1024)
    private String declaredDestination;

    @Size(max = 4096)
    @Column(name = "declared_goods", length = 4096)
    private String declaredGoods;

    @Size(max = 4096)
    @Column(name = "info", length = 4096)
    private String info;

    @Column(name = "picture")
    private byte[] picture;

    @Column(name = "scan")
    private byte[] scan;

    @Size(max = 256)
    @Column(name = "driver", length = 256)
    private String driver;

    @Size(max = 128)
    @Column(name = "bcp", length = 128)
    private String bcp;

    @Column(name = "arival_time")
    private Instant arivalTime;



}