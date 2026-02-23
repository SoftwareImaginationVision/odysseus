package ro.simavi.odysseus.platform.controllers;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;
import ro.simavi.odysseus.platform.entities.*;
import ro.simavi.odysseus.platform.repositories.*;
import ro.simavi.odysseus.platform.services.OdsAuditLogService;
import ro.simavi.odysseus.platform.services.OdsWorkflowInstanceNodeService;
import ro.simavi.odysseus.platform.services.OdsWorkflowInstanceService;

import javax.annotation.PostConstruct;
import java.time.Instant;
import java.util.*;

@Setter
@Getter
@Component
@Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class WorkflowInstanceController {
    @Autowired
    private OdsWorkflowInstanceService odsWorkflowInstanceService;

    @Autowired
    private OdsWorkflowLinkRepository odsWorkflowLinkRepository;

    @Autowired
    private OdsWorkflowNodeRepository odsWorkflowNodeRepository;

    @Autowired
    private OdsWorkflowRepository odsWorkflowRepository;

    @Autowired
    private OdsWorkflowInstanceNodeRepository odsWorkflowInstanceNodeRepository;

    @Autowired
    private OdsWorkflowInstanceNodeService odsWorkflowInstanceNodeService;

    @Autowired
    private OdsWorkflowInstanceRepository odsWorkflowInstanceRepository;

    @Autowired
    private OdsAuditLogService odsAuditLogService;

    private KeycloackRestCalls keycloackRestCalls;

    private OdsWorkflowInstance odsWorkflowInstanceCurrent;

    private OdsWorkflowInstanceNode odsWorkflowInstanceNodeCurrent;

    @PostConstruct
    public void init(){
        keycloackRestCalls = new KeycloackRestCalls();

    }

    public void createWorkflowInstance(String firstNodeName, String tuuid) {
        if(!odsWorkflowInstanceRepository.existsByInstanceNameAndWorkflowStatus(keycloackRestCalls.getUsername(),"Active")) {
            if (odsWorkflowNodeRepository.existsByNodeNameEqualsIgnoreCase(firstNodeName)) {
                OdsWorkflowNode odsWorkflowNode = odsWorkflowNodeRepository.findFirstByNodeNameEqualsIgnoreCase(firstNodeName);
                if (odsWorkflowLinkRepository.existsByDestNode(odsWorkflowNode.getId())) {
                    OdsWorkflowLink odsWorkflowLink = odsWorkflowLinkRepository.findFirstBySourceNodeAndDestNode(null,odsWorkflowNode.getId());
                    if (Objects.nonNull(odsWorkflowLink) && Objects.isNull(odsWorkflowLink.getSourceNode())) {
                        String workflowId = odsWorkflowLink.getWorkflowId();
                        OdsWorkflow odsWorkflow = odsWorkflowRepository.getById(Integer.valueOf(workflowId));
                        OdsWorkflowInstance odsWorkflowInstance = new OdsWorkflowInstance();
                        odsWorkflowInstance.setWorkflowName(odsWorkflow.getWorkflowName());
                        odsWorkflowInstance.setInstanceName(keycloackRestCalls.getUsername());
                        odsWorkflowInstance.setCreatedOn(Instant.now());
                        odsWorkflowInstance.setWorkflowStatus("Active");
                        odsAuditLogService.startTravelProcess(this.getClass().getName(), "createWorkflowInstance", odsWorkflowInstance.getInstanceName());
                        if(tuuid == null)
                            tuuid = "1234";
                        odsWorkflowInstance.setTravelerUuid(tuuid);

                        OdsWorkflowInstance odsWorkflowInstance1 = odsWorkflowInstanceService.saveOrEditOdsWorkflowInstance(odsWorkflowInstance);
                        odsWorkflowInstanceCurrent = odsWorkflowInstance1;


                        List<OdsWorkflowLink> odsWorkflowLinkList = odsWorkflowLinkRepository.findAllByWorkflowId(workflowId);
                        List<OdsWorkflowNode> odsWorkflowNodeList = new ArrayList<>();
                        List<Integer> listSource = new ArrayList<>();
                        List<Integer> listDest = new ArrayList<>();
                        for (OdsWorkflowLink workflowLink : odsWorkflowLinkList) {
                            listSource.add(workflowLink.getSourceNode());
                            listDest.add(workflowLink.getDestNode());
                        }

                        for (Integer i : mergeAndFilterLists(listSource, listDest)) {
                            odsWorkflowNodeList.add(odsWorkflowNodeRepository.getById(i));
                        }

                        for (OdsWorkflowNode node : odsWorkflowNodeList) {
                            OdsWorkflowInstanceNode odsWorkflowInstanceNode = new OdsWorkflowInstanceNode();
                            odsWorkflowInstanceNode.setNodeName(node.getNodeName());
                            odsWorkflowInstanceNode.setDruleId(node.getDruleId());
                            odsWorkflowInstanceNode.setDruleParameters(node.getDruleParameters());
                            odsWorkflowInstanceNode.setNodeText(node.getNodeText());
                            odsWorkflowInstanceNode.setNodeText(node.getNodeType());
                            odsWorkflowInstanceNode.setDruleName(node.getDruleName());
                            odsWorkflowInstanceNode.setCreatedOn(Instant.now());
                            odsWorkflowInstanceNode.setWorkflowInstanceId(odsWorkflowInstance1.getId());
                            odsWorkflowInstanceNode.setWorkflowInstanceName(odsWorkflowInstance1.getInstanceName());
                            odsWorkflowInstanceNode.setTravelerUuid(odsWorkflowInstance1.getTravelerUuid());
                            if (node.getNodeName().equalsIgnoreCase(firstNodeName)) {
                                odsWorkflowInstanceNode.setNodeStatus("Active");
                            } else {
                                odsWorkflowInstanceNode.setNodeStatus("Inactive");
                            }
                            OdsWorkflowInstanceNode odsWorkflowInstanceNode1 = odsWorkflowInstanceNodeService.saveOrEditOdsWorkflowInstanceNode(odsWorkflowInstanceNode);
                            if (node.getNodeName().equalsIgnoreCase(firstNodeName)) {
                                odsWorkflowInstanceNodeCurrent = odsWorkflowInstanceNode1;
                            }
                        }

                    }
                }
            }
        }else {
            OdsWorkflowInstance odsWorkflowInstanceActive = odsWorkflowInstanceRepository.findFirstByInstanceNameAndWorkflowStatus(keycloackRestCalls.getUsername(),"Active");
            odsAuditLogService.startTravelProcess(this.getClass().getName(), "createWorkflowInstance", odsWorkflowInstanceActive.getInstanceName(),"Work on existing instance", odsWorkflowInstanceActive.getWorkflowName());
            OdsWorkflow odsWorkflow = odsWorkflowRepository.findFirstByWorkflowNameEqualsIgnoreCase(odsWorkflowInstanceActive.getWorkflowName());
            List<OdsWorkflowLink> odsWorkflowLinkList = odsWorkflowLinkRepository.findAllByWorkflowId(String.valueOf(odsWorkflow.getId()));
            OdsWorkflowInstanceNode odsWorkflowInstanceNodeActive = odsWorkflowInstanceNodeRepository.findFirstByWorkflowInstanceIdAndNodeStatusEqualsIgnoreCase(odsWorkflowInstanceActive.getId(),"Active");
            for(OdsWorkflowLink odsWorkflowLink : odsWorkflowLinkList){
                if(Objects.nonNull(odsWorkflowLink.getSourceNode())) {
                    OdsWorkflowNode odsWorkflowNodeSource = odsWorkflowNodeRepository.getById(odsWorkflowLink.getSourceNode());
                    if(odsWorkflowNodeSource.getNodeName().equalsIgnoreCase(odsWorkflowInstanceNodeActive.getNodeName())){
                        if(Objects.nonNull(odsWorkflowLink.getDestNode())) {
                            OdsWorkflowNode odsWorkflowNodeDest = odsWorkflowNodeRepository.getById(odsWorkflowLink.getDestNode());
                            if(odsWorkflowNodeDest.getNodeName().equalsIgnoreCase(firstNodeName)) {
                                odsWorkflowInstanceNodeActive.setNodeStatus("Inactive");
                                odsWorkflowInstanceNodeRepository.save(odsWorkflowInstanceNodeActive);
                                OdsWorkflowInstanceNode odsWorkflowInstanceNodeNewActive = odsWorkflowInstanceNodeRepository.findFirstByWorkflowInstanceIdAndNodeNameEqualsIgnoreCase(odsWorkflowInstanceActive.getId(), odsWorkflowNodeDest.getNodeName());
                                if(odsWorkflowNodeDest.getNodeName().equalsIgnoreCase("verifyVDP") && odsWorkflowInstanceNodeNewActive.getTravelerUuid().equals(tuuid)){
                                    odsWorkflowInstanceNodeNewActive.setNodeStatus("Active");
                                    odsWorkflowInstanceNodeRepository.save(odsWorkflowInstanceNodeNewActive);
                                } else if (!odsWorkflowNodeDest.getNodeName().equalsIgnoreCase("verifyVDP")) {
                                    odsWorkflowInstanceNodeNewActive.setNodeStatus("Active");
                                    odsWorkflowInstanceNodeRepository.save(odsWorkflowInstanceNodeNewActive);
                                } else if (odsWorkflowNodeDest.getNodeName().equalsIgnoreCase("verifyVDP") && tuuid.equalsIgnoreCase("1234") ||  Objects.isNull(tuuid)) {
                                    odsWorkflowInstanceNodeNewActive.setNodeStatus("Active");
                                    odsWorkflowInstanceNodeRepository.save(odsWorkflowInstanceNodeNewActive);
                                } else {
                                    odsWorkflowInstanceNodeActive.setNodeStatus("Active");
                                    odsWorkflowInstanceNodeRepository.save(odsWorkflowInstanceNodeActive);
                                }
                                OdsWorkflowLink workflowLink = odsWorkflowLinkRepository.findFirstBySourceNodeAndWorkflowId(odsWorkflowLink.getDestNode(),String.valueOf(odsWorkflow.getId()));
                                if(Objects.nonNull(workflowLink) && workflowLink.getDestNode() == null){
                                    odsWorkflowInstanceActive.setWorkflowStatus("Inactive");
                                    odsWorkflowInstanceRepository.save(odsWorkflowInstanceActive);
                                    odsWorkflowInstanceNodeActive.setNodeStatus("Inactive");
                                    odsWorkflowInstanceNodeRepository.save(odsWorkflowInstanceNodeActive);
                                    odsWorkflowInstanceNodeNewActive.setNodeStatus("Inactive");
                                    odsWorkflowInstanceNodeRepository.save(odsWorkflowInstanceNodeNewActive);
                                }
                            }
                        }else {
                            //dezactivare workflow and nodes
                            odsWorkflowInstanceActive.setWorkflowStatus("Inactive");
                            odsWorkflowInstanceRepository.save(odsWorkflowInstanceActive);
                            odsWorkflowInstanceNodeActive.setNodeStatus("Inactive");
                            odsWorkflowInstanceNodeRepository.save(odsWorkflowInstanceNodeActive);
                            odsAuditLogService.endTravelProcess(this.getClass().getName(), "End the workflow instance", odsWorkflowInstanceActive.getInstanceName(), odsWorkflowInstanceActive.getResultWorkflow(), odsWorkflowInstanceActive.getWorkflowName(), null);
                        }
                    }
                }
            }
        }
    }

    public Map<String, Long> countWorkflowInstanceNodesByStatus() {
        Map<String, Long> counts = new HashMap<>();
        counts.put("Active", odsWorkflowInstanceNodeRepository.countByNodeStatus("Active"));
        counts.put("Inactive", odsWorkflowInstanceNodeRepository.countByNodeStatus("Inactive"));
        return counts;
    }
    public Map<String, Long> countWorkflowInstancesByStatus() {
        Map<String, Long> counts = new HashMap<>();
        counts.put("Active", odsWorkflowInstanceRepository.countByWorkflowStatus("Active"));
        counts.put("Inactive", odsWorkflowInstanceRepository.countByWorkflowStatus("Inactive"));
        return counts;
    }

    public Map<String, Long> countActiveWorkflowInstanceNodesByType() {
        Map<String, Long> counts = new HashMap<>();
        List<OdsWorkflowInstanceNode> activeNodes = odsWorkflowInstanceNodeRepository.findByNodeStatusIgnoreCase("Active");

        for (OdsWorkflowInstanceNode node : activeNodes) {
            String nodeType = node.getNodeName();
            counts.put(nodeType, counts.getOrDefault(nodeType, 0L) + 1);

        }

        return counts;
    }

    public Map<String, Long> countInactiveWorkflowInstanceNodesByType() {
        Map<String, Long> counts = new HashMap<>();
        List<OdsWorkflowInstanceNode> InactiveNodes = odsWorkflowInstanceNodeRepository.findByNodeStatusIgnoreCase("Inactive");

        for (OdsWorkflowInstanceNode node : InactiveNodes) {
            String nodeType = node.getNodeName();
            counts.put(nodeType, counts.getOrDefault(nodeType, 0L) + 1);

        }

        return counts;
    }


    private List<Integer> mergeAndFilterLists(List<Integer> list1, List<Integer> list2) {
        // Merge the lists
        List<Integer> mergedList = new ArrayList<>();
        mergedList.addAll(filterNonNullElements(list1));
        mergedList.addAll(filterNonNullElements(list2));

        // Filter out duplicates using a Set
        Set<Integer> uniqueSet = new HashSet<>(mergedList);
        mergedList.clear();
        mergedList.addAll(uniqueSet);

        return mergedList;
    }

    private List<Integer> filterNonNullElements(List<Integer> list) {
        List<Integer> filteredList = new ArrayList<>();
        for (Integer element : list) {
            if (element != null) {
                filteredList.add(element);
            }
        }
        return filteredList;
    }


}
