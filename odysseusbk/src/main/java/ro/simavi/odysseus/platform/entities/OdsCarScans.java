package ro.simavi.odysseus.platform.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "ods_car_scans", schema = "odysseus_bk")
public class OdsCarScans {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "first_car_scan")
    private byte[] picture1;
    @Column(name = "second_car_scan")
    private byte[] picture2;
    @Column(name = "thirs_car_scan")
    private byte[] picture3;
    @Column(name = "number_of_passengers")
    private int numberOfPassengers;


}
