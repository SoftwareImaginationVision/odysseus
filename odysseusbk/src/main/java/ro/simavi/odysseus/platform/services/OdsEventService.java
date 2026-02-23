package ro.simavi.odysseus.platform.services;

import org.springframework.stereotype.Service;
import ro.simavi.odysseus.platform.entities.OdsEvent;

@Service
public interface OdsEventService {
    OdsEvent saveOrEditOdsEvent(OdsEvent odsEventSelected);

    void deleteOdsEvent(OdsEvent odsEventSelected);
}
