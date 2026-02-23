package ro.simavi.odysseus.platform.controllers;

import lombok.Getter;
import lombok.Setter;
import org.primefaces.PrimeFaces;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;
import ro.simavi.odysseus.platform.entities.OdsEvent;
import ro.simavi.odysseus.platform.repositories.OdsEventRepository;
import ro.simavi.odysseus.platform.services.OdsEventService;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import java.util.List;
import java.util.Objects;

@Setter
@Getter
@Component
@Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class OdsEventController {
    @Autowired
    private OdsEventService odsEventService;

    @Autowired
    private OdsEventRepository odsEventRepository;

    private List<OdsEvent> odsEventsList;

    private OdsEvent odsEventSelected;

    private String nameFilter;

    @PostConstruct
    public void init(){
        odsEventsList = odsEventRepository.findAll();
        if(odsEventsList.isEmpty()){
            odsEventRepository.save(new OdsEvent(
                    "Earthquake",
                    "Active",
                    "34.0522",
                    "-118.2437",
                    "Fault Line",
                    "Los Angeles",
                    "North",
                    "Yes",
                    "Seismic Center",
                    "50",
                    "Natural Disaster",
                    5,
                    1,
                    "High"
            ));

            odsEventRepository.save(new OdsEvent(
                    "Fire Outbreak",
                    "Active",
                    "40.7128",
                    "-74.0060",
                    "Building",
                    "New York",
                    "South",
                    "No",
                    "Fire Department",
                    "25",
                    "Accident",
                    3,
                    2,
                    "Medium"
            ));

            odsEventRepository.save(new OdsEvent(
                    "Flood",
                    "Active",
                    "29.9511",
                    "-90.0715",
                    "River Overflow",
                    "New Orleans",
                    "East",
                    "Yes",
                    "Environmental Agency",
                    "30",
                    "Natural Disaster",
                    4,
                    3,
                    "High"
            ));

            odsEventRepository.save(new OdsEvent(
                    "Traffic Accident",
                    "Active",
                    "41.8781",
                    "-87.6298",
                    "Intersection",
                    "Chicago",
                    "West",
                    "No",
                    "Police Department",
                    "15",
                    "Accident",
                    2,
                    4,
                    "Low"
            ));

            odsEventRepository.save(new OdsEvent(
                    "Power Outage",
                    "Active",
                    "37.7749",
                    "-122.4194",
                    "Power Station Failure",
                    "San Francisco",
                    "South",
                    "No",
                    "Utility Company",
                    "20",
                    "Infrastructure",
                    3,
                    5,
                    "Medium"
            ));

            odsEventRepository.save(new OdsEvent(
                    "Gas Leak",
                    "Active",
                    "32.7767",
                    "-96.7970",
                    "Underground Pipeline",
                    "Dallas",
                    "North",
                    "Yes",
                    "Gas Company",
                    "35",
                    "Environmental Hazard",
                    4,
                    6,
                    "High"
            ));

            odsEventRepository.save(new OdsEvent(
                    "Medical Emergency",
                    "Active",
                    "42.3601",
                    "-71.0589",
                    "Hospital Incident",
                    "Boston",
                    "East",
                    "No",
                    "Emergency Services",
                    "10",
                    "Medical",
                    1,
                    7,
                    "Low"
            ));

            odsEventRepository.save(new OdsEvent(
                    "Wildfire",
                    "Active",
                    "34.0522",
                    "-118.2437",
                    "Vegetation Fire",
                    "Los Angeles",
                    "West",
                    "Yes",
                    "Fire Department",
                    "40",
                    "Natural Disaster",
                    5,
                    8,
                    "High"
            ));

            odsEventRepository.save(new OdsEvent(
                    "Airport Security Threat",
                    "Active",
                    "33.9416",
                    "-118.4085",
                    "Airport Incident",
                    "Los Angeles",
                    "South",
                    "Yes",
                    "Airport Security",
                    "30",
                    "Security",
                    4,
                    9,
                    "Medium"
            ));

            odsEventRepository.save(new OdsEvent("Train Derailment",
                    "Active",
                    "40.7128",
                    "-74.0060",
                    "Railway Accident",
                    "New York",
                    "North",
                    "No",
                    "Transportation Authority",
                    "25",
                    "Accident",
                    3,
                    10,
                    "Medium"
            ));

            odsEventsList = odsEventRepository.findAll();
        }

    }

    public void saveOdsEvent(){
        if(Objects.nonNull(odsEventSelected)){
            OdsEvent odsEvent = odsEventService.saveOrEditOdsEvent(odsEventSelected);
            odsEventsList = odsEventRepository.findAll();
            PrimeFaces.current().ajax().update("odsEventForm:dataTableOdsEvent");
            FacesMessage msg =new FacesMessage("Successfully saved!");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
    }

    public void deleteOdsEvent(){
        if(Objects.nonNull(odsEventSelected)){
            odsEventService.deleteOdsEvent(odsEventSelected);
            odsEventsList.remove(odsEventSelected);
            odsEventSelected = null;
            PrimeFaces.current().ajax().update("odsEventForm:dataTableOdsEvent");
            FacesMessage msg =new FacesMessage("Successfully deleted!");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
    }

    public void createNewOdsEvent(){
        this.odsEventSelected = new OdsEvent();
    }

    public void filter(){
        if(nameFilter != null){
            odsEventsList = odsEventRepository.findAllByEventContainingIgnoreCaseOrStatusContainingIgnoreCaseOrEventSourceContainingIgnoreCase(nameFilter, nameFilter, nameFilter);
        }else {
            odsEventsList = odsEventRepository.findAll();
        }
    }

    public void reset(){
        odsEventsList = odsEventRepository.findAll();
        nameFilter = null;
    }

}
