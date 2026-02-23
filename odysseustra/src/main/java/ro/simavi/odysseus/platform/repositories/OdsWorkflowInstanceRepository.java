package ro.simavi.odysseus.platform.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import ro.simavi.odysseus.platform.entities.OdsWorkflowInstance;

public interface OdsWorkflowInstanceRepository extends JpaRepository<OdsWorkflowInstance, Integer> {
    OdsWorkflowInstance findFirstByInstanceNameAndWorkflowStatus(@Param("instanceName") String instanceName, @Param("workflowStatus") String workflowStatus);

    boolean existsByInstanceNameAndWorkflowStatus(@Param("instanceName") String instanceName, @Param("workflowStatus") String workflowStatus);

    Long countByWorkflowStatus(String status);

    OdsWorkflowInstance findTopByInstanceNameAndWorkflowStatusOrderByIdDesc(@Param("instanceName") String instanceName, @Param("workflowStatus") String workflowStatus);

    boolean existsByWorkflowStatus(String active);

    OdsWorkflowInstance findFirstByWorkflowStatus(String active);

    OdsWorkflowInstance findTopByWorkflowStatusOrderByIdDesc(String inactive);
}