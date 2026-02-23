package ro.simavi.odysseus.platform.controllers;

import lombok.Getter;
import lombok.Setter;
import org.primefaces.PrimeFaces;
import org.primefaces.event.DragDropEvent;
import org.primefaces.event.ItemSelectEvent;
import org.primefaces.event.diagram.ConnectEvent;
import org.primefaces.event.diagram.DisconnectEvent;
import org.primefaces.model.diagram.Connection;
import org.primefaces.model.diagram.DefaultDiagramModel;
import org.primefaces.model.diagram.Element;
import org.primefaces.model.diagram.connector.StraightConnector;
import org.primefaces.model.diagram.endpoint.DotEndPoint;
import org.primefaces.model.diagram.endpoint.EndPoint;
import org.primefaces.model.diagram.endpoint.EndPointAnchor;
import org.primefaces.model.diagram.endpoint.RectangleEndPoint;
import org.primefaces.model.diagram.overlay.ArrowOverlay;
import org.primefaces.model.diagram.overlay.LabelOverlay;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;
import ro.simavi.odysseus.platform.entities.*;
import ro.simavi.odysseus.platform.repositories.*;
import ro.simavi.odysseus.platform.services.OdsWorkflowLinkService;
import ro.simavi.odysseus.platform.services.OdsWorkflowNodeService;
import ro.simavi.odysseus.platform.services.OdsWorkflowService;
import ro.simavi.odysseus.platform.services.UserOidcService;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;


@Setter
@Getter
@Component
@Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class WorkflowManagementController {
    @Autowired
    private OdsWorkflowRepository odsWorkflowRepository;

    @Autowired
    private OdsWorkflowService odsWorkflowService;

    @Autowired
    private OdsWorkflowNodeRepository odsWorkflowNodeRepository;

    @Autowired
    private OdsWorkflowNodeService odsWorkflowNodeService;

    @Autowired
    private OdsWorkflowLinkRepository odsWorkflowLinkRepository;

    @Autowired
    private OdsWorkflowLinkService odsWorkflowLinkService;

    @Autowired
    private OdsWorkflowInstanceRepository odsWorkflowInstanceRepository;

    @Autowired
    private OdsWorkflowInstanceNodeRepository odsWorkflowInstanceNodeRepository;

    @Autowired
    private UserOidcService userOidcService;


    private DefaultDiagramModel model;

    private DefaultDiagramModel modelForTraveler;


    private DefaultDiagramModel modelForBCP;

    private List<OdsWorkflow> odsWorkflowsList;

    private List<OdsWorkflowNode> odsWorkflowNodesList;

    private List<OdsWorkflow> odsWorkflowsListForBCP;

    private OdsWorkflow odsWorkflowSelected;

    private OdsWorkflowNode odsWorkflowNodeSelected;

    private OdsWorkflowLink odsWorkflowLinkSelected;

    private String nameFilter;

    private String nameFilterForWorkflowNode;

    private OdsWorkflowInstance odsWorkflowInstanceActive;

    private OdsWorkflowInstanceNode odsWorkflowInstanceNodeActive;

    private String nameOfSelectedWorkflowInstanceActive;

    private OdsWorkflowNode selectedNode;

    private boolean suspendEvent;

    private Integer numberOfActiveNodeInstance;

    private Integer totalNodeInstance;

    @PostConstruct
    public void init(){

        odsWorkflowsList = odsWorkflowRepository.findAll();
        if(odsWorkflowsList.isEmpty()){
            odsWorkflowRepository.save(new OdsWorkflow(
                    "Employee Onboarding",
                    Instant.parse("2022-01-10T09:00:00Z"),
                    30
            ));

            odsWorkflowRepository.save(new OdsWorkflow(
                    "Purchase Approval",
                    Instant.parse("2022-02-15T14:30:00Z"),
                    15
            ));

            odsWorkflowRepository.save(new OdsWorkflow(
                    "Bug Resolution",
                    Instant.parse("2022-03-20T11:45:00Z"),
                    60
            ));

            odsWorkflowRepository.save(new OdsWorkflow(
                    "Project Planning",
                    Instant.parse("2022-04-25T08:30:00Z"),
                    45
            ));

            odsWorkflowRepository.save(new OdsWorkflow(
                    "Customer Support Ticket",
                    Instant.parse("2022-05-30T13:15:00Z"),
                    20
            ));

            odsWorkflowsList = odsWorkflowRepository.findAll();
        }

        odsWorkflowNodesList = odsWorkflowNodeRepository.findAll();
        if(odsWorkflowNodesList.isEmpty()){
            odsWorkflowNodeRepository.save(new OdsWorkflowNode(
                    "Node1",
                    Instant.parse("2022-01-10T09:00:00Z"),
                    "Text for Node1",
                    "1"
            ));

            odsWorkflowNodeRepository.save(new OdsWorkflowNode(
                    "Node2",
                    Instant.parse("2022-02-15T14:30:00Z"),
                    "Text for Node2",
                    "1"
            ));

            odsWorkflowNodeRepository.save(new OdsWorkflowNode(
                    "Node3",
                    Instant.parse("2022-03-20T11:45:00Z"),
                    "Text for Node3",
                    "1"
            ));

            odsWorkflowNodeRepository.save(new OdsWorkflowNode(
                    "Node4",
                    Instant.parse("2022-04-25T08:30:00Z"),
                    "Text for Node4",
                    "1"
            ));

            odsWorkflowNodeRepository.save(new OdsWorkflowNode(
                    "Node5",
                    Instant.parse("2022-05-30T13:15:00Z"),
                    "Text for Node5",
                    "1"
            ));
            odsWorkflowNodesList = odsWorkflowNodeRepository.findAll();
        }

        model = new DefaultDiagramModel();

        model.setMaxConnections(-1);
        model.setContainment(false);

        model.getDefaultConnectionOverlays().add(new ArrowOverlay(20, 20, 1, 1));

        StraightConnector connector = new StraightConnector();
        connector.setPaintStyle("{stroke:'#C7B097',strokeWidth:3}");
        connector.setHoverPaintStyle("{stroke:'#5C738B'}");
        model.setDefaultConnector(connector);

        modelForTraveler = new DefaultDiagramModel();

        modelForTraveler.setMaxConnections(-1);
        modelForTraveler.setContainment(false);

        modelForTraveler.getDefaultConnectionOverlays().add(new ArrowOverlay(20, 20, 1, 1));

        StraightConnector connectorForTraveler = new StraightConnector();
        connectorForTraveler.setPaintStyle("{stroke:'#C7B097',strokeWidth:3}");
        connectorForTraveler.setHoverPaintStyle("{stroke:'#5C738B'}");
        modelForTraveler.setDefaultConnector(connectorForTraveler);

        modelForBCP = new DefaultDiagramModel();

        modelForBCP.setMaxConnections(-1);
        modelForBCP.setContainment(false);

        modelForBCP.getDefaultConnectionOverlays().add(new ArrowOverlay(20, 20, 1, 1));

        StraightConnector connectorForBCP = new StraightConnector();
        connectorForBCP.setPaintStyle("{stroke:'#C7B097',strokeWidth:3}");
        connectorForBCP.setHoverPaintStyle("{stroke:'#5C738B'}");
        modelForBCP.setDefaultConnector(connectorForBCP);
    }

    public void onPageLoad(){
        if(odsWorkflowInstanceRepository.existsByInstanceNameAndWorkflowStatus(userOidcService.getCurrentUserEmail(),"Active"))
            odsWorkflowInstanceActive = odsWorkflowInstanceRepository.findFirstByInstanceNameAndWorkflowStatus(userOidcService.getCurrentUserEmail(),"Active");
        else if (odsWorkflowInstanceRepository.existsByInstanceNameAndWorkflowStatus(userOidcService.getCurrentUserEmail(),"Inactive")) {
            odsWorkflowInstanceActive = odsWorkflowInstanceRepository.findTopByInstanceNameAndWorkflowStatusOrderByIdDesc(userOidcService.getCurrentUserEmail(),"Inactive");
        }else {
            odsWorkflowInstanceActive = null;
        }


        if(Objects.nonNull(odsWorkflowInstanceActive) && odsWorkflowInstanceNodeRepository.existsByWorkflowInstanceIdAndNodeStatusEqualsIgnoreCase(odsWorkflowInstanceActive.getId(),"Active")){
            odsWorkflowInstanceNodeActive = odsWorkflowInstanceNodeRepository.findFirstByWorkflowInstanceIdAndNodeStatusEqualsIgnoreCase(odsWorkflowInstanceActive.getId(),"Active");
        }else {
            odsWorkflowInstanceNodeActive = null;
        }

        odsWorkflowsListForBCP = odsWorkflowRepository.findAll();
    }


    public void deleteOdsWorkflowLink(OdsWorkflowLink odsWorkflowLink){
        if(Objects.nonNull(odsWorkflowLink)){
            odsWorkflowLinkService.deleteOdsWorkflowLink(odsWorkflowLink);
            FacesMessage msg =new FacesMessage("Successfully deleted!");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
    }


    private EndPoint createDotEndPoint(EndPointAnchor anchor) {
        DotEndPoint endPoint = new DotEndPoint(anchor);
        endPoint.setSource(true);
        endPoint.setStyle("{fill:'#98AFC7'}");
        endPoint.setHoverStyle("{fill:'#5C738B'}");

        return endPoint;
    }

    private EndPoint createRectangleEndPoint(EndPointAnchor anchor) {
        RectangleEndPoint endPoint = new RectangleEndPoint(anchor);
        endPoint.setTarget(true);
        endPoint.setStyle("{fill:'#98AFC7'}");
        endPoint.setHoverStyle("{fill:'#5C738B'}");

        return endPoint;
    }

    private Connection createConnection(EndPoint from, EndPoint to, String label) {
        Connection conn = new Connection(from, to);
        conn.getOverlays().add(new ArrowOverlay(20, 20, 1, 1));

        if (label != null) {
            conn.getOverlays().add(new LabelOverlay(label, "flow-label", 0.5));
        }

        return conn;
    }


    public void resetModelForTraveler() throws IOException {
            modelForTraveler = new DefaultDiagramModel();

            modelForTraveler.setMaxConnections(-1);
            modelForTraveler.setContainment(false);

            modelForTraveler.getDefaultConnectionOverlays().add(new ArrowOverlay(20, 20, 1, 1));

            StraightConnector connectorForTraveler = new StraightConnector();
            connectorForTraveler.setPaintStyle("{stroke:'#C7B097',strokeWidth:3}");
            connectorForTraveler.setHoverPaintStyle("{stroke:'#5C738B'}");
            modelForTraveler.setDefaultConnector(connectorForTraveler);

            String path = FacesContext.getCurrentInstance().getExternalContext().getRequest().toString();

            if(path.contains("pages")){
                FacesContext.getCurrentInstance().getExternalContext().redirect("verifyStatus.xhtml");
            }else {
                FacesContext.getCurrentInstance().getExternalContext().redirect("pages/verifyStatus.xhtml");
            }
    }


    public String getNodeName(Integer id){
        if(id != null) {
            String nodeName = "";
            OdsWorkflowNode node = odsWorkflowNodeRepository.getById(id);
            if (Objects.nonNull(node.getNodeName())) {
                nodeName = node.getNodeName();
            }
            return nodeName;
        }
        if(Objects.isNull(odsWorkflowLinkSelected.getSourceNode())){
            return "Start";
        } else if (Objects.isNull(odsWorkflowLinkSelected.getDestNode())) {
            return "End";
        }
        return "";
    }

    public boolean showButton(){
        boolean check = true;
        if(Objects.nonNull(odsWorkflowSelected)) {
            List<OdsWorkflowLink> odsWorkflowLinkList = odsWorkflowLinkRepository.findAllByWorkflowId(String.valueOf(odsWorkflowSelected.getId()));
            if (!odsWorkflowLinkList.isEmpty()) {
                check = false;
            }
        }
        return check;
    }

    public void createDiagramForTraveler(){
        // first add start and stop
        Element start = new Element("Start", "4em", "2em");
        EndPoint endPointStart = createDotEndPoint(EndPointAnchor.BOTTOM);
        start.addEndPoint(endPointStart);

        if(Objects.nonNull(odsWorkflowInstanceActive) && Objects.nonNull(odsWorkflowInstanceNodeActive)) {
            modelForTraveler.addElement(start);
            List<OdsWorkflowLink> odsWorkflowLinkList = odsWorkflowLinkRepository.findAllByWorkflowId(String.valueOf(odsWorkflowRepository.findFirstByWorkflowNameEqualsIgnoreCase(odsWorkflowInstanceActive.getWorkflowName()).getId()));
            if (!odsWorkflowLinkList.isEmpty()) {

                for (OdsWorkflowLink odsWorkflowLink : odsWorkflowLinkList) {
                    odsWorkflowNodesList.removeIf(node -> node.getId().equals(odsWorkflowLink.getSourceNode()) || node.getId().equals(odsWorkflowLink.getDestNode()));
                }

                Map<Integer, Element> mle = new HashMap<>();
                int ii = 1;
                Element e1 = null;
                Element e2 = null;
                for (OdsWorkflowLink odsWorkflowLink : odsWorkflowLinkList) {

                    if (!mle.containsKey(odsWorkflowLink.getSourceNode()) && Objects.nonNull(odsWorkflowLink.getSourceNode())) {
                        OdsWorkflowNode node = odsWorkflowNodeRepository.getById(odsWorkflowLink.getSourceNode());
                        e1 = new Element(node, "10em", 10 * ii + "em");
                        ii++;
                        e1.addEndPoint(createDotEndPoint(EndPointAnchor.BOTTOM));
                        e1.addEndPoint(createRectangleEndPoint(EndPointAnchor.LEFT));
                        if (node.getNodeName().equalsIgnoreCase(odsWorkflowInstanceNodeActive.getNodeName())) {
                            e1.setStyleClass("my-box-green-active");
                            e1.setId(String.valueOf(odsWorkflowInstanceNodeActive.getId()));
                        } else {
                            e1.setStyleClass("my-box-green-inactive");
                        }
                        mle.put(odsWorkflowLink.getSourceNode(), e1);
                        modelForTraveler.addElement(e1);
                    } else {
                        e1 = mle.get(odsWorkflowLink.getSourceNode());
                    }
                    String slabel = "";

                    if (!mle.containsKey(odsWorkflowLink.getDestNode()) && Objects.nonNull(odsWorkflowLink.getDestNode())) {
                        OdsWorkflowNode node = odsWorkflowNodeRepository.getById(odsWorkflowLink.getDestNode());
                        e2 = new Element(node, 5 * ii + "em", 10 * ii + "em");
                        ii++;
                        e2.addEndPoint(createDotEndPoint(EndPointAnchor.BOTTOM));
                        e2.addEndPoint(createRectangleEndPoint(EndPointAnchor.LEFT));
                        if (node.getNodeName().equalsIgnoreCase(odsWorkflowInstanceNodeActive.getNodeName())) {
                            e2.setStyleClass("my-box-green-active");
                            e2.setId(String.valueOf(odsWorkflowInstanceNodeActive.getId()));
                        } else
                            e2.setStyleClass("my-box-green-inactive");
                        mle.put(odsWorkflowLink.getDestNode(), e2);

                        modelForTraveler.addElement(e2);

                    } else {
                        e2 = mle.get(odsWorkflowLink.getDestNode());
                    }


                    if (!Objects.nonNull(odsWorkflowLink.getSourceNode())) {
                        modelForTraveler.connect(createConnection(start.getEndPoints().get(0), e2.getEndPoints().get(1), null));
                    } else if (!Objects.nonNull(odsWorkflowLink.getDestNode())) {
                        Element end = new Element("End", "4em", 10 * ii + "em");
                        end.addEndPoint(createRectangleEndPoint(EndPointAnchor.TOP));

                        modelForTraveler.addElement(end);

                        modelForTraveler.connect(createConnection(e1.getEndPoints().get(0), end.getEndPoints().get(0), null));
                    } else {


                        modelForTraveler.connect(createConnection(e1.getEndPoints().get(0), e2.getEndPoints().get(1), null));
                    }
                }

            } else {

                Element end = new Element("End", "4em", 15 + "em");
                end.addEndPoint(createRectangleEndPoint(EndPointAnchor.TOP));
                modelForTraveler.addElement(end);
            }
        } else if (Objects.nonNull(odsWorkflowInstanceActive)) {
            modelForTraveler.addElement(start);
            List<OdsWorkflowLink> odsWorkflowLinkList = odsWorkflowLinkRepository.findAllByWorkflowId(String.valueOf(odsWorkflowRepository.findFirstByWorkflowNameEqualsIgnoreCase(odsWorkflowInstanceActive.getWorkflowName()).getId()));
            if (!odsWorkflowLinkList.isEmpty()) {

                for (OdsWorkflowLink odsWorkflowLink : odsWorkflowLinkList) {
                    odsWorkflowNodesList.removeIf(node -> node.getId().equals(odsWorkflowLink.getSourceNode()) || node.getId().equals(odsWorkflowLink.getDestNode()));
                }

                Map<Integer, Element> mle = new HashMap<>();
                int ii = 1;
                Element e1 = null;
                Element e2 = null;
                for (OdsWorkflowLink odsWorkflowLink : odsWorkflowLinkList) {

                    if (!mle.containsKey(odsWorkflowLink.getSourceNode()) && Objects.nonNull(odsWorkflowLink.getSourceNode())) {
                        OdsWorkflowNode node = odsWorkflowNodeRepository.getById(odsWorkflowLink.getSourceNode());
                        e1 = new Element(node, "10em", 10 * ii + "em");
                        ii++;
                        e1.addEndPoint(createDotEndPoint(EndPointAnchor.BOTTOM));
                        e1.addEndPoint(createRectangleEndPoint(EndPointAnchor.LEFT));
                        e1.setId(String.valueOf(node.getId()));
                        e1.setStyleClass("my-box-green-inactive");

                        mle.put(odsWorkflowLink.getSourceNode(), e1);
                        modelForTraveler.addElement(e1);
                    } else {
                        e1 = mle.get(odsWorkflowLink.getSourceNode());
                    }
                    String slabel = "";

                    if (!mle.containsKey(odsWorkflowLink.getDestNode()) && Objects.nonNull(odsWorkflowLink.getDestNode())) {
                        OdsWorkflowNode node = odsWorkflowNodeRepository.getById(odsWorkflowLink.getDestNode());
                        e2 = new Element(node, 5 * ii + "em", 10 * ii + "em");
                        ii++;
                        e2.addEndPoint(createDotEndPoint(EndPointAnchor.BOTTOM));
                        e2.addEndPoint(createRectangleEndPoint(EndPointAnchor.LEFT));
                        e2.setId(String.valueOf(node.getId()));
                        e2.setStyleClass("my-box-green-inactive");
                        mle.put(odsWorkflowLink.getDestNode(), e2);

                        modelForTraveler.addElement(e2);

                    } else {
                        e2 = mle.get(odsWorkflowLink.getDestNode());
                    }


                    if (!Objects.nonNull(odsWorkflowLink.getSourceNode())) {
                        modelForTraveler.connect(createConnection(start.getEndPoints().get(0), e2.getEndPoints().get(1), null));
                    } else if (!Objects.nonNull(odsWorkflowLink.getDestNode())) {
                        Element end = new Element("End", "4em", 10 * ii + "em");
                        end.addEndPoint(createRectangleEndPoint(EndPointAnchor.TOP));

                        modelForTraveler.addElement(end);

                        modelForTraveler.connect(createConnection(e1.getEndPoints().get(0), end.getEndPoints().get(0), null));
                    } else {


                        modelForTraveler.connect(createConnection(e1.getEndPoints().get(0), e2.getEndPoints().get(1), null));
                    }
                }

            } else {

                Element end = new Element("End", "4em", 15 + "em");
                end.addEndPoint(createRectangleEndPoint(EndPointAnchor.TOP));
                modelForTraveler.addElement(end);
            }
        }

    }

    public void elementSelected() {
        String id = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("elementId");
        id = id.substring(13);
        if(Objects.nonNull(odsWorkflowInstanceNodeActive) && id.equalsIgnoreCase(String.valueOf(odsWorkflowInstanceNodeActive.getId()))){
            PrimeFaces.current().executeScript("PF('dialogElementDetails').show()");
        }
    }



}
