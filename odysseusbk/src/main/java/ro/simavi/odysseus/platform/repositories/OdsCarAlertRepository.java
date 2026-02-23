package ro.simavi.odysseus.platform.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import ro.simavi.odysseus.platform.entities.OdsCarAlert;

import java.util.List;

public interface OdsCarAlertRepository extends JpaRepository<OdsCarAlert, Integer> {

}
