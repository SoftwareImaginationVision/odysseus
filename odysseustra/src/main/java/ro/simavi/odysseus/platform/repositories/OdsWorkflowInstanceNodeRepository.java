package ro.simavi.odysseus.platform.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import ro.simavi.odysseus.platform.entities.OdsWorkflowInstanceNode;

import java.util.List;

public interface OdsWorkflowInstanceNodeRepository extends JpaRepository<OdsWorkflowInstanceNode, Integer> {
    List<OdsWorkflowInstanceNode> findAllByWorkflowInstanceId(@Param("workflowInstanceId") Integer workflowInstanceId);
    OdsWorkflowInstanceNode findFirstByWorkflowInstanceIdAndNodeStatusEqualsIgnoreCase(@Param("workflowInstanceId") Integer workflowInstanceId, @Param("nodeStatus") String nodeStatus);

    OdsWorkflowInstanceNode findFirstByWorkflowInstanceIdAndNodeNameEqualsIgnoreCase(@Param("workflowInstanceId") Integer workflowInstanceId, @Param("nodeName") String nodeName);
    boolean existsByWorkflowInstanceIdAndNodeStatusEqualsIgnoreCase(@Param("workflowInstanceId") Integer workflowInstanceId, @Param("nodeStatus") String nodeStatus);

    Integer countOdsWorkflowInstanceNodeByNodeNameEqualsIgnoreCase(@Param("nodeName") String nodeName);

    Integer countOdsWorkflowInstanceNodeByNodeNameEqualsIgnoreCaseAndNodeStatusEqualsIgnoreCase(@Param("nodeName") String nodeName, @Param("nodeStatus") String nodeStatus);


    Long countByNodeStatus(String nodeStatus);
}