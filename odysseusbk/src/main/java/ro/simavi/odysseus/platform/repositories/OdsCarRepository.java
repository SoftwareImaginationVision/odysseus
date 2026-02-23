package ro.simavi.odysseus.platform.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ro.simavi.odysseus.platform.entities.OdsCar;

import java.time.Instant;
import java.util.List;
public interface OdsCarRepository extends JpaRepository<OdsCar, Integer> {
    public List<OdsCar> findOdsCarByManufacturer(String manufacturer);
    public  List<OdsCar> findOdsCarByDeclaredDestination(String destination);
    public OdsCar findOdsCarByRegNumber(String regNumber);
    public OdsCar findOdsCarByCarVin(String vin);

    public  List<OdsCar> findOdsCarByBcp(String bcp);
    public List<OdsCar> findOdsCarByArivalTimeBetween(Instant from, Instant to);

}