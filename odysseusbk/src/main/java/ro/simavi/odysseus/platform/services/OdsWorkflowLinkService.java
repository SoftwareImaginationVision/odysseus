package ro.simavi.odysseus.platform.services;

import org.springframework.stereotype.Service;
import ro.simavi.odysseus.platform.entities.OdsWorkflowLink;

@Service
public interface OdsWorkflowLinkService {
    OdsWorkflowLink saveOrEditOdsWorkflowLink(OdsWorkflowLink odsWorkflowLinkSelected);

    void deleteOdsWorkflowLink(OdsWorkflowLink odsWorkflowLinkSelected);
}
