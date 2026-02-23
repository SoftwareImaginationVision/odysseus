package ro.simavi.odysseus.platform.services;

import org.apache.commons.imaging.ImageReadException;
import org.springframework.web.multipart.MultipartFile;
import ro.simavi.odysseus.platform.entities.OdsCarScans;

import java.io.IOException;
import java.util.List;


public interface OdsCarScansService {
    public void processUffFile(MultipartFile uffFile) throws IOException, ImageReadException;
    public List<OdsCarScans> getAllCarScans();
}
