package ro.simavi.odysseus.platform.services;

import org.springframework.stereotype.Service;
import ro.simavi.odysseus.platform.entities.OdsWorkflowInstanceNode;
import ro.simavi.odysseus.platform.entities.OdsWorkflowNode;

@Service
public interface OdsWorkflowInstanceNodeService {
    OdsWorkflowInstanceNode saveOrEditOdsWorkflowInstanceNode(OdsWorkflowInstanceNode odsWorkflowInstanceNodeSelected);
    void deleteOdsWorkflowInstanceNode(OdsWorkflowInstanceNode odsWorkflowInstanceNodeSelected);
}
