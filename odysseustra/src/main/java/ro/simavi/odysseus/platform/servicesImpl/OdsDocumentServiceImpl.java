package ro.simavi.odysseus.platform.servicesImpl;

import org.kie.api.runtime.KieSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ro.simavi.odysseus.platform.controllers.WorkflowInstanceController;
import ro.simavi.odysseus.platform.entities.OdsDocument;
import ro.simavi.odysseus.platform.repositories.OdsDocumentRepository;
import ro.simavi.odysseus.platform.services.OdsDocumentService;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;
import java.util.logging.Logger;

@Service
public class OdsDocumentServiceImpl implements OdsDocumentService {
    @Autowired
    OdsDocumentRepository odsDocumentRepository;

    @Autowired
    private KieSession session;


    @Override
    public OdsDocument saveOrEditOdsDevice(OdsDocument odsDocument) {
        return odsDocumentRepository.save(odsDocument);
    }

    @Override
    public void deleteOdsDevice(OdsDocument odsDocument) {
        odsDocumentRepository.delete(odsDocument);
    }

    @Override
    public OdsDocument findOdsDocumentByPersonalId(String personalId) {

        return odsDocumentRepository.findOdsDocumentByPersonalId(personalId);
    }

    @Override
    public List<OdsDocument> findOdsDocumentsBySurnameEqualsIgnoreCase(String surname)
    {
        return odsDocumentRepository.findOdsDocumentsBySurnameEqualsIgnoreCase(surname);
    }

    @Override
    public List<OdsDocument> findOdsDocumentsByGivenNameEqualsIgnoreCase(String givenName) {

        return odsDocumentRepository.findOdsDocumentsByGivenNameEqualsIgnoreCase(givenName);
    }

    @Override
    public List<OdsDocument> findOdsDocumentsByGivenNameEqualsIgnoreCaseAndSurnameEqualsIgnoreCase(String givenName, String surname) {
        return odsDocumentRepository.findOdsDocumentsByGivenNameEqualsIgnoreCaseAndSurnameEqualsIgnoreCase(givenName, surname);
    }

    @Override
    public List<OdsDocument> findAllBy(OdsDocument odsDocument) {

        // todo chage here
        Example<OdsDocument> eo = null;
        List<OdsDocument> all = odsDocumentRepository.findAll(eo);
        return all;
    }

    @Override
    public OdsDocument addPicture(String personalId, byte[] picture) {
        OdsDocument od = findOdsDocumentByPersonalId(personalId);
        od.setPicture(picture);
        return saveOrEditOdsDocument(od);
    }

    @Override
    public List<OdsDocument> findOdsDocumentsByGivenNameContainsIgnoreCaseOrSurnameContainsIgnoreCase(String givenName, String surname) {
        return odsDocumentRepository.findOdsDocumentsByGivenNameContainsIgnoreCaseOrSurnameContainsIgnoreCase(givenName, surname);
    }

    @Override
    public OdsDocument saveOrEditOdsDocument(OdsDocument odsDocumentSelected) {
        if (Objects.nonNull(odsDocumentSelected.getId())) {
            Optional<OdsDocument> optionalOdsDocument = this.odsDocumentRepository.findById(odsDocumentSelected.getId());
            if(optionalOdsDocument.isPresent()) {
                optionalOdsDocument.get().setTravelerId(odsDocumentSelected.getTravelerId());
                optionalOdsDocument.get().setDocumentType(odsDocumentSelected.getDocumentType());
                optionalOdsDocument.get().setCountryCode(odsDocumentSelected.getCountryCode());
                optionalOdsDocument.get().setDocNumber(odsDocumentSelected.getDocNumber());
                optionalOdsDocument.get().setSurname(odsDocumentSelected.getSurname());
                optionalOdsDocument.get().setGivenName(odsDocumentSelected.getGivenName());
                optionalOdsDocument.get().setCitizenship(odsDocumentSelected.getCitizenship());
                optionalOdsDocument.get().setDateOfBirth(odsDocumentSelected.getDateOfBirth());
                optionalOdsDocument.get().setPersonalId(odsDocumentSelected.getPersonalId());
                optionalOdsDocument.get().setSex(odsDocumentSelected.getSex());
                optionalOdsDocument.get().setPlaceOfBirth(odsDocumentSelected.getPlaceOfBirth());
                optionalOdsDocument.get().setDateOfIssuance(odsDocumentSelected.getDateOfIssuance());
                optionalOdsDocument.get().setAuthority(odsDocumentSelected.getAuthority());
                optionalOdsDocument.get().setDateOfExpiry(odsDocumentSelected.getDateOfExpiry());
                optionalOdsDocument.get().setDocFullId(odsDocumentSelected.getDocFullId());
                optionalOdsDocument.get().setHeight(odsDocumentSelected.getHeight());
                optionalOdsDocument.get().setColorOfEyes(odsDocumentSelected.getColorOfEyes());
                optionalOdsDocument.get().setResidence(odsDocumentSelected.getResidence());
                optionalOdsDocument.get().setPicture(odsDocumentSelected.getPicture());
                optionalOdsDocument.get().setHasRisk(odsDocumentSelected.getHasRisk());


                this.odsDocumentRepository.save(optionalOdsDocument.get());
                return optionalOdsDocument.get();
            }

        }

        OdsDocument odsDocument = new OdsDocument();
        odsDocument.setTravelerId(odsDocumentSelected.getTravelerId());
        odsDocument.setDocumentType(odsDocumentSelected.getDocumentType());
        odsDocument.setCountryCode(odsDocumentSelected.getCountryCode());
        odsDocument.setDocNumber(odsDocumentSelected.getDocNumber());
        odsDocument.setSurname(odsDocumentSelected.getSurname());
        odsDocument.setGivenName(odsDocumentSelected.getGivenName());
        odsDocument.setCitizenship(odsDocumentSelected.getCitizenship());
        odsDocument.setDateOfBirth(odsDocumentSelected.getDateOfBirth());
        odsDocument.setPersonalId(odsDocumentSelected.getPersonalId());
        odsDocument.setSex(odsDocumentSelected.getSex());
        odsDocument.setPlaceOfBirth(odsDocumentSelected.getPlaceOfBirth());
        odsDocument.setDateOfIssuance(odsDocumentSelected.getDateOfIssuance());
        odsDocument.setAuthority(odsDocumentSelected.getAuthority());
        odsDocument.setDateOfExpiry(odsDocumentSelected.getDateOfExpiry());
        odsDocument.setDocFullId(odsDocumentSelected.getDocFullId());
        odsDocument.setHeight(odsDocumentSelected.getHeight());
        odsDocument.setColorOfEyes(odsDocumentSelected.getColorOfEyes());
        odsDocument.setResidence(odsDocumentSelected.getResidence());
        odsDocument.setPicture(odsDocumentSelected.getPicture());
        odsDocument.setHasRisk(odsDocumentSelected.getHasRisk());

        try {
            return odsDocumentRepository.save(odsDocument);
        }
        catch (Exception dex){
            Logger.getLogger(this.getClass().getName()).warning(odsDocument.getDocFullId()+ " - "+ dex.getMessage());
        }

        return odsDocument;
    }

    @Override
    public void deleteOdsDocument(OdsDocument odsDocumentSelected) {
        try {
            odsDocumentRepository.delete(odsDocumentSelected);
        }
        catch (Exception dex) {
            Logger.getLogger(this.getClass().getName()).warning("fail to delete " + odsDocumentSelected.getDocFullId()+ " - "+ dex.getMessage());
        }
    }

    @Override
    public String verifyDocumentForTrain(OdsDocument odsDocument){
        String result = null;

        session.insert(odsDocument);
        int ruleFiredCount = session.fireAllRules();

        if(ruleFiredCount != 0 ) {
            result = "Risk";
        }else{
        Random random = new Random();

        double randomValue = random.nextDouble();

        if(randomValue < 0.99)
           result = "Successfully";
        else
            result = "Error";
        }

        return result;
    }

}
