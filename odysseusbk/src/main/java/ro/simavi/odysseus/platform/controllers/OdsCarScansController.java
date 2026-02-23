package ro.simavi.odysseus.platform.controllers;


import lombok.Getter;
import lombok.Setter;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
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
import ro.simavi.odysseus.platform.entities.OdsCar;
import ro.simavi.odysseus.platform.entities.OdsCarScans;
import ro.simavi.odysseus.platform.servicesImpl.OdsCarScansServiceImpl;

import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.Objects;

@RestController
@Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)
@Getter
@Setter
public class OdsCarScansController {


    private final OdsCarScansServiceImpl odsCarScansService;
    private List<OdsCarScans> odsCarScansList;
    private OdsCarScans odsCarScans;
    private byte[] imageFromDB;
    @Autowired
    public OdsCarScansController(OdsCarScansServiceImpl odsCarScansService) {
        this.odsCarScansService = odsCarScansService;
        loadAllCarScans();
    }

    public void loadAllCarScans() {
        odsCarScansList = odsCarScansService.getAllCarScans();
    }
    @PostMapping(value ="/upload-uff", consumes = { "multipart/form-data" })
    public ResponseEntity<String> uploadUffFile(@RequestParam("uffFile") MultipartFile uffFile) {
        try {
            odsCarScansService.processUffFile(uffFile);
            return ResponseEntity.ok("Uff uploaded!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing  : " + e.getMessage());
        }
    }
    public void onPageLoad(){
        odsCarScansList =  odsCarScansService.getAllCarScans();
    }
    public void settingImage(Long carId, int pictureNumber) {
        if (!odsCarScansList.isEmpty()) {
            for (OdsCarScans odsCarScans : odsCarScansList) {
                if (Objects.equals(odsCarScans.getId(), carId)) {
                    switch (pictureNumber) {
                        case 1:
                            imageFromDB = odsCarScans.getPicture1();
                            break;
                        case 2:
                            imageFromDB = odsCarScans.getPicture2();
                            break;
                        case 3:
                            imageFromDB = odsCarScans.getPicture3();
                            break;
                        default:
                            imageFromDB = null;
                    }
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
}
