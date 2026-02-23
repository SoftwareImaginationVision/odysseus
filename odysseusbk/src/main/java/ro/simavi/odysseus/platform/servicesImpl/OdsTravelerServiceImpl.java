package ro.simavi.odysseus.platform.servicesImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ro.simavi.odysseus.platform.entities.OdsTraveler;
import ro.simavi.odysseus.platform.repositories.OdsTravelerRepository;
import ro.simavi.odysseus.platform.services.OdsTravelerService;

import java.util.List;
@Service
public class OdsTravelerServiceImpl implements OdsTravelerService {
    @Autowired
    private OdsTravelerRepository odsTravelerRepository;
    @Override
    public OdsTraveler saveOrEditOdsTraveler(OdsTraveler odsTraveler) {
        OdsTraveler save = (OdsTraveler) odsTravelerRepository.save(odsTraveler);
        return save;

    }

    @Override
    public void deleteOdsTraveler(OdsTraveler odsTraveler) {

        odsTravelerRepository.delete(odsTraveler);
    }

    @Override
    public OdsTraveler getTravelerById(Integer id) {

        return odsTravelerRepository.getById(id);

    }

    @Override
    public OdsTraveler getTravelerByPersonalId(String personalid) {
        return odsTravelerRepository.findOdsTravelerByPersonalId(personalid);

    }

    @Override
    public OdsTraveler getTravelerByPEmail(String email) {

        return odsTravelerRepository.findOdsTravelerByEmail(email);

    }

    @Override
    public List<OdsTraveler> getTravelersByName(String name) {
        return odsTravelerRepository.findOdsTravelersByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(name, name);
        //return null;
    }

    @Override
    public List<OdsTraveler> getTravelersByCity(String city) {
        return odsTravelerRepository.findOdsTravelersByCityContainsIgnoreCase(city);

    }
}
