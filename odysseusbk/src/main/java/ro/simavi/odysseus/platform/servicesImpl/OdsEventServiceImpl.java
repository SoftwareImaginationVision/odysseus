package ro.simavi.odysseus.platform.servicesImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ro.simavi.odysseus.platform.entities.OdsEvent;
import ro.simavi.odysseus.platform.repositories.OdsEventRepository;
import ro.simavi.odysseus.platform.services.OdsEventService;

import java.time.Instant;
import java.util.Objects;
import java.util.Optional;
import java.util.logging.Logger;

@Service
public class OdsEventServiceImpl implements OdsEventService {
    @Autowired
    private OdsEventRepository odsEventRepository;

    @Override
    public OdsEvent saveOrEditOdsEvent(OdsEvent odsEventSelected){
        if( Objects.nonNull( odsEventSelected.getId() ) ){
            Optional<OdsEvent> optionalOdsEvent = this.odsEventRepository.findById( odsEventSelected.getId());
            if (optionalOdsEvent.isPresent()) {
                OdsEvent odsEvent = optionalOdsEvent.get();

                copyOdsEvent(odsEventSelected,odsEvent);

                this.odsEventRepository.save(odsEvent);

                return optionalOdsEvent.get();
            }
        }

        OdsEvent odsEvent = new OdsEvent();

        copyOdsEvent(odsEventSelected,odsEvent);

        try {
            this.odsEventRepository.save(odsEvent);
        }
        catch (Exception dex){
            Logger.getLogger(this.getClass().getName()).warning(odsEvent.getEvent()+ " - "+ dex.getMessage());
        }
        return odsEvent;
    }

    @Override
    public void deleteOdsEvent(OdsEvent odsEventSelected){
        try {
            this.odsEventRepository.deleteById(odsEventSelected.getId());
        }
        catch (Exception dex){
            Logger.getLogger(this.getClass().getName()).warning("fail to delete " + odsEventSelected.getEvent()+ " - "+ dex.getMessage());
        }
    }

    private void copyOdsEvent(OdsEvent odsEventSelected, OdsEvent odsEvent){
        odsEvent.setEvent(odsEventSelected.getEvent());
        odsEvent.setEventSource(odsEventSelected.getEventSource());
        odsEvent.setEventType(odsEventSelected.getEventType());
        odsEvent.setCriticality(odsEventSelected.getCriticality());
        odsEvent.setDirection(odsEventSelected.getDirection());
        odsEvent.setLatitude(odsEventSelected.getLatitude());
        odsEvent.setLongitude(odsEventSelected.getLongitude());
        odsEvent.setSequence(odsEventSelected.getSequence());
        odsEvent.setStartDate(Instant.now());
        odsEvent.setSeverity(odsEventSelected.getSeverity());
        odsEvent.setStatus(odsEventSelected.getStatus());
        odsEvent.setSpeed(odsEventSelected.getSpeed());
        odsEvent.setEndDate(Instant.now());
        odsEvent.setSpecificArea(odsEventSelected.getSpecificArea());
        odsEvent.setApproachingTarget(odsEventSelected.getApproachingTarget());
        odsEvent.setDetectedTarget(odsEventSelected.getDetectedTarget());
    }
}
