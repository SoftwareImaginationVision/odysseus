package ro.simavi.odysseus.platform.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import ro.simavi.odysseus.platform.entities.OdsWorkflowLink;

import java.util.List;

public interface OdsWorkflowLinkRepository extends JpaRepository<OdsWorkflowLink, Integer> {
    OdsWorkflowLink findFirstBySourceNodeAndDestNodeAndWorkflowId(@Param("sourceNode") Integer sourceNode, @Param("destNode") Integer destNode, @Param("workflowId") String workflowId);

    List<OdsWorkflowLink> findAllByWorkflowId(@Param("workflowId") String workflowId);

    OdsWorkflowLink findFirstBySourceNodeAndWorkflowId(@Param("sourceNode") Integer sourceNode, @Param("workflowId") String workflowId);

    OdsWorkflowLink findFirstByDestNodeAndWorkflowId(@Param("destNode") Integer destNode, @Param("workflowId") String workflowId);

    OdsWorkflowLink findFirstByDestNode(@Param("destNode") Integer destNode);

    boolean existsByDestNode(@Param("destNode") Integer destNode);

    OdsWorkflowLink findFirstBySourceNodeAndDestNode(@Param("sourceNode") Integer sourceNode, @Param("destNode") Integer destNode);
}