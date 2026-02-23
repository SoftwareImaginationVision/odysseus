package ro.simavi.odysseus.platform.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ro.simavi.odysseus.platform.entities.OdsFile;

public interface OdsFileRepository extends JpaRepository<OdsFile, Integer> {
}