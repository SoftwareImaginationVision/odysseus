package ro.simavi.odysseus.platform.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import ro.simavi.odysseus.platform.entities.OdsWorkflowNode;

import java.util.List;

public interface OdsWorkflowNodeRepository extends JpaRepository<OdsWorkflowNode, Integer> {
    List<OdsWorkflowNode> findAllByNodeNameContainingIgnoreCaseOrNodeTextContainingIgnoreCase(@Param("nodeName") String nodeName, @Param("nodeText") String nodeText);

    OdsWorkflowNode findFirstByNodeNameEqualsIgnoreCase(@Param("nodeName") String nodeName);

    boolean existsByNodeNameEqualsIgnoreCase(@Param("nodeName") String nodeName);

    boolean existsById(@Param("id") Integer id);

}