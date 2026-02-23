package ro.simavi.odysseus.platform.services;

import org.springframework.stereotype.Service;
import ro.simavi.odysseus.platform.entities.OdsWorkflow;

@Service
public interface OdsWorkflowService {
    OdsWorkflow saveOrEditOdsWorkflow(OdsWorkflow odsWorkflowSelected);

    void deleteOdsWorkflow(OdsWorkflow odsWorkflowSelected);
}
