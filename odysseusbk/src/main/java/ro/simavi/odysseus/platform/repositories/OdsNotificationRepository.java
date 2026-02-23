package ro.simavi.odysseus.platform.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import ro.simavi.odysseus.platform.entities.OdsNotification;

import java.util.List;

public interface OdsNotificationRepository extends JpaRepository<OdsNotification, Integer> {

    List<OdsNotification> findAllByApplicationNameContainingIgnoreCaseOrNotificationStatusContainingIgnoreCaseOrShortContentContainingIgnoreCase(@Param("applicationName") String applicationName, @Param("notificationStatus") String notificationStatus, @Param("shortContent") String shortContent);
}