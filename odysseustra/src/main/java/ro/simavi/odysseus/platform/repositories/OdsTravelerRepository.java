package ro.simavi.odysseus.platform.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import ro.simavi.odysseus.platform.entities.OdsTraveler;

import java.util.List;

public interface OdsTravelerRepository extends JpaRepository<OdsTraveler, Integer> {
    public OdsTraveler findOdsTravelerByPersonalId(String personalId);

    public OdsTraveler findOdsTravelerByEmail(String personalId);
    public List<OdsTraveler> findOdsTravelersByFirstNameContainingIgnoreCase(String firstName);

    public List<OdsTraveler> findOdsTravelersByLastNameContainingIgnoreCase(String lastName);

    public List<OdsTraveler> findOdsTravelersByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(String fname, String lname);

    public List<OdsTraveler> findOdsTravelersByCityContainsIgnoreCase(String city);

    OdsTraveler findFirstByTuuid(@Param("tuuid") String tuuid);

    OdsTraveler findFirstByRegisteredUser(@Param("registered_user") String registeredUser);

    OdsTraveler findByRegisteredUser(@Param("registered_user") String registeredUser);

    boolean existsByRegisteredUser(@Param("registered_user") String registeredUser);

    OdsTraveler findByTuuid(@Param("tuuid") String tuuid);

}
