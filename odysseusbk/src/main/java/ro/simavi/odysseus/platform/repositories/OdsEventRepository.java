package ro.simavi.odysseus.platform.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import ro.simavi.odysseus.platform.entities.OdsEvent;

import java.util.List;

public interface OdsEventRepository extends JpaRepository<OdsEvent, Integer> {
    List<OdsEvent> findAllByEventContainingIgnoreCaseOrStatusContainingIgnoreCaseOrEventSourceContainingIgnoreCase(@Param("event") String event, @Param("status") String status, @Param("eventSource") String eventSource);
}