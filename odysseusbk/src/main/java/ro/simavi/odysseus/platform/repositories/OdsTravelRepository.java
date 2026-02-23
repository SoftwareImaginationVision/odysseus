package ro.simavi.odysseus.platform.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import ro.simavi.odysseus.platform.entities.OdsTravel;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

public interface OdsTravelRepository extends JpaRepository<OdsTravel, Integer> {
    public List<OdsTravel> findOdsTravelsByBcpPlanned(String bcp);

    public List<OdsTravel> findOdsTravelsByBcpPlannedAndPassingDatePlannedBetween(String bcp, LocalDate ds, LocalDate df);

    List<OdsTravel> findAllByPassingDatePlannedBetween(LocalDate startDate, LocalDate endDate);

    List<OdsTravel> findAllByPassingDatePlannedEquals(LocalDate localDate);

    List<OdsTravel> findAllByMeansOfTransportIdentification(@Param("means_of_transport_identification") String meansOfTransportIdentification);

    boolean existsOdsTravelByPassengerId(@Param("passenger_id") String passengerId);

    List<OdsTravel> findAllByPassengerId(@Param("passenger_id") String passengerId);

    boolean existsOdsTravelById(@Param("id") Integer id);

}