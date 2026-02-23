package ro.simavi.odysseus.platform.services;

import org.springframework.stereotype.Service;
import ro.simavi.odysseus.platform.entities.OdsEvent;
import ro.simavi.odysseus.platform.entities.OdsTraveler;

import java.util.List;

@Service
public interface OdsTravelerService {
    OdsTraveler saveOrEditOdsTraveler(OdsTraveler odsTraveler);

    void deleteOdsTraveler(OdsTraveler odsTraveler);

    OdsTraveler getTravelerById(Integer id);

    OdsTraveler getTravelerByPersonalId(String personalid);

    OdsTraveler getTravelerByPEmail(String email);

    List<OdsTraveler> getTravelersByName(String name);

    List<OdsTraveler> getTravelersByCity(String city);

}
