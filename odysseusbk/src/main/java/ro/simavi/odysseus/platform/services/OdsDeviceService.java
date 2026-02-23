package ro.simavi.odysseus.platform.services;

import org.springframework.stereotype.Service;
import ro.simavi.odysseus.platform.entities.OdsDevice;

@Service
public interface OdsDeviceService {
    OdsDevice saveOrEditOdsDevice(OdsDevice odsDeviceSelected);

    void deleteOdsDevice(OdsDevice odsDeviceSelected);
}
