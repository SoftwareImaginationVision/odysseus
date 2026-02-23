package ro.simavi.odysseus.platform.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ro.simavi.odysseus.platform.entities.OdsSimpleRiskProfile;

import java.time.Instant;
import java.util.List;

public interface OdsSimpleRiskProfileRepository extends JpaRepository<OdsSimpleRiskProfile, Integer> {
    @Query(value = "Select c FROM OdsSimpleRiskProfile c  where c.profileName = :gname")
    List<OdsSimpleRiskProfile> findAllSimpleRiskProfilesByName(@Param(value = "gname") String name);

    List<OdsSimpleRiskProfile> findOdsSimpleRiskProfileByProfileNameContainingIgnoreCase(String name);

    @Query(value = "Select c FROM OdsSimpleRiskProfile c  where c.createdAt between :dateFrom and :dateTo")
    List<OdsSimpleRiskProfile> findAllSimpleRiskProfilesCreatedBetween(@Param(value = "dateFrom") Instant dateFrom, @Param(value = "dateTo") Instant dateTo);

    @Query(value = "Select c FROM OdsSimpleRiskProfile c  where (c.validFrom between :dateFrom and :dateTo) and (c.validTo between :dateFrom and :dateTo)")
    List<OdsSimpleRiskProfile> findAllSimpleRiskProfilesValidityBetween(@Param(value = "dateFrom") Instant dateFrom, @Param(value = "dateTo") Instant dateTo);

    @Query(value = "Select c FROM OdsSimpleRiskProfile c  where c.entityName =:entityName")
    List<OdsSimpleRiskProfile> findAllSimpleRiskProfilesByEntityName(@Param(value = "entityName") String entityName);

    @Query(value = "Select c FROM OdsSimpleRiskProfile c  where c.entityName =:entityName and c.columnName =:columnName")
    List<OdsSimpleRiskProfile> findAllSimpleRiskProfilesByEntityAndColumnName(@Param(value = "entityName") String entityName, @Param(value = "columnName") String columnName);
}