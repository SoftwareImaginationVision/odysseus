package ro.simavi.odysseus.platform.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ro.simavi.odysseus.platform.entities.OdsDrule;

public interface OdsDruleRepository extends JpaRepository<OdsDrule, Integer> {
}