package ro.simavi.odysseus.platform.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ro.simavi.odysseus.platform.entities.OdsUserDefaultWorkspace;

public interface OdsUserDefaultWorkspaceRepository extends JpaRepository<OdsUserDefaultWorkspace, Integer> {
}