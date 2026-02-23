package ro.simavi.odysseus.platform.views;

import lombok.Getter;
import lombok.Setter;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

@Getter
@Setter
@Component
@Scope("session")
public class FileDownloadView {

    private StreamedContent file;

    @Value("${mescobrad.plugin.folder}")
    private String fileSource="/tmp/";

    public FileDownloadView() {
        File file1 = new File(fileSource+"cnxf9.jpg");
        //InputStream stream = null;
        try
        {
            InputStream stream = new FileInputStream(file1);
            file = DefaultStreamedContent.builder()
                    .name("downloaded_boromir.jpg")
                    .contentType("image/jpg")
                    .stream(() -> stream)
                    .build();
        }
        catch ( FileNotFoundException e )
        {
            e.printStackTrace();
        }

        // InputStream stream = this.getClass().getResourceAsStream(fileSource+"cnxf9.jpg");
      //  file = new DefaultStreamedContent(stream, "image/jpg", "downloaded_primefaces.jpg");


    }

    public StreamedContent getFile() {
        return file;
    }
}
