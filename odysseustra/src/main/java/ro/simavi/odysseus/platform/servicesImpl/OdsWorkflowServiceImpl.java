package ro.simavi.odysseus.platform.servicesImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ro.simavi.odysseus.platform.entities.OdsWorkflow;
import ro.simavi.odysseus.platform.repositories.OdsWorkflowRepository;
import ro.simavi.odysseus.platform.services.OdsWorkflowService;

import java.time.Instant;
import java.util.Objects;
import java.util.Optional;
import java.util.logging.Logger;

@Service
public class OdsWorkflowServiceImpl implements OdsWorkflowService {
    @Autowired
    OdsWorkflowRepository odsWorkflowRepository;

    @Override
    public OdsWorkflow saveOrEditOdsWorkflow(OdsWorkflow odsWorkflowSelected){
        if( Objects.nonNull( odsWorkflowSelected.getId() ) ){
            Optional<OdsWorkflow> optionalOdsWorkflow = this.odsWorkflowRepository.findById( odsWorkflowSelected.getId());
            if (optionalOdsWorkflow.isPresent()) {
                optionalOdsWorkflow.get().setWorkflowName(odsWorkflowSelected.getWorkflowName());
                optionalOdsWorkflow.get().setCreationDate(odsWorkflowSelected.getCreationDate());
                optionalOdsWorkflow.get().setInstanceDuration(odsWorkflowSelected.getInstanceDuration());

                this.odsWorkflowRepository.save(optionalOdsWorkflow.get());

                return optionalOdsWorkflow.get();
            }
        }

        OdsWorkflow odsWorkflow = new OdsWorkflow();
        odsWorkflow.setWorkflowName(odsWorkflowSelected.getWorkflowName());
        odsWorkflow.setInstanceDuration(odsWorkflowSelected.getInstanceDuration());
        odsWorkflow.setCreationDate(Instant.now());

        try {
            this.odsWorkflowRepository.save(odsWorkflow);
        }
        catch (Exception dex){
            Logger.getLogger(this.getClass().getName()).warning(odsWorkflow.getWorkflowName()+ " - "+ dex.getMessage());
        }
        return odsWorkflow;
    }

    @Override
    public void deleteOdsWorkflow(OdsWorkflow odsWorkflowSelected){
        try {
            this.odsWorkflowRepository.deleteById(odsWorkflowSelected.getId());
        }
        catch (Exception dex){
            Logger.getLogger(this.getClass().getName()).warning("fail to delete " + odsWorkflowSelected.getWorkflowName()+ " - "+ dex.getMessage());
        }
    }

}
