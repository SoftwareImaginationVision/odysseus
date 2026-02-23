package ro.simavi.odysseus.platform.services;

import ro.simavi.odysseus.platform.entities.OdsTravel;
import ro.simavi.odysseus.platform.entities.OdsTraveler;

import java.util.List;

public interface OdsTravelService {
    public List<OdsTravel> getAllTravels();
    OdsTravel saveOrEditOdsTravel(OdsTravel odsTravel);
    void deleteOdsTravel(OdsTravel odsTravel);
}
