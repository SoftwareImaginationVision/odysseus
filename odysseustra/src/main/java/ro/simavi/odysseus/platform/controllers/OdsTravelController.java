package ro.simavi.odysseus.platform.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.swagger.v3.oas.annotations.Operation;
import lombok.Getter;
import lombok.Setter;
import org.primefaces.PrimeFaces;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.multipart.MultipartFile;
import ro.simavi.odysseus.platform.entities.OdsTravel;
import ro.simavi.odysseus.platform.entities.OdsTraveler;
import ro.simavi.odysseus.platform.entities.OdsTravelerDto;
import ro.simavi.odysseus.platform.repositories.OdsTravelRepository;
import ro.simavi.odysseus.platform.repositories.OdsTravelerRepository;
import ro.simavi.odysseus.platform.services.UserOidcService;
import ro.simavi.odysseus.platform.servicesImpl.OdsTravelServiceImpl;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

@Setter
@Getter
@Component
@RestController
@Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class OdsTravelController {

    private final OdsTravelServiceImpl odsTravelService;
    private OdsTravel odsTravelSelected;
    private List<OdsTravel> travelList;

    @Autowired
    private UserOidcService userOidcService;

    @Autowired
    private OdsTravelRepository odsTravelRepository;

    @Autowired
    private OdsTravelerRepository odsTravelerRepository;

    @Autowired
    public OdsTravelController(OdsTravelServiceImpl odsTravelService) {
        this.odsTravelService = odsTravelService;
    }

    @PostConstruct
    public void init() {
        travelList = odsTravelService.getAllTravels();
    }
    public void saveOdsTravel() {
        if(Objects.nonNull(odsTravelSelected)) {

            if(odsTravelerRepository.existsByRegisteredUser(userOidcService.getCurrentUserEmail())) {
                OdsTraveler odsTraveler = odsTravelerRepository.findFirstByRegisteredUser(userOidcService.getCurrentUserEmail());
                odsTravelSelected.setPassengerId(odsTraveler.getTuuid());
            }
            odsTravelSelected = odsTravelService.saveOrEditOdsTravel(odsTravelSelected);
            travelList = odsTravelService.getAllTravels();
            PrimeFaces.current().ajax().update("odsTravelForm:dataTableOdsTravel");
            FacesMessage msg = new FacesMessage("Successfully saved!");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
    }
    public void createNewOdsTravel() {
        odsTravelSelected = new OdsTravel();
    }

    public void deleteOdsTravel() {
        if(Objects.nonNull(odsTravelSelected)) {
            odsTravelService.deleteOdsTravel(odsTravelSelected);
            travelList.remove(odsTravelSelected);
            odsTravelSelected = null;
            PrimeFaces.current().ajax().update("odsTravelForm:dataTableOdsTravel");
            FacesMessage msg =new FacesMessage("Successfully deleted!");
            FacesContext.getCurrentInstance().addMessage(null, msg);

        }
    }


}
