package ro.simavi.odysseus.platform.services;

import org.springframework.stereotype.Service;
import ro.simavi.odysseus.platform.entities.OdsCombinedRiskProfile;
import ro.simavi.odysseus.platform.entities.OdsSimpleRiskProfile;

import java.util.Date;
import java.util.List;
@Service
public interface ComplexRiskProfileService {
    OdsCombinedRiskProfile saveOrEditOdsSComplexRiskProfile(OdsCombinedRiskProfile odsCombinedRiskProfile);
    void deleteOdsSComplexRiskProfile(OdsCombinedRiskProfile odsCombinedRiskProfile);

    void deleteOdsComplexRiskProfileById(Integer id);
    List<OdsCombinedRiskProfile> getAllComplexProfiles();

    List<OdsCombinedRiskProfile> getComplexProfilesByName(String profileName);

    List<OdsCombinedRiskProfile> getComplexProfilesByObjectName(String objectName);

    List<OdsCombinedRiskProfile> getComplexProfilesByColumnAndObjectName(String columnName, String objectName);

    List<OdsCombinedRiskProfile> getComplexProfilesValidBetween(Date from, Date to);

    List<OdsCombinedRiskProfile> getComplexProfilesCreatedBetween(Date from, Date to);

    List<OdsCombinedRiskProfile> getComplexProfilesValidToday(Date today);

    OdsCombinedRiskProfile getComplexProfileById(Integer id);
    Float getRiskValue(Integer id);

    Float computeRiskForAllComplexProfiles(Object entity);

    Float applyComplexProfileOnObject(Integer profileId, Object entity);
}
