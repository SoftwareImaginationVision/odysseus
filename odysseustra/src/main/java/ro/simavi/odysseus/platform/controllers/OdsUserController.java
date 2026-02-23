//package ro.simavi.odysseus.platform.controllers;
//
//import lombok.Getter;
//import lombok.Setter;
//import org.primefaces.PrimeFaces;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Scope;
//import org.springframework.context.annotation.ScopedProxyMode;
//import org.springframework.stereotype.Component;
//import org.springframework.web.context.WebApplicationContext;
//import ro.simavi.odysseus.platform.entities.OdsUser;
//import ro.simavi.odysseus.platform.repositories.OdsUserRepository;
//import ro.simavi.odysseus.platform.services.OdsUserService;
//
//import javax.annotation.PostConstruct;
//import javax.faces.application.FacesMessage;
//import javax.faces.context.FacesContext;
//import java.time.Instant;
//import java.util.List;
//import java.util.Objects;
//
//@Setter
//@Getter
//@Component
//@Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)
//public class OdsUserController {
//    @Autowired
//    private OdsUserRepository odsUserRepository;
//
//    @Autowired
//    private OdsUserService odsUserService;
//
//    private List<OdsUser> listPassengers;
//
//    private OdsUser odsUserSelected;
//
//    private String nameFilter;
//
//    @PostConstruct
//    public void init(){
//        listPassengers = odsUserRepository.findAll();
//        if(listPassengers.isEmpty()){
//            odsUserRepository.save(new OdsUser( "John Doe", "Active", "john.doe@example.com", Instant.now()));
//            odsUserRepository.save(new OdsUser( "Jane Smith", "Active", "jane.smith@example.com", Instant.now()));
//            odsUserRepository.save(new OdsUser( "Bob Johnson", "Active", "bob.johnson@example.com", Instant.now()));
//            odsUserRepository.save(new OdsUser( "Alice Brown", "Active", "alice.brown@example.com", Instant.now()));
//            odsUserRepository.save(new OdsUser( "Charlie Wilson", "Active", "charlie.wilson@example.com", Instant.now()));
//            odsUserRepository.save(new OdsUser( "Eva Davis", "Active", "eva.davis@example.com", Instant.now()));
//            odsUserRepository.save(new OdsUser( "Frank Miller", "Active", "frank.miller@example.com", Instant.now()));
//            odsUserRepository.save(new OdsUser( "Grace Lee", "Active", "grace.lee@example.com", Instant.now()));
//            odsUserRepository.save(new OdsUser( "Henry Turner", "Active", "henry.turner@example.com", Instant.now()));
//            odsUserRepository.save(new OdsUser( "Ivy Clark", "Active", "ivy.clark@example.com", Instant.now()));
//            listPassengers = odsUserRepository.findAll();
//        }
//    }
//
//    public void saveOdsUser(){
//        if(Objects.nonNull(odsUserSelected)){
//            OdsUser odsUser = odsUserService.saveOrEditOdsUser(odsUserSelected);
//            listPassengers = odsUserRepository.findAll();
//            PrimeFaces.current().ajax().update("odsUserForm:dataTableOdsUser");
//            FacesMessage msg =new FacesMessage("Successfully saved!");
//            FacesContext.getCurrentInstance().addMessage(null, msg);
//        }
//    }
//
//    public void deleteOdsUser(){
//        if(Objects.nonNull(odsUserSelected)){
//            odsUserService.deleteOdsUser(odsUserSelected);
//            listPassengers.remove(odsUserSelected);
//            odsUserSelected = null;
//            PrimeFaces.current().ajax().update("odsUserForm:dataTableOdsUser");
//            FacesMessage msg =new FacesMessage("Successfully deleted!");
//            FacesContext.getCurrentInstance().addMessage(null, msg);
//        }
//    }
//
//    public void createNewOdsUser(){
//        this.odsUserSelected = new OdsUser();
//    }
//
//    public void filter(){
//        if(nameFilter != null){
//            listPassengers = odsUserRepository.findAllByUserNameContainingIgnoreCaseOrStatusContainingIgnoreCaseOrEmailContainingIgnoreCase(nameFilter, nameFilter, nameFilter);
//        }else {
//            listPassengers = odsUserRepository.findAll();
//        }
//    }
//
//    public void reset(){
//        listPassengers = odsUserRepository.findAll();
//        nameFilter = null;
//    }
//
//
//}
