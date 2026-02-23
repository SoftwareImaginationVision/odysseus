package ro.simavi.odysseus.platform.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ro.simavi.odysseus.platform.entities.DeviceType;

public interface DeviceTypeRepository extends JpaRepository<DeviceType, String> {
}