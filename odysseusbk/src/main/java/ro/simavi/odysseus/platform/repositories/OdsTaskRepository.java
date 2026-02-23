package ro.simavi.odysseus.platform.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ro.simavi.odysseus.platform.entities.OdsTask;

public interface OdsTaskRepository extends JpaRepository<OdsTask, Integer> {
}