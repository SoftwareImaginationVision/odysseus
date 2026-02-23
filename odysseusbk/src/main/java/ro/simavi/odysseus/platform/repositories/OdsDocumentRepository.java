package ro.simavi.odysseus.platform.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ro.simavi.odysseus.platform.entities.OdsDocument;

import java.util.List;

public interface OdsDocumentRepository extends JpaRepository<OdsDocument, Integer> {
    public OdsDocument findOdsDocumentByPersonalId(String personalId);
    public List<OdsDocument> findOdsDocumentsBySurnameEqualsIgnoreCase(String surname);
    public List<OdsDocument> findOdsDocumentsByGivenNameEqualsIgnoreCase(String givenName);
    public List<OdsDocument> findOdsDocumentsByGivenNameEqualsIgnoreCaseAndSurnameEqualsIgnoreCase(String givenName, String surname);
    //public List<OdsDocument> findAllBy(OdsDocument odsDocument);

    public List<OdsDocument> findOdsDocumentsByGivenNameContainsIgnoreCaseOrSurnameContainsIgnoreCase(String givenName, String surname);

    List<OdsDocument> findAllByTravelerId(Integer travelerId);

    List<OdsDocument> findAllByHasRisk(String hasRisk);

    List<OdsDocument> findOdsDocumentsByGivenNameContainsIgnoreCaseOrSurnameContainsIgnoreCaseAndHasRisk(String givenName, String surname, String hasRisk);

}