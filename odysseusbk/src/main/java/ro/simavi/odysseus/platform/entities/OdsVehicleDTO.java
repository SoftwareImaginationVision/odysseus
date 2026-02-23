package ro.simavi.odysseus.platform.entities;

import javax.persistence.Column;
import javax.validation.constraints.Size;
import java.time.Instant;
import java.time.OffsetDateTime;

public class OdsVehicleDTO {
    private String regNumber;
    private String carVin;
    private String manufacturer;
    private String type;
    private OffsetDateTime firstRegDate;
    private String carOwner;
    private String declaredDestination;
    private String declaredGoods;
    private String info;
    private String picture;
    private String scan;
    private String driver;
    private String bcp;
    private Instant arivalTime;

    public OdsVehicleDTO() {
    }

    public OdsVehicleDTO(String regNumber, String carVin, String manufacturer, String type, OffsetDateTime firstRegDate, String carOwner, String declaredDestination, String declaredGoods, String info, String picture, String scan, String driver, String bcp, Instant arivalTime) {
        this.regNumber = regNumber;
        this.carVin = carVin;
        this.manufacturer = manufacturer;
        this.type = type;
        this.firstRegDate = firstRegDate;
        this.carOwner = carOwner;
        this.declaredDestination = declaredDestination;
        this.declaredGoods = declaredGoods;
        this.info = info;
        this.picture = picture;
        this.scan = scan;
        this.driver = driver;
        this.bcp = bcp;
        this.arivalTime = arivalTime;
    }
}
