package ro.simavi.odysseus.platform.services;

import org.springframework.stereotype.Service;
import ro.simavi.odysseus.platform.entities.OdsWorkflowInstanceNode;

@Service
public interface OdsWorkflowInstanceNodeService {
    OdsWorkflowInstanceNode saveOrEditOdsWorkflowInstanceNode(OdsWorkflowInstanceNode odsWorkflowInstanceNodeSelected);

    void deleteOdsWorkflowInstanceNode(OdsWorkflowInstanceNode odsWorkflowInstanceNodeSelected);
}
