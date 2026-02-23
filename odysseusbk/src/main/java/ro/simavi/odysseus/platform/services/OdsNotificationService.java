package ro.simavi.odysseus.platform.services;

import org.springframework.stereotype.Service;
import ro.simavi.odysseus.platform.entities.OdsNotification;

@Service
public interface OdsNotificationService {
    OdsNotification saveOrEditOdsNotification(OdsNotification odsNotificationSelected);

    void deleteOdsNotification(OdsNotification odsNotificationSelected);
}
