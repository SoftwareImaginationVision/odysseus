package ro.simavi.odysseus.platform.servicesImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ro.simavi.odysseus.platform.entities.OdsUser;
import ro.simavi.odysseus.platform.repositories.OdsUserRepository;
import ro.simavi.odysseus.platform.services.OdsUserService;

import java.time.Instant;
import java.util.Objects;
import java.util.Optional;
import java.util.logging.Logger;

@Service
public class OdsUserServiceImpl implements OdsUserService {

    @Autowired
    private OdsUserRepository odsUserRepository;

    @Override
    public OdsUser saveOrEditOdsUser(OdsUser odsUserSelected){
        if( Objects.nonNull( odsUserSelected.getId() ) ){
            Optional<OdsUser> optionalOdsUser = this.odsUserRepository.findById( odsUserSelected.getId());
            if (optionalOdsUser.isPresent()) {
                optionalOdsUser.get().setUserName(odsUserSelected.getUserName());
                optionalOdsUser.get().setStatus(odsUserSelected.getStatus());
                optionalOdsUser.get().setEmail(odsUserSelected.getEmail());
                optionalOdsUser.get().setCreatedOn(odsUserSelected.getCreatedOn());

                this.odsUserRepository.save(optionalOdsUser.get());

                return optionalOdsUser.get();
            }
        }

        OdsUser odsUser = new OdsUser();
        odsUser.setUserName(odsUserSelected.getUserName());
        odsUser.setStatus(odsUserSelected.getStatus());
        odsUser.setEmail(odsUserSelected.getEmail());
        odsUser.setCreatedOn(Instant.now());

        try {
            this.odsUserRepository.save(odsUser);
        }
        catch (Exception dex){
            Logger.getLogger(this.getClass().getName()).warning(odsUser.getUserName()+ " - "+ dex.getMessage());
        }
        return odsUser;
    }

    @Override
    public void deleteOdsUser(OdsUser odsUserSelected){
        try {
            this.odsUserRepository.deleteById(odsUserSelected.getId());
        }
        catch (Exception dex){
            Logger.getLogger(this.getClass().getName()).warning("fail to delete " + odsUserSelected.getUserName()+ " - "+ dex.getMessage());
        }
    }

}
