package ro.simavi.odysseus.platform.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import ro.simavi.odysseus.platform.entities.OdsWorkflow;

import java.util.List;

public interface OdsWorkflowRepository extends JpaRepository<OdsWorkflow, Integer> {
    List<OdsWorkflow> findAllByWorkflowNameContainingIgnoreCase(@Param("workflowName") String workflowName);

    OdsWorkflow findFirstByWorkflowNameEqualsIgnoreCase(@Param("workflowName") String workflowName);
}