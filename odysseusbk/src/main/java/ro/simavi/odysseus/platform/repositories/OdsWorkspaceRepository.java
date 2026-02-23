package ro.simavi.odysseus.platform.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ro.simavi.odysseus.platform.entities.OdsWorkspace;

public interface OdsWorkspaceRepository extends JpaRepository<OdsWorkspace, Integer> {
}