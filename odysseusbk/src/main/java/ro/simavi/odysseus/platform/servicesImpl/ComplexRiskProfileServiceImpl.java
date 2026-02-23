package ro.simavi.odysseus.platform.servicesImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ro.simavi.odysseus.platform.entities.OdsCombinedRiskProfile;
import ro.simavi.odysseus.platform.repositories.OdsCombinedRiskProfileRepository;
import ro.simavi.odysseus.platform.services.ComplexRiskProfileService;

import java.util.Date;
import java.util.List;

@Service
public class ComplexRiskProfileServiceImpl implements ComplexRiskProfileService {

    @Autowired
    OdsCombinedRiskProfileRepository odsCombinedRiskProfileRepository;

    @Override
    public OdsCombinedRiskProfile saveOrEditOdsSComplexRiskProfile(OdsCombinedRiskProfile odsCombinedRiskProfile) {
        return odsCombinedRiskProfileRepository.save(odsCombinedRiskProfile);
    }

    @Override
    public void deleteOdsSComplexRiskProfile(OdsCombinedRiskProfile odsCombinedRiskProfile) {
        odsCombinedRiskProfileRepository.delete(odsCombinedRiskProfile);
    }

    @Override
    public void deleteOdsComplexRiskProfileById(Integer id) {
        OdsCombinedRiskProfile od = odsCombinedRiskProfileRepository.getById(id);
        odsCombinedRiskProfileRepository.delete(od);
    }

    @Override
    public List<OdsCombinedRiskProfile> getAllComplexProfiles() {
        return odsCombinedRiskProfileRepository.findAll();
    }

    @Override
    public List<OdsCombinedRiskProfile> getComplexProfilesByName(String profileName) {
        return null;
    }

    @Override
    public List<OdsCombinedRiskProfile> getComplexProfilesByObjectName(String objectName) {
        return null;
    }

    @Override
    public List<OdsCombinedRiskProfile> getComplexProfilesByColumnAndObjectName(String columnName, String objectName) {
        return null;
    }

    @Override
    public List<OdsCombinedRiskProfile> getComplexProfilesValidBetween(Date from, Date to) {
        return null;
    }

    @Override
    public List<OdsCombinedRiskProfile> getComplexProfilesCreatedBetween(Date from, Date to) {
        return null;
    }

    @Override
    public List<OdsCombinedRiskProfile> getComplexProfilesValidToday(Date today) {
        return null;
    }

    @Override
    public OdsCombinedRiskProfile getComplexProfileById(Integer id) {
        return odsCombinedRiskProfileRepository.getById(id);
    }

    @Override
    public Float getRiskValue(Integer id) {
        return null;
    }

    @Override
    public Float computeRiskForAllComplexProfiles(Object entity) {
        return null;
    }

    @Override
    public Float applyComplexProfileOnObject(Integer profileId, Object entity) {
        return null;
    }
}
