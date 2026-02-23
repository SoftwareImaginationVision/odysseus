package ro.simavi.odysseus.platform.servicesImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ro.simavi.odysseus.platform.entities.OdsCar;
import ro.simavi.odysseus.platform.entities.OdsDevice;
import ro.simavi.odysseus.platform.repositories.OdsCarRepository;
import ro.simavi.odysseus.platform.services.OdsCarService;

import java.util.Objects;
import java.util.Optional;
import java.util.logging.Logger;

@Service
public class OdsCarServiceImpl implements OdsCarService {

    @Autowired
    private OdsCarRepository odsCarRepository;
    @Override
    public OdsCar saveOrEditOdsCar(OdsCar odsCarSelected) {
        if( Objects.nonNull( odsCarSelected.getId() ) ){
            Optional<OdsCar> optionalOdsCar = this.odsCarRepository.findById( odsCarSelected.getId());
            if (optionalOdsCar.isPresent()) {
                optionalOdsCar.get().setRegNumber(odsCarSelected.getRegNumber());
                optionalOdsCar.get().setCarVin(odsCarSelected.getCarVin());
                optionalOdsCar.get().setManufacturer(odsCarSelected.getManufacturer());
                optionalOdsCar.get().setType(odsCarSelected.getType());
                optionalOdsCar.get().setFirstRegDate(odsCarSelected.getFirstRegDate());
                optionalOdsCar.get().setCarOwner(odsCarSelected.getCarOwner());
                optionalOdsCar.get().setDeclaredDestination(odsCarSelected.getDeclaredDestination());
                optionalOdsCar.get().setDeclaredGoods(odsCarSelected.getDeclaredGoods());
                optionalOdsCar.get().setInfo(odsCarSelected.getInfo());
                optionalOdsCar.get().setPicture(odsCarSelected.getPicture());
                optionalOdsCar.get().setScan(odsCarSelected.getScan());
                optionalOdsCar.get().setDriver(odsCarSelected.getDriver());
                optionalOdsCar.get().setBcp(odsCarSelected.getBcp());
                optionalOdsCar.get().setArivalTime(odsCarSelected.getArivalTime());


                this.odsCarRepository.save(optionalOdsCar.get());

                return optionalOdsCar.get();
            }
        }

        OdsCar odsCar = new OdsCar();
        odsCar.setRegNumber(odsCarSelected.getRegNumber());
        odsCar.setCarVin(odsCarSelected.getCarVin());
        odsCar.setManufacturer(odsCarSelected.getManufacturer());
        odsCar.setType(odsCarSelected.getType());
        odsCar.setFirstRegDate(odsCarSelected.getFirstRegDate());
        odsCar.setCarOwner(odsCarSelected.getCarOwner());
        odsCar.setDeclaredDestination(odsCarSelected.getDeclaredDestination());
        odsCar.setDeclaredGoods(odsCarSelected.getDeclaredGoods());
        odsCar.setInfo(odsCarSelected.getInfo());
        odsCar.setPicture(odsCarSelected.getPicture());
        odsCar.setScan(odsCarSelected.getScan());
        odsCar.setDriver(odsCarSelected.getDriver());
        odsCar.setBcp(odsCarSelected.getBcp());
        odsCar.setArivalTime(odsCarSelected.getArivalTime());

        try {
            this.odsCarRepository.save(odsCar);
        }
        catch (Exception dex){
            Logger.getLogger(this.getClass().getName()).warning(odsCar.getRegNumber()+ " - "+ dex.getMessage());
        }
        return odsCar;
    }

    @Override
    public void deleteOdsCar(OdsCar odsCarSelected) {
        try {
            this.odsCarRepository.deleteById(odsCarSelected.getId());
        }
        catch (Exception dex){
            Logger.getLogger(this.getClass().getName()).warning("fail to delete " + odsCarSelected.getRegNumber()+ " - "+ dex.getMessage());
        }

    }
}




