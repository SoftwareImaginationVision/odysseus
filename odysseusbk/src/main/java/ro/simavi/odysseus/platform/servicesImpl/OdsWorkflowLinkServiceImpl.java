package ro.simavi.odysseus.platform.servicesImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ro.simavi.odysseus.platform.entities.OdsWorkflowLink;
import ro.simavi.odysseus.platform.repositories.OdsWorkflowLinkRepository;
import ro.simavi.odysseus.platform.services.OdsWorkflowLinkService;

import java.time.Instant;
import java.util.Objects;
import java.util.Optional;
import java.util.logging.Logger;

@Service
public class OdsWorkflowLinkServiceImpl implements OdsWorkflowLinkService {
    @Autowired
    OdsWorkflowLinkRepository odsWorkflowLinkRepository;

    @Override
    public OdsWorkflowLink saveOrEditOdsWorkflowLink(OdsWorkflowLink odsWorkflowLinkSelected){
        if( Objects.nonNull( odsWorkflowLinkSelected.getId() ) ){
            Optional<OdsWorkflowLink> optionalOdsWorkflowLink = this.odsWorkflowLinkRepository.findById( odsWorkflowLinkSelected.getId());
            if (optionalOdsWorkflowLink.isPresent()) {
                optionalOdsWorkflowLink.get().setLinkName(odsWorkflowLinkSelected.getLinkName());
                optionalOdsWorkflowLink.get().setSourceNode(odsWorkflowLinkSelected.getSourceNode());
                optionalOdsWorkflowLink.get().setDestNode(odsWorkflowLinkSelected.getDestNode());
                optionalOdsWorkflowLink.get().setLogicalOperator(odsWorkflowLinkSelected.getLogicalOperator());
                optionalOdsWorkflowLink.get().setComparedTo(odsWorkflowLinkSelected.getComparedTo());
                optionalOdsWorkflowLink.get().setCreatedOn(odsWorkflowLinkSelected.getCreatedOn());
                optionalOdsWorkflowLink.get().setLinkText(odsWorkflowLinkSelected.getLinkText());
                optionalOdsWorkflowLink.get().setWorkflowId(odsWorkflowLinkSelected.getWorkflowId());

                this.odsWorkflowLinkRepository.save(optionalOdsWorkflowLink.get());

                return optionalOdsWorkflowLink.get();
            }
        }

        OdsWorkflowLink odsWorkflowLink = new OdsWorkflowLink();
        odsWorkflowLink.setLinkName(odsWorkflowLinkSelected.getLinkName());
        odsWorkflowLink.setSourceNode(odsWorkflowLinkSelected.getSourceNode());
        odsWorkflowLink.setDestNode(odsWorkflowLinkSelected.getDestNode());
        odsWorkflowLink.setLogicalOperator(odsWorkflowLinkSelected.getLogicalOperator());
        odsWorkflowLink.setComparedTo(odsWorkflowLinkSelected.getComparedTo());
        odsWorkflowLink.setCreatedOn(Instant.now());
        odsWorkflowLink.setLinkText(odsWorkflowLinkSelected.getLinkText());
        odsWorkflowLink.setWorkflowId(odsWorkflowLinkSelected.getWorkflowId());

        try {
            this.odsWorkflowLinkRepository.save(odsWorkflowLink);
        }
        catch (Exception dex){
            Logger.getLogger(this.getClass().getName()).warning(odsWorkflowLink.getLinkName()+ " - "+ dex.getMessage());
        }
        return odsWorkflowLink;
    }

    @Override
    public void deleteOdsWorkflowLink(OdsWorkflowLink odsWorkflowLinkSelected){
        try {
            this.odsWorkflowLinkRepository.deleteById(odsWorkflowLinkSelected.getId());
        }
        catch (Exception dex){
            Logger.getLogger(this.getClass().getName()).warning("fail to delete " + odsWorkflowLinkSelected.getLinkName()+ " - "+ dex.getMessage());
        }
    }
}
