package ro.simavi.odysseus.platform.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import ro.simavi.odysseus.platform.entities.OdsDevice;

import java.util.List;

public interface OdsDeviceRepository extends JpaRepository<OdsDevice, Integer> {
    List<OdsDevice> findAllByDescriptionContainingIgnoreCaseOrDeviceNameContainingIgnoreCaseOrDeviceTypeContainsIgnoreCase(@Param("description") String description, @Param("deviceName") String deviceName, @Param("deviceType") String deviceType);
}