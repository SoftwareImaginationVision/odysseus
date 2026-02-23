package ro.simavi.odysseus.platform.services;

import org.springframework.stereotype.Service;
import ro.simavi.odysseus.platform.entities.AuditData;
import ro.simavi.odysseus.platform.entities.OdsEvent;
import ro.simavi.odysseus.platform.entities.OdsSimpleRiskProfile;

import java.util.Date;
import java.util.List;
@Service
public interface SimpleRiskProfileService {
    OdsSimpleRiskProfile saveOrEditOdsSimpleRiskProfile(OdsSimpleRiskProfile simpleRiskProfile);
    void deleteOdsSimpleRiskProfile(OdsSimpleRiskProfile odsSimpleRiskProfile);

    void deleteOdsSimpleRiskProfileById(Integer id);

    List<OdsSimpleRiskProfile> getAllSimpleProfiles();

    List<OdsSimpleRiskProfile> getSimpleProfilesByName(String profileName);

    List<OdsSimpleRiskProfile> getSimpleProfilesByObjectName(String objectName);

    List<OdsSimpleRiskProfile> getSimpleProfilesByColumnAndObjectName(String columnName, String objectName);

    List<OdsSimpleRiskProfile> getSimpleProfilesValidBetween(Date from, Date to);

    List<OdsSimpleRiskProfile> getSimpleProfilesCreatedBetween(Date from, Date to);

    List<OdsSimpleRiskProfile> getSimpleProfilesValidToday(Date today);

    OdsSimpleRiskProfile getSimpleProfileById(Integer id);
    Float getRiskValue(Integer id);

    Float computeRiskForAllSimpleProfiles(Object entity);

    Float applySimpleProfileOnObject(Integer profileId, Object entity);
}
