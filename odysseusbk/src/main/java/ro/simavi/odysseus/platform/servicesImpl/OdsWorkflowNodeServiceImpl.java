package ro.simavi.odysseus.platform.servicesImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ro.simavi.odysseus.platform.entities.OdsWorkflowNode;
import ro.simavi.odysseus.platform.repositories.OdsWorkflowNodeRepository;
import ro.simavi.odysseus.platform.services.OdsWorkflowNodeService;

import java.time.Instant;
import java.util.Objects;
import java.util.Optional;
import java.util.logging.Logger;

@Service
public class OdsWorkflowNodeServiceImpl implements OdsWorkflowNodeService {
    @Autowired
    OdsWorkflowNodeRepository odsWorkflowNodeRepository;

    @Override
    public OdsWorkflowNode saveOrEditOdsWorkflowNode(OdsWorkflowNode odsWorkflowNodeSelected){
        if( Objects.nonNull( odsWorkflowNodeSelected.getId() ) ){
            Optional<OdsWorkflowNode> optionalOdsWorkflowNode = this.odsWorkflowNodeRepository.findById( odsWorkflowNodeSelected.getId());
            if (optionalOdsWorkflowNode.isPresent()) {
                optionalOdsWorkflowNode.get().setNodeName(odsWorkflowNodeSelected.getNodeName());
                optionalOdsWorkflowNode.get().setDruleId(odsWorkflowNodeSelected.getDruleId());
                optionalOdsWorkflowNode.get().setDruleParameters(odsWorkflowNodeSelected.getDruleParameters());
                optionalOdsWorkflowNode.get().setNodeText(odsWorkflowNodeSelected.getNodeText());
                optionalOdsWorkflowNode.get().setDruleName(odsWorkflowNodeSelected.getDruleName());
                optionalOdsWorkflowNode.get().setCreatedOn(odsWorkflowNodeSelected.getCreatedOn());
                optionalOdsWorkflowNode.get().setWorkflowId(odsWorkflowNodeSelected.getWorkflowId());
                optionalOdsWorkflowNode.get().setNodeType(odsWorkflowNodeSelected.getNodeType());

                this.odsWorkflowNodeRepository.save(optionalOdsWorkflowNode.get());

                return optionalOdsWorkflowNode.get();
            }
        }

        OdsWorkflowNode odsWorkflowNode = new OdsWorkflowNode();
        odsWorkflowNode.setNodeName(odsWorkflowNodeSelected.getNodeName());
        odsWorkflowNode.setDruleId(odsWorkflowNodeSelected.getDruleId());
        odsWorkflowNode.setDruleParameters(odsWorkflowNodeSelected.getDruleParameters());
        odsWorkflowNode.setNodeText(odsWorkflowNodeSelected.getNodeText());
        odsWorkflowNode.setDruleName(odsWorkflowNodeSelected.getDruleName());
        odsWorkflowNode.setCreatedOn(Instant.now());
        odsWorkflowNode.setWorkflowId(odsWorkflowNodeSelected.getWorkflowId());
        odsWorkflowNode.setNodeType(odsWorkflowNodeSelected.getNodeType());

        try {
            this.odsWorkflowNodeRepository.save(odsWorkflowNode);
        }
        catch (Exception dex){
            Logger.getLogger(this.getClass().getName()).warning(odsWorkflowNode.getNodeName()+ " - "+ dex.getMessage());
        }
        return odsWorkflowNode;
    }

    @Override
    public OdsWorkflowNode editOdsWorkflowNode(OdsWorkflowNode odsWorkflowNodeSelected) throws Exception {
        if (Objects.nonNull(odsWorkflowNodeSelected.getId())) {
            Optional<OdsWorkflowNode> optionalOdsWorkflowNode = this.odsWorkflowNodeRepository.findById(odsWorkflowNodeSelected.getId());
            if (optionalOdsWorkflowNode.isPresent()) {
                OdsWorkflowNode odsWorkflowNode = optionalOdsWorkflowNode.get();
                odsWorkflowNode.setNodeName(odsWorkflowNodeSelected.getNodeName());
                odsWorkflowNode.setDruleId(odsWorkflowNodeSelected.getDruleId());
                odsWorkflowNode.setDruleParameters(odsWorkflowNodeSelected.getDruleParameters());
                odsWorkflowNode.setNodeText(odsWorkflowNodeSelected.getNodeText());
                odsWorkflowNode.setDruleName(odsWorkflowNodeSelected.getDruleName());
                odsWorkflowNode.setCreatedOn(odsWorkflowNodeSelected.getCreatedOn());
                odsWorkflowNode.setWorkflowId(odsWorkflowNodeSelected.getWorkflowId());
                odsWorkflowNode.setNodeType(odsWorkflowNodeSelected.getNodeType());

                this.odsWorkflowNodeRepository.save(odsWorkflowNode);
                return odsWorkflowNode;
            } else {
                throw new Exception("OdsWorkflowNode not found with ID: " + odsWorkflowNodeSelected.getId());
            }
        } else {
            throw new IllegalArgumentException("ID must not be null for editing a node.");
        }
    }


    @Override
    public void deleteOdsWorkflowNode(OdsWorkflowNode odsWorkflowNodeSelected){
        try {
            this.odsWorkflowNodeRepository.deleteById(odsWorkflowNodeSelected.getId());
        }
        catch (Exception dex){
            Logger.getLogger(this.getClass().getName()).warning("fail to delete " + odsWorkflowNodeSelected.getNodeName()+ " - "+ dex.getMessage());
        }
    }
}
