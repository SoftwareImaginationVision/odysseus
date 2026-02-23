package ro.simavi.odysseus.platform.services;

import org.springframework.stereotype.Service;
import ro.simavi.odysseus.platform.entities.OdsDevice;
import ro.simavi.odysseus.platform.entities.OdsDocument;

import java.util.List;

@Service
public interface OdsDocumentService {
    OdsDocument saveOrEditOdsDevice(OdsDocument odsDocument);
    void deleteOdsDevice(OdsDocument odsDocument);
    public OdsDocument findOdsDocumentByPersonalId(String personalId);
    public List<OdsDocument> findOdsDocumentsBySurnameEqualsIgnoreCase(String surname);
    public List<OdsDocument> findOdsDocumentsByGivenNameEqualsIgnoreCase(String givenName);
    public List<OdsDocument> findOdsDocumentsByGivenNameEqualsIgnoreCaseAndSurnameEqualsIgnoreCase(String givenName, String surname);
    public List<OdsDocument> findAllBy(OdsDocument odsDocument);

    public OdsDocument addPicture(String personalId, byte[] picture);
    public List<OdsDocument> findOdsDocumentsByGivenNameContainsIgnoreCaseOrSurnameContainsIgnoreCase(String givenName, String surname);

    OdsDocument saveOrEditOdsDocument(OdsDocument odsDocumentSelected);

    void deleteOdsDocument(OdsDocument odsDocumentSelected);

    String verifyDocumentForTrain(OdsDocument odsDocument);
}
