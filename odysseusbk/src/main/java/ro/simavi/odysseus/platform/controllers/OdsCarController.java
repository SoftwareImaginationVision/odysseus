package ro.simavi.odysseus.platform.controllers;

import lombok.Getter;
import lombok.Setter;
import org.drools.core.io.impl.InputStreamResource;
import org.primefaces.PrimeFaces;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.file.UploadedFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;
import ro.simavi.odysseus.platform.entities.OdsCar;
import ro.simavi.odysseus.platform.repositories.OdsCarRepository;
import ro.simavi.odysseus.platform.services.OdsAuditLogService;
import ro.simavi.odysseus.platform.services.OdsCarService;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseId;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Objects;

@Setter
@Getter
@Component
@Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class OdsCarController {
    @Autowired
    private OdsCarRepository odsCarRepository;

    @Autowired
    private OdsCarService odsCarService;

    @Autowired
    private OdsAuditLogService odsAuditLogService;

    private List<OdsCar> carList;

    private OdsCar odsCarSelected;

    private UploadedFile originalImage;
    private byte[] imageFromDB;

    @PostConstruct
    public void init() {
        carList = odsCarRepository.findAll();
    }

    public void cancelOdsCar() {
        this.originalImage = null;
    }
    public void saveOdsCar() {
        if(Objects.nonNull(odsCarSelected)) {
            odsAuditLogService.startCarProcess(this.getClass().getName(), "saveOdsCar", odsCarSelected.getCarVin());
            odsCarSelected.setPicture(originalImage.getContent());
            odsCarSelected = odsCarService.saveOrEditOdsCar(odsCarSelected);
            carList = odsCarRepository.findAll();
            PrimeFaces.current().ajax().update("odsCarForm:dataTableOdsCar");
            FacesMessage msg = new FacesMessage("Succesfully saved!");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            this.originalImage = null;
            odsAuditLogService.endCarProcess(this.getClass().getName(), "saveOdsCar", odsCarSelected.getRegNumber(), odsCarSelected.getCarVin());
        }
    }




    public void handleFileUpload(FileUploadEvent event) {
        this.originalImage = null;
        UploadedFile file = event.getFile();
        if (file != null && file.getContent() != null && file.getContent().length > 0 && file.getFileName() != null) {
            this.originalImage = file;
            FacesMessage msg = new FacesMessage("Successful", this.originalImage.getFileName() + " is uploaded.");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
    }

    public void settingImage(Integer carId) {
        if(!carList.isEmpty()) {
            for (OdsCar odsCar: carList) {
                if(Objects.equals(odsCar.getId(), carId)) {
                    imageFromDB = odsCar.getPicture();
                }
            }
        }
    }
    public StreamedContent getImageDB() {

        return DefaultStreamedContent.builder()
                .contentType("image/jpeg")
                .stream(() -> new ByteArrayInputStream(imageFromDB))
                .build();
    }



    public StreamedContent getImage() {
        return DefaultStreamedContent.builder()
                .contentType(originalImage == null ? null : originalImage.getContentType())
                .stream(() -> {
                    if (originalImage == null
                            || originalImage.getContent() == null
                            || originalImage.getContent().length == 0) {
                        return null;
                    }

                    try {
                        return new ByteArrayInputStream(originalImage.getContent());
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                        return null;
                    }
                })
                .build();
    }

    public void createNewOdsCar() {
        this.odsCarSelected = new OdsCar();
        this.originalImage = null;
    }

    public void deleteOdsCar() {
        if(Objects.nonNull(odsCarSelected)) {
            odsCarService.deleteOdsCar(odsCarSelected);
            carList.remove(odsCarSelected);
            odsCarSelected = null;
            PrimeFaces.current().ajax().update("odsCarForm:dataTableOdsCar");
            FacesMessage msg =new FacesMessage("Successfully deleted!");
            FacesContext.getCurrentInstance().addMessage(null, msg);

        }
    }

}
