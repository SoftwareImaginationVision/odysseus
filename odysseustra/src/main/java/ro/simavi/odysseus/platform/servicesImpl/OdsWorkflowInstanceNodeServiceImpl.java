package ro.simavi.odysseus.platform.servicesImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ro.simavi.odysseus.platform.entities.OdsWorkflowInstanceNode;
import ro.simavi.odysseus.platform.repositories.OdsWorkflowInstanceNodeRepository;
import ro.simavi.odysseus.platform.services.OdsWorkflowInstanceNodeService;

import java.time.Instant;
import java.util.Objects;
import java.util.Optional;
import java.util.logging.Logger;

@Service
public class OdsWorkflowInstanceNodeServiceImpl implements OdsWorkflowInstanceNodeService {

    @Autowired
    OdsWorkflowInstanceNodeRepository odsWorkflowInstanceNodeRepository;

    @Override
    public OdsWorkflowInstanceNode saveOrEditOdsWorkflowInstanceNode(OdsWorkflowInstanceNode odsWorkflowInstanceNodeSelected){
        if( Objects.nonNull( odsWorkflowInstanceNodeSelected.getId() ) ){
            Optional<OdsWorkflowInstanceNode> optionalOdsWorkflowInstanceNode = this.odsWorkflowInstanceNodeRepository.findById( odsWorkflowInstanceNodeSelected.getId());
            if (optionalOdsWorkflowInstanceNode.isPresent()) {
                optionalOdsWorkflowInstanceNode.get().setNodeName(odsWorkflowInstanceNodeSelected.getNodeName());
                optionalOdsWorkflowInstanceNode.get().setDruleId(odsWorkflowInstanceNodeSelected.getDruleId());
                optionalOdsWorkflowInstanceNode.get().setDruleParameters(odsWorkflowInstanceNodeSelected.getDruleParameters());
                optionalOdsWorkflowInstanceNode.get().setNodeText(odsWorkflowInstanceNodeSelected.getNodeText());
                optionalOdsWorkflowInstanceNode.get().setDruleName(odsWorkflowInstanceNodeSelected.getDruleName());
                optionalOdsWorkflowInstanceNode.get().setCreatedOn(odsWorkflowInstanceNodeSelected.getCreatedOn());
                optionalOdsWorkflowInstanceNode.get().setWorkflowInstanceId(odsWorkflowInstanceNodeSelected.getWorkflowInstanceId());
                optionalOdsWorkflowInstanceNode.get().setWorkflowInstanceName(odsWorkflowInstanceNodeSelected.getWorkflowInstanceName());
                optionalOdsWorkflowInstanceNode.get().setHitRate(odsWorkflowInstanceNodeSelected.getHitRate());
                optionalOdsWorkflowInstanceNode.get().setNodeStatus(odsWorkflowInstanceNodeSelected.getNodeStatus());
                optionalOdsWorkflowInstanceNode.get().setTravelerUuid(odsWorkflowInstanceNodeSelected.getTravelerUuid());

                this.odsWorkflowInstanceNodeRepository.save(optionalOdsWorkflowInstanceNode.get());

                return optionalOdsWorkflowInstanceNode.get();
            }
        }

        OdsWorkflowInstanceNode odsWorkflowInstanceNode = new OdsWorkflowInstanceNode();
        odsWorkflowInstanceNode.setNodeName(odsWorkflowInstanceNodeSelected.getNodeName());
        odsWorkflowInstanceNode.setDruleId(odsWorkflowInstanceNodeSelected.getDruleId());
        odsWorkflowInstanceNode.setDruleParameters(odsWorkflowInstanceNodeSelected.getDruleParameters());
        odsWorkflowInstanceNode.setNodeText(odsWorkflowInstanceNodeSelected.getNodeText());
        odsWorkflowInstanceNode.setDruleName(odsWorkflowInstanceNodeSelected.getDruleName());
        odsWorkflowInstanceNode.setCreatedOn(Instant.now());
        odsWorkflowInstanceNode.setWorkflowInstanceId(odsWorkflowInstanceNodeSelected.getWorkflowInstanceId());
        odsWorkflowInstanceNode.setWorkflowInstanceName(odsWorkflowInstanceNodeSelected.getWorkflowInstanceName());
        odsWorkflowInstanceNode.setHitRate(odsWorkflowInstanceNodeSelected.getHitRate());
        odsWorkflowInstanceNode.setNodeStatus(odsWorkflowInstanceNodeSelected.getNodeStatus());
        odsWorkflowInstanceNode.setTravelerUuid(odsWorkflowInstanceNodeSelected.getTravelerUuid());

        try {
            this.odsWorkflowInstanceNodeRepository.save(odsWorkflowInstanceNode);
        }
        catch (Exception dex){
            Logger.getLogger(this.getClass().getName()).warning(odsWorkflowInstanceNode.getNodeName()+ " - "+ dex.getMessage());
        }
        return odsWorkflowInstanceNode;
    }

    @Override
    public void deleteOdsWorkflowInstanceNode(OdsWorkflowInstanceNode odsWorkflowInstanceNodeSelected){
        try {
            this.odsWorkflowInstanceNodeRepository.deleteById(odsWorkflowInstanceNodeSelected.getId());
        }
        catch (Exception dex){
            Logger.getLogger(this.getClass().getName()).warning("fail to delete " + odsWorkflowInstanceNodeSelected.getNodeName()+ " - "+ dex.getMessage());
        }
    }
}
