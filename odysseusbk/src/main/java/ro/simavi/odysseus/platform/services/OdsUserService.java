package ro.simavi.odysseus.platform.services;

import org.springframework.stereotype.Service;
import ro.simavi.odysseus.platform.entities.OdsUser;
@Service
public interface OdsUserService {
    OdsUser saveOrEditOdsUser(OdsUser odsUserSelected);

    void deleteOdsUser(OdsUser odsUserSelected);
}
