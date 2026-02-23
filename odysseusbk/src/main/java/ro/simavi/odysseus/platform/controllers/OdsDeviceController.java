package ro.simavi.odysseus.platform.controllers;

import lombok.Getter;
import lombok.Setter;
import org.primefaces.PrimeFaces;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;
import ro.simavi.odysseus.platform.entities.OdsDevice;
import ro.simavi.odysseus.platform.repositories.OdsDeviceRepository;
import ro.simavi.odysseus.platform.services.OdsDeviceService;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import java.util.List;
import java.util.Objects;

@Setter
@Getter
@Component
@Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class OdsDeviceController {
    @Autowired
    private OdsDeviceRepository odsDeviceRepository;

    @Autowired
    private OdsDeviceService odsDeviceService;

    private List<OdsDevice> deviceList;

    private OdsDevice odsDeviceSelected;

    private String nameFilter;

    @PostConstruct
    public void init(){
        deviceList = odsDeviceRepository.findAll();
        if(deviceList.isEmpty()){
            odsDeviceRepository.save(new OdsDevice( "123456789012345", "Smartphone", "High-end smartphone", "Mobile", "Units", 100.0, 1000.0));
            odsDeviceRepository.save(new OdsDevice( "234567890123456", "Laptop", "Powerful laptop for gaming", "Computer", "Units", 500.0, 2000.0));
            odsDeviceRepository.save(new OdsDevice( "345678901234567", "Fitness Tracker", "Tracks your daily activity", "Wearable", "Units", 20.0, 200.0));
            odsDeviceRepository.save(new OdsDevice( "456789012345678", "Smart TV", "4K Ultra HD TV with smart features", "TV", "Units", 300.0, 1500.0));
            odsDeviceRepository.save(new OdsDevice( "567890123456789", "Digital Camera", "Professional DSLR camera", "Photography", "Units", 200.0, 2500.0));
            deviceList = odsDeviceRepository.findAll();
        }
    }

    public void saveOdsDevice(){
        if(Objects.nonNull(odsDeviceSelected)){
            OdsDevice odsDevice = odsDeviceService.saveOrEditOdsDevice(odsDeviceSelected);
            deviceList = odsDeviceRepository.findAll();
            PrimeFaces.current().ajax().update("odsDeviceForm:dataTableOdsDevice");
            FacesMessage msg =new FacesMessage("Successfully saved!");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
    }

    public void deleteOdsDevice(){
        if(Objects.nonNull(odsDeviceSelected)){
            odsDeviceService.deleteOdsDevice(odsDeviceSelected);
            deviceList.remove(odsDeviceSelected);
            odsDeviceSelected = null;
            PrimeFaces.current().ajax().update("odsDeviceForm:dataTableOdsDevice");
            FacesMessage msg =new FacesMessage("Successfully deleted!");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
    }

    public void createNewOdsDevice(){
        this.odsDeviceSelected = new OdsDevice();
    }

    public void filter(){
        if(nameFilter != null){
            deviceList = odsDeviceRepository.findAllByDescriptionContainingIgnoreCaseOrDeviceNameContainingIgnoreCaseOrDeviceTypeContainsIgnoreCase(nameFilter, nameFilter, nameFilter);
        }else {
            deviceList = odsDeviceRepository.findAll();
        }
    }

    public void reset(){
        deviceList = odsDeviceRepository.findAll();
        nameFilter = null;
    }
}
