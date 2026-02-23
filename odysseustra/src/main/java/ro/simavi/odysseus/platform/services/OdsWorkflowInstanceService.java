package ro.simavi.odysseus.platform.services;

import org.springframework.stereotype.Service;
import ro.simavi.odysseus.platform.entities.OdsWorkflowInstance;

@Service
public interface OdsWorkflowInstanceService {
    OdsWorkflowInstance saveOrEditOdsWorkflowInstance(OdsWorkflowInstance odsWorkflowInstanceSelected);

    void deleteOdsWorkflowInstance(OdsWorkflowInstance odsWorkflowInstanceSelected);
}
