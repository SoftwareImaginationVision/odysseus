package ro.simavi.odysseus.platform.views;

import lombok.Getter;
import lombok.Setter;
import org.primefaces.PrimeFaces;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.FilesUploadEvent;
import org.primefaces.model.file.UploadedFile;
import org.primefaces.model.file.UploadedFiles;
import org.primefaces.util.EscapeUtils;
import org.springframework.stereotype.Component;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;

@Component
@ViewScoped
@Getter
@Setter
public class FileUploadView
{

    private UploadedFile file;
    private UploadedFiles files;
    private String dropZoneText = "Drop zone p:inputTextarea demo.";

   // private FileUploadController fileUploadController;

    public void upload()
    {
        if ( file != null )
        {
            //fileUploadController.upload(  );
            FacesMessage message = new FacesMessage( "Successful", file.getFileName() + " is uploaded." );
            FacesContext.getCurrentInstance().addMessage( null, message );
        }
    }

    public void uploadMultiple()
    {
        if ( files != null )
        {
            for ( UploadedFile f : files.getFiles() )
            {
                FacesMessage message = new FacesMessage( "Successful", f.getFileName() + " is uploaded." );
                FacesContext.getCurrentInstance().addMessage( null, message );
            }
        }
    }

    public void handleFileUpload( FileUploadEvent event )
    {
        FacesMessage message = new FacesMessage( "Successful", event.getFile().getFileName() + " is uploaded." );
        FacesContext.getCurrentInstance().addMessage( null, message );
    }

    public void handleFileUploadTextarea( FileUploadEvent event )
    {
        String jsVal = "PF('textarea').jq.val";
        String fileName = EscapeUtils.forJavaScript( event.getFile().getFileName() );
        PrimeFaces.current().executeScript( jsVal + "(" + jsVal + "() + '\\n\\n" + fileName + " uploaded.')" );
    }

    public void handleFilesUpload( FilesUploadEvent event )
    {
        for ( UploadedFile f : event.getFiles().getFiles() )
        {
            FacesMessage message = new FacesMessage( "Successful", f.getFileName() + " is uploaded." );
            FacesContext.getCurrentInstance().addMessage( null, message );
        }
    }
}
