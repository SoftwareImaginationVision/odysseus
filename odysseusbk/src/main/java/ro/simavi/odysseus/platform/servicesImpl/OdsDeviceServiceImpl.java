package ro.simavi.odysseus.platform.servicesImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ro.simavi.odysseus.platform.entities.OdsDevice;
import ro.simavi.odysseus.platform.repositories.OdsDeviceRepository;
import ro.simavi.odysseus.platform.services.OdsDeviceService;

import java.util.Objects;
import java.util.Optional;
import java.util.logging.Logger;

@Service
public class OdsDeviceServiceImpl implements OdsDeviceService {
    @Autowired
    OdsDeviceRepository odsDeviceRepository;

    @Override
    public OdsDevice saveOrEditOdsDevice(OdsDevice odsDeviceSelected){
        if( Objects.nonNull( odsDeviceSelected.getId() ) ){
            Optional<OdsDevice> optionalOdsDevice = this.odsDeviceRepository.findById( odsDeviceSelected.getId());
            if (optionalOdsDevice.isPresent()) {
                optionalOdsDevice.get().setDeviceName(odsDeviceSelected.getDeviceName());
                optionalOdsDevice.get().setDeviceType(odsDeviceSelected.getDeviceType());
                optionalOdsDevice.get().setGuid(odsDeviceSelected.getGuid());
                optionalOdsDevice.get().setDescription(odsDeviceSelected.getDescription());
                optionalOdsDevice.get().setUm(odsDeviceSelected.getUm());
                optionalOdsDevice.get().setMaxValue(odsDeviceSelected.getMaxValue());
                optionalOdsDevice.get().setMinValue(odsDeviceSelected.getMinValue());

                this.odsDeviceRepository.save(optionalOdsDevice.get());

                return optionalOdsDevice.get();
            }
        }

        OdsDevice odsDevice = new OdsDevice();
        odsDevice.setDeviceName(odsDeviceSelected.getDeviceName());
        odsDevice.setDeviceType(odsDeviceSelected.getDeviceType());
        odsDevice.setGuid(odsDeviceSelected.getGuid());
        odsDevice.setDescription(odsDeviceSelected.getDescription());
        odsDevice.setUm(odsDeviceSelected.getUm());
        odsDevice.setMaxValue(odsDeviceSelected.getMaxValue());
        odsDevice.setMinValue(odsDeviceSelected.getMinValue());

        try {
            this.odsDeviceRepository.save(odsDevice);
        }
        catch (Exception dex){
            Logger.getLogger(this.getClass().getName()).warning(odsDevice.getDeviceName()+ " - "+ dex.getMessage());
        }
        return odsDevice;
    }

    @Override
    public void deleteOdsDevice(OdsDevice odsDeviceSelected){
        try {
            this.odsDeviceRepository.deleteById(odsDeviceSelected.getId());
        }
        catch (Exception dex){
            Logger.getLogger(this.getClass().getName()).warning("fail to delete " + odsDeviceSelected.getDeviceName()+ " - "+ dex.getMessage());
        }
    }
}
