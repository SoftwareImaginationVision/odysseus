package ro.simavi.odysseus.platform.servicesImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ro.simavi.odysseus.platform.entities.OdsTravel;
import ro.simavi.odysseus.platform.repositories.OdsTravelRepository;
import ro.simavi.odysseus.platform.services.OdsTravelService;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Service
public class OdsTravelServiceImpl implements OdsTravelService {
    private final OdsTravelRepository odsTravelRepository;

    @Autowired
    public OdsTravelServiceImpl(OdsTravelRepository odsTravelRepository) {
        this.odsTravelRepository = odsTravelRepository;
    }

    @Override
    public List<OdsTravel> getAllTravels() {
        return odsTravelRepository.findAll(Sort.by(Sort.Order.asc("id")));
    }

    @Override
    public OdsTravel saveOrEditOdsTravel(OdsTravel odsTravel) {
        if(Objects.isNull(odsTravel.getId()))
            odsTravel.setStartDate(LocalDate.now());
        return odsTravelRepository.save(odsTravel);
    }

    @Override
    public void deleteOdsTravel(OdsTravel odsTravel) {
        odsTravelRepository.delete(odsTravel);
    }
}
