package ro.simavi.odysseus.platform.services;

import ro.simavi.odysseus.platform.entities.OdsCar;

public interface OdsCarService {

    OdsCar saveOrEditOdsCar(OdsCar odsCarSelected);

    void deleteOdsCar(OdsCar odsCarSelected);
}
