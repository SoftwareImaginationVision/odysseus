package ro.simavi.odysseus.platform.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import ro.simavi.odysseus.platform.entities.OdsUser;

import java.util.List;

public interface OdsUserRepository extends JpaRepository<OdsUser, Integer> {

    List<OdsUser> findAllByUserNameContainingIgnoreCaseOrStatusContainingIgnoreCaseOrEmailContainingIgnoreCase(@Param("userName") String userName, @Param("status") String status, @Param("email") String email);
}