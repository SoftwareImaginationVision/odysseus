package ro.simavi.odysseus.platform.services;

import org.springframework.stereotype.Service;
import ro.simavi.odysseus.platform.entities.OdsWorkflowNode;

@Service
public interface OdsWorkflowNodeService {
    OdsWorkflowNode saveOrEditOdsWorkflowNode(OdsWorkflowNode odsWorkflowNodeSelected);

    void deleteOdsWorkflowNode(OdsWorkflowNode odsWorkflowNodeSelected);
}
