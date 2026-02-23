package ro.simavi.odysseus.platform.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ro.simavi.odysseus.platform.entities.OdsTravel;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

public interface OdsTravelRepository extends JpaRepository<OdsTravel, Integer> {
    public List<OdsTravel> findOdsTravelsByBcpPlanned(String bcp);

    public List<OdsTravel> findOdsTravelsByBcpPlannedAndPassingDatePlannedBetween(String bcp, Instant ds, Instant df);
    List<OdsTravel> findOdsTravelsByPassengerId(Integer passengerId);
    List<OdsTravel> findOdsTravelsByMeansOfTransportType(String meansOfTransportType);

    List<OdsTravel> findAllByPassingDatePlannedBetween(LocalDate startDate, LocalDate endDate);

}