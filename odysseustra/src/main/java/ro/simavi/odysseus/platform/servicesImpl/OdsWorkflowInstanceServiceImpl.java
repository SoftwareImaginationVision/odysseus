package ro.simavi.odysseus.platform.servicesImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ro.simavi.odysseus.platform.entities.OdsWorkflowInstance;
import ro.simavi.odysseus.platform.repositories.OdsWorkflowInstanceRepository;
import ro.simavi.odysseus.platform.services.OdsWorkflowInstanceService;

import java.time.Instant;
import java.util.Objects;
import java.util.Optional;
import java.util.logging.Logger;

@Service
public class OdsWorkflowInstanceServiceImpl implements OdsWorkflowInstanceService {
    @Autowired
    OdsWorkflowInstanceRepository odsWorkflowInstanceRepository;

    @Override
    public OdsWorkflowInstance saveOrEditOdsWorkflowInstance(OdsWorkflowInstance odsWorkflowInstanceSelected){
        if( Objects.nonNull( odsWorkflowInstanceSelected.getId() ) ){
            Optional<OdsWorkflowInstance> optionalOdsWorkflowInstance = this.odsWorkflowInstanceRepository.findById( odsWorkflowInstanceSelected.getId());
            if (optionalOdsWorkflowInstance.isPresent()) {
                optionalOdsWorkflowInstance.get().setWorkflowName(odsWorkflowInstanceSelected.getWorkflowName());
                optionalOdsWorkflowInstance.get().setWorkflowStatus(odsWorkflowInstanceSelected.getWorkflowStatus());
                optionalOdsWorkflowInstance.get().setCreatedOn(odsWorkflowInstanceSelected.getCreatedOn());
                optionalOdsWorkflowInstance.get().setInstanceName(odsWorkflowInstanceSelected.getInstanceName());
                optionalOdsWorkflowInstance.get().setTravelerUuid(odsWorkflowInstanceSelected.getTravelerUuid());

                this.odsWorkflowInstanceRepository.save(optionalOdsWorkflowInstance.get());

                return optionalOdsWorkflowInstance.get();
            }
        }

        OdsWorkflowInstance odsWorkflowInstance = new OdsWorkflowInstance();
        odsWorkflowInstance.setWorkflowName(odsWorkflowInstanceSelected.getWorkflowName());
        odsWorkflowInstance.setWorkflowStatus(odsWorkflowInstanceSelected.getWorkflowStatus());
        odsWorkflowInstance.setInstanceName(odsWorkflowInstanceSelected.getInstanceName());
        odsWorkflowInstance.setCreatedOn(Instant.now());
        odsWorkflowInstance.setTravelerUuid(odsWorkflowInstanceSelected.getTravelerUuid());

        try {
            this.odsWorkflowInstanceRepository.save(odsWorkflowInstance);
        }
        catch (Exception dex){
            Logger.getLogger(this.getClass().getName()).warning(odsWorkflowInstance.getWorkflowName()+ " - "+ dex.getMessage());
        }
        return odsWorkflowInstance;
    }

    @Override
    public void deleteOdsWorkflowInstance(OdsWorkflowInstance odsWorkflowInstanceSelected){
        try {
            this.odsWorkflowInstanceRepository.deleteById(odsWorkflowInstanceSelected.getId());
        }
        catch (Exception dex){
            Logger.getLogger(this.getClass().getName()).warning("fail to delete " + odsWorkflowInstanceSelected.getWorkflowName()+ " - "+ dex.getMessage());
        }
    }
}
