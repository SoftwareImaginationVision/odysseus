package ro.simavi.odysseus.platform.servicesImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ro.simavi.odysseus.platform.entities.OdsNotification;
import ro.simavi.odysseus.platform.repositories.OdsNotificationRepository;
import ro.simavi.odysseus.platform.services.OdsNotificationService;

import java.time.Instant;
import java.util.Objects;
import java.util.Optional;
import java.util.logging.Logger;

@Service
public class OdsNotificationServiceImpl implements OdsNotificationService {
    @Autowired
    OdsNotificationRepository odsNotificationRepository;


    @Override
    public OdsNotification saveOrEditOdsNotification(OdsNotification odsNotificationSelected){
        if( Objects.nonNull( odsNotificationSelected.getId() ) ){
            Optional<OdsNotification> optionalOdsNotification = this.odsNotificationRepository.findById( odsNotificationSelected.getId());
            if (optionalOdsNotification.isPresent()) {
                optionalOdsNotification.get().setShortContent(odsNotificationSelected.getShortContent());
                optionalOdsNotification.get().setFullContent(odsNotificationSelected.getFullContent());
                optionalOdsNotification.get().setSeverity(odsNotificationSelected.getSeverity());
                optionalOdsNotification.get().setCreationDate(odsNotificationSelected.getCreationDate());
                optionalOdsNotification.get().setCreatedBy(odsNotificationSelected.getCreatedBy());
                optionalOdsNotification.get().setApplicationName(odsNotificationSelected.getApplicationName());
                optionalOdsNotification.get().setExpirationDate(odsNotificationSelected.getExpirationDate());
                optionalOdsNotification.get().setNotificationStatus(odsNotificationSelected.getNotificationStatus());
                optionalOdsNotification.get().setTarget(odsNotificationSelected.getTarget());
                optionalOdsNotification.get().setUrl(odsNotificationSelected.getUrl());
                optionalOdsNotification.get().setSourceMissionId(odsNotificationSelected.getSourceMissionId());
                optionalOdsNotification.get().setTaskName(odsNotificationSelected.getTaskName());
                optionalOdsNotification.get().setAreaName(odsNotificationSelected.getAreaName());
                optionalOdsNotification.get().setPosition(odsNotificationSelected.getPosition());


                this.odsNotificationRepository.save(optionalOdsNotification.get());

                return optionalOdsNotification.get();
            }
        }

        OdsNotification odsNotification = new OdsNotification();

        odsNotification.setShortContent(odsNotificationSelected.getShortContent());
        odsNotification.setFullContent(odsNotificationSelected.getFullContent());
        odsNotification.setSeverity(odsNotificationSelected.getSeverity());
        odsNotification.setCreationDate(Instant.now());
        odsNotification.setCreatedBy(odsNotificationSelected.getCreatedBy());
        odsNotification.setApplicationName(odsNotificationSelected.getApplicationName());
        odsNotification.setExpirationDate(Instant.now());
        odsNotification.setNotificationStatus(odsNotificationSelected.getNotificationStatus());
        odsNotification.setTarget(odsNotificationSelected.getTarget());
        odsNotification.setUrl(odsNotificationSelected.getUrl());
        odsNotification.setSourceMissionId(odsNotificationSelected.getSourceMissionId());
        odsNotification.setTaskName(odsNotificationSelected.getTaskName());
        odsNotification.setAreaName(odsNotificationSelected.getAreaName());
        odsNotification.setPosition(odsNotificationSelected.getPosition());

        try {
            this.odsNotificationRepository.save(odsNotification);
        }
        catch (Exception dex){
            Logger.getLogger(this.getClass().getName()).warning(odsNotification.getShortContent()+ " - "+ dex.getMessage());
        }
        return odsNotification;
    }

    @Override
    public void deleteOdsNotification(OdsNotification odsNotificationSelected){
        try {
            this.odsNotificationRepository.deleteById(odsNotificationSelected.getId());
        }
        catch (Exception dex){
            Logger.getLogger(this.getClass().getName()).warning("fail to delete " + odsNotificationSelected.getShortContent()+ " - "+ dex.getMessage());
        }
    }
}
