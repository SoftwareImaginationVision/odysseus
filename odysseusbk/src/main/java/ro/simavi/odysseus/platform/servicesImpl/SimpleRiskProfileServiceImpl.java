package ro.simavi.odysseus.platform.servicesImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ro.simavi.odysseus.platform.entities.OdsSimpleRiskProfile;
import ro.simavi.odysseus.platform.repositories.OdsSimpleRiskProfileRepository;
import ro.simavi.odysseus.platform.services.SimpleRiskProfileService;

import java.util.Date;
import java.util.List;
@Service
public class SimpleRiskProfileServiceImpl implements SimpleRiskProfileService {

    @Autowired
    OdsSimpleRiskProfileRepository odsSimpleRiskProfileRepository;

    @Override
    public OdsSimpleRiskProfile saveOrEditOdsSimpleRiskProfile(OdsSimpleRiskProfile simpleRiskProfile) {
        return odsSimpleRiskProfileRepository.save(simpleRiskProfile);
    }

    @Override
    public void deleteOdsSimpleRiskProfile(OdsSimpleRiskProfile odsSimpleRiskProfile) {
        odsSimpleRiskProfileRepository.delete(odsSimpleRiskProfile);
    }

    @Override
    public void deleteOdsSimpleRiskProfileById(Integer id) {
        OdsSimpleRiskProfile od = odsSimpleRiskProfileRepository.getById(id);
        odsSimpleRiskProfileRepository.delete(od);
    }

    @Override
    public List<OdsSimpleRiskProfile> getAllSimpleProfiles() {
        return odsSimpleRiskProfileRepository.findAll();
    }

    @Override
    public List<OdsSimpleRiskProfile> getSimpleProfilesByName(String profileName) {
        return null;
    }

    @Override
    public List<OdsSimpleRiskProfile> getSimpleProfilesByObjectName(String objectName) {
        return null;
    }

    @Override
    public List<OdsSimpleRiskProfile> getSimpleProfilesByColumnAndObjectName(String columnName, String objectName) {
        return null;
    }

    @Override
    public List<OdsSimpleRiskProfile> getSimpleProfilesValidBetween(Date from, Date to) {
        return null;
    }

    @Override
    public List<OdsSimpleRiskProfile> getSimpleProfilesCreatedBetween(Date from, Date to) {
        return null;
    }

    @Override
    public List<OdsSimpleRiskProfile> getSimpleProfilesValidToday(Date today) {
        return null;
    }

    @Override
    public OdsSimpleRiskProfile getSimpleProfileById(Integer id) {
        return odsSimpleRiskProfileRepository.getById(id);
    }

    @Override
    public Float getRiskValue(Integer id) {
        return null;
    }

    @Override
    public Float computeRiskForAllSimpleProfiles(Object entity) {
        return null;
    }

    @Override
    public Float applySimpleProfileOnObject(Integer profileId, Object entity) {
        return null;
    }
}
