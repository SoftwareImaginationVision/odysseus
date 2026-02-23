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

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.time.Instant;
import java.util.*;
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

    private KeycloackRestCalls keycloackRestCalls;

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

    private int mouseX;
    private int mouseY;

    @PostConstruct
    public void init(){
        keycloackRestCalls = new KeycloackRestCalls();
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
                    "Node test cu type 2",
                    Instant.parse("2024-04-25T08:30:00Z"),
                    "Text for Node cu type",
                    "1", "database node"
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
        if(odsWorkflowInstanceRepository.existsByInstanceNameAndWorkflowStatus(keycloackRestCalls.getUsername(),"Active"))
            odsWorkflowInstanceActive = odsWorkflowInstanceRepository.findFirstByInstanceNameAndWorkflowStatus(keycloackRestCalls.getUsername(),"Active");
        else if (odsWorkflowInstanceRepository.existsByInstanceNameAndWorkflowStatus(keycloackRestCalls.getUsername(),"Inactive")) {
            odsWorkflowInstanceActive = odsWorkflowInstanceRepository.findTopByInstanceNameAndWorkflowStatusOrderByIdDesc(keycloackRestCalls.getUsername(),"Inactive");
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

    public void saveOdsWorkflow(){
        if(Objects.nonNull(odsWorkflowSelected)){
            OdsWorkflow odsWorkflow = odsWorkflowService.saveOrEditOdsWorkflow(odsWorkflowSelected);
            odsWorkflowsList = odsWorkflowRepository.findAll();
            PrimeFaces.current().ajax().update("odsWorkflowForm:dataTableOdsWorkflow");
            FacesMessage msg =new FacesMessage("Successfully saved!");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
    }

    public void deleteOdsWorkflow(){
        if(Objects.nonNull(odsWorkflowSelected)){
            odsWorkflowService.deleteOdsWorkflow(odsWorkflowSelected);
            odsWorkflowsList.remove(odsWorkflowSelected);
            odsWorkflowSelected = null;
            PrimeFaces.current().ajax().update("odsWorkflowForm:dataTableOdsWorkflow");
            FacesMessage msg =new FacesMessage("Successfully deleted!");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
    }

    public void createNewOdsWorkflow(){
        this.odsWorkflowSelected = new OdsWorkflow();
    }

    public void filter(){
        if(nameFilter != null){
            odsWorkflowsList = odsWorkflowRepository.findAllByWorkflowNameContainingIgnoreCase(nameFilter);
        }else {
            odsWorkflowsList = odsWorkflowRepository.findAll();
        }
    }

    public void reset(){
        odsWorkflowsList = odsWorkflowRepository.findAll();
        nameFilter = null;
    }

    public void saveOdsWorkflowNode(){
        if(Objects.nonNull(odsWorkflowNodeSelected)){
            OdsWorkflowNode odsWorkflowNode = odsWorkflowNodeService.saveOrEditOdsWorkflowNode(odsWorkflowNodeSelected);
            odsWorkflowNodesList = odsWorkflowNodeRepository.findAll();
            PrimeFaces.current().ajax().update("form:odsWorkFlowNodeData");
            FacesMessage msg =new FacesMessage("Successfully saved!");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
    }
    public void editOdsWorkflowNodeSelected() throws Exception {
        if(Objects.nonNull(selectedNode)){
            OdsWorkflowNode odsWorkflowNode = odsWorkflowNodeService.editOdsWorkflowNode(selectedNode);
            PrimeFaces.current().ajax().update("form:odsWorkFlowNodeData");
            FacesMessage msg =new FacesMessage("Successfully saved!");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
    }




    public void deleteOdsWorkflowNode(){
        if(Objects.nonNull(odsWorkflowNodeSelected)){
            odsWorkflowNodeService.deleteOdsWorkflowNode(odsWorkflowNodeSelected);
            odsWorkflowNodesList.remove(odsWorkflowNodeSelected);
            odsWorkflowNodeSelected = null;
            PrimeFaces.current().ajax().update("form:odsWorkFlowNodeData");
            FacesMessage msg =new FacesMessage("Successfully deleted!");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
    }

    public void createNewOdsWorkflowNode(){
        this.odsWorkflowNodeSelected = new OdsWorkflowNode();
    }

    public void filterOdsWorkflowNode(){
        if(nameFilterForWorkflowNode != null){
            odsWorkflowNodesList = odsWorkflowNodeRepository.findAllByNodeNameContainingIgnoreCaseOrNodeTextContainingIgnoreCase(nameFilterForWorkflowNode, nameFilterForWorkflowNode);
        }else {
            odsWorkflowNodesList = odsWorkflowNodeRepository.findAll();
        }
    }

    public void resetOdsWorkflowNode(){
        odsWorkflowNodesList = odsWorkflowNodeRepository.findAll();
        nameFilterForWorkflowNode = null;
    }

    public void saveOdsWorkflowLink(){
        if(Objects.nonNull(odsWorkflowLinkSelected)){
            OdsWorkflowLink odsWorkflowLink = odsWorkflowLinkService.saveOrEditOdsWorkflowLink(odsWorkflowLinkSelected);
            FacesMessage msg =new FacesMessage("Successfully saved!");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
    }

    public void deleteOdsWorkflowLink(OdsWorkflowLink odsWorkflowLink){
        if(Objects.nonNull(odsWorkflowLink)){
            odsWorkflowLinkService.deleteOdsWorkflowLink(odsWorkflowLink);
            FacesMessage msg =new FacesMessage("Successfully deleted!");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
    }

    public void onProductDrop(DragDropEvent<OdsWorkflowNode> ddEvent) {
       OdsWorkflowNode odsWorkflowNode = ddEvent.getData();

       List<String> nodeTypes = List.of("Decision", "Merge",  "Data Storage", "End", "Start", "Fork", "Join", "Time", "Incoming Event", "Outgoing Event");
        if (!Objects.isNull(odsWorkflowNode.getNodeName())) {
            if(odsWorkflowNode.getNodeType().equalsIgnoreCase("Process")) {
                Element e1 = createElementWithEndpoints(odsWorkflowNode, "rectangle-with-lines");
                e1.setId(String.valueOf(odsWorkflowNode.getId()));
                model.addElement(e1);
                odsWorkflowNodesList.remove(odsWorkflowNode);
            }
            else if (odsWorkflowNode.getNodeType().equalsIgnoreCase("Card")) {
                Element e1 = createElementWithEndpoints(odsWorkflowNode, "rectangle-cut-corner");
                e1.setId(String.valueOf(odsWorkflowNode.getId()));
                model.addElement(e1);
                odsWorkflowNodesList.remove(odsWorkflowNode);
            }
            else if (nodeTypes.contains(odsWorkflowNode.getNodeType())){
                Element e1 = createElementWithEndpoints(odsWorkflowNode, "ui-diagram-element_special_case");
                e1.setId(String.valueOf(odsWorkflowNode.getId()));
                model.addElement(e1);
                odsWorkflowNodesList.remove(odsWorkflowNode);
            }
            else {
                Element e1 = createElementWithEndpoints(odsWorkflowNode, "ui-diagram-element");
                e1.setId(String.valueOf(odsWorkflowNode.getId()));

                model.addElement(e1);
                odsWorkflowNodesList.remove(odsWorkflowNode);
            }
        } else {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "Error", "The node name must not be null");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            PrimeFaces.current().ajax().update("form:msgs");
        }
    }

    private Element createElementWithEndpoints(OdsWorkflowNode odsWorkflowNode, String styleClass) {

        Element e1 = new Element(odsWorkflowNode, mouseX + "px", mouseY + "px");
        e1.setDraggable(true);
        e1.setStyleClass(styleClass);

        if (odsWorkflowNode.getNodeType().equalsIgnoreCase("Start")) {
            EndPoint endPointBottom = createDotEndPoint(EndPointAnchor.BOTTOM);
            endPointBottom.setSource(true);
            e1.addEndPoint(endPointBottom);

        }
        else if (odsWorkflowNode.getNodeType().equalsIgnoreCase("End")) {

            EndPoint endPointTop = createRectangleEndPoint(EndPointAnchor.TOP);
            endPointTop.setTarget(true);
            e1.addEndPoint(endPointTop);

        }
        else {

            EndPoint endPointBottom = createDotEndPoint(EndPointAnchor.BOTTOM);
            EndPoint endPointTop = createRectangleEndPoint(EndPointAnchor.TOP);
            endPointTop.setTarget(true);
            endPointBottom.setSource(true);
            e1.addEndPoint(endPointTop);
            e1.addEndPoint(endPointBottom);
        }

        return e1;
    }




    public void onConnect(ConnectEvent event) {
        if (!suspendEvent) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Connected",
                    "From " + event.getSourceElement().getData() + " To " + event.getTargetElement().getData());

            if(event.getSourceElement().getData().toString().equalsIgnoreCase("start")) {
                odsWorkflowLinkSelected = new OdsWorkflowLink();
                OdsWorkflowNode odsWorkflowNodeTarget = (OdsWorkflowNode) event.getTargetElement().getData();
                odsWorkflowLinkSelected.setDestNode(odsWorkflowNodeTarget.getId());
                odsWorkflowLinkSelected.setWorkflowId(String.valueOf(this.odsWorkflowSelected.getId()));
                PrimeFaces.current().ajax().update("dialogsOdsWorkflowLink");
                PrimeFaces.current().executeScript("PF('manageOdsWorkflowLink').show()");
                FacesContext.getCurrentInstance().addMessage(null, msg);
                PrimeFaces.current().ajax().update("form:msgs");
            } else if (event.getTargetElement().getData().toString().equalsIgnoreCase("end")) {
                odsWorkflowLinkSelected = new OdsWorkflowLink();
                OdsWorkflowNode odsWorkflowNodeSource = (OdsWorkflowNode) event.getSourceElement().getData();
                odsWorkflowLinkSelected.setSourceNode(odsWorkflowNodeSource.getId());
                odsWorkflowLinkSelected.setWorkflowId(String.valueOf(this.odsWorkflowSelected.getId()));
                PrimeFaces.current().ajax().update("dialogsOdsWorkflowLink");
                PrimeFaces.current().executeScript("PF('manageOdsWorkflowLink').show()");
                FacesContext.getCurrentInstance().addMessage(null, msg);
                PrimeFaces.current().ajax().update("form:msgs");

            } else {
                odsWorkflowLinkSelected = new OdsWorkflowLink();
                OdsWorkflowNode odsWorkflowNodeSource = (OdsWorkflowNode) event.getSourceElement().getData();
                OdsWorkflowNode odsWorkflowNodeTarget = (OdsWorkflowNode) event.getTargetElement().getData();
                odsWorkflowLinkSelected.setSourceNode(odsWorkflowNodeSource.getId());
                odsWorkflowLinkSelected.setDestNode(odsWorkflowNodeTarget.getId());
                odsWorkflowLinkSelected.setWorkflowId(String.valueOf(this.odsWorkflowSelected.getId()));
                PrimeFaces.current().ajax().update("dialogsOdsWorkflowLink");
                PrimeFaces.current().executeScript("PF('manageOdsWorkflowLink').show()");
                FacesContext.getCurrentInstance().addMessage(null, msg);
                PrimeFaces.current().ajax().update("form:msgs");
            }

        }
        else {
            suspendEvent = false;
        }
    }

    public void onDisconnect(DisconnectEvent event) {
        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Disconnected",
                "From " + event.getSourceElement().getData() + " To " + event.getTargetElement().getData());


        if(event.getSourceElement().getData().toString().equalsIgnoreCase("start")) {
            OdsWorkflowNode odsWorkflowNodeTarget = (OdsWorkflowNode) event.getTargetElement().getData();
            OdsWorkflowLink odsWorkflowLink = this.odsWorkflowLinkRepository.findFirstByDestNodeAndWorkflowId(odsWorkflowNodeTarget.getId(), String.valueOf(this.odsWorkflowSelected.getId()));
            deleteOdsWorkflowLink(odsWorkflowLink);
            FacesContext.getCurrentInstance().addMessage(null, msg);
            PrimeFaces.current().ajax().update("form:msgs");
        } else if (event.getTargetElement().getData().toString().equalsIgnoreCase("end")) {
            OdsWorkflowNode odsWorkflowNodeSource = (OdsWorkflowNode) event.getSourceElement().getData();
            OdsWorkflowLink odsWorkflowLink = this.odsWorkflowLinkRepository.findFirstBySourceNodeAndWorkflowId(odsWorkflowNodeSource.getId(), String.valueOf(this.odsWorkflowSelected.getId()));
            deleteOdsWorkflowLink(odsWorkflowLink);
            FacesContext.getCurrentInstance().addMessage(null, msg);
            PrimeFaces.current().ajax().update("form:msgs");

        }else {
            OdsWorkflowNode odsWorkflowNodeSource = (OdsWorkflowNode) event.getSourceElement().getData();
            OdsWorkflowNode odsWorkflowNodeTarget = (OdsWorkflowNode) event.getTargetElement().getData();
            OdsWorkflowLink odsWorkflowLink = this.odsWorkflowLinkRepository.findFirstBySourceNodeAndDestNodeAndWorkflowId(odsWorkflowNodeSource.getId(), odsWorkflowNodeTarget.getId(), String.valueOf(this.odsWorkflowSelected.getId()));
            deleteOdsWorkflowLink(odsWorkflowLink);
            FacesContext.getCurrentInstance().addMessage(null, msg);
            PrimeFaces.current().ajax().update("form:msgs");
        }
    }
    /*
    public void createDiagram(){
        // first add start and stop
        Element start = new Element("Start", "4em", "2em");
        EndPoint endPointStart = createDotEndPoint(EndPointAnchor.BOTTOM);
        start.addEndPoint(endPointStart);

        model.addElement(start);

        List<OdsWorkflowLink> odsWorkflowLinkList = odsWorkflowLinkRepository.findAllByWorkflowId(String.valueOf(odsWorkflowSelected.getId()));
        if(!odsWorkflowLinkList.isEmpty()){

            for (OdsWorkflowLink odsWorkflowLink : odsWorkflowLinkList){
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
                    e1.setStyleClass("my-box-green");
                    mle.put(odsWorkflowLink.getSourceNode(), e1);
                    model.addElement(e1);
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
                    e2.setStyleClass("my-box-blue");
                    mle.put(odsWorkflowLink.getDestNode(), e2);

                    model.addElement(e2);

                } else {
                    e2 = mle.get(odsWorkflowLink.getDestNode());
                }


                if (!Objects.nonNull(odsWorkflowLink.getSourceNode())) {
                    model.connect(createConnection(start.getEndPoints().get(0), e2.getEndPoints().get(1), null));
                } else if (!Objects.nonNull(odsWorkflowLink.getDestNode())) {
                    Element end = new Element("End", "4em", 10 * ii + "em");
                    end.addEndPoint(createRectangleEndPoint(EndPointAnchor.TOP));

                    model.addElement(end);

                    model.connect(createConnection(e1.getEndPoints().get(0), end.getEndPoints().get(0), null));
                }else {


                    model.connect(createConnection(e1.getEndPoints().get(0), e2.getEndPoints().get(1), null));
                }
            }

        }else {

            Element end = new Element("End", "4em", 15 + "em");
            end.addEndPoint(createRectangleEndPoint(EndPointAnchor.TOP));
            model.addElement(end);
        }
    }
     */
    public void createDiagram() {

        List<OdsWorkflowLink> odsWorkflowLinkList = odsWorkflowLinkRepository.findAllByWorkflowId(String.valueOf(odsWorkflowSelected.getId()));
        List<String> nodeTypes = List.of("Decision", "Merge", "Data Storage", "End", "Start", "Fork", "Join", "Time", "Incoming Event", "Outgoing Event");

        if (!odsWorkflowLinkList.isEmpty()) {

            // Îndepărtează nodurile deja conectate pentru a evita dublarea acestora în diagramă
            for (OdsWorkflowLink odsWorkflowLink : odsWorkflowLinkList) {
                odsWorkflowNodesList.removeIf(node -> node.getId().equals(odsWorkflowLink.getSourceNode()) || node.getId().equals(odsWorkflowLink.getDestNode()));
            }

            Map<Integer, Element> mle = new HashMap<>();
            int ii = 1;
            Element e1 = null;
            Element e2 = null;

            for (OdsWorkflowLink odsWorkflowLink : odsWorkflowLinkList) {
                // Creeaza elementul sursă (e1)
                if (!mle.containsKey(odsWorkflowLink.getSourceNode()) && Objects.nonNull(odsWorkflowLink.getSourceNode())) {
                    OdsWorkflowNode node = odsWorkflowNodeRepository.getById(odsWorkflowLink.getSourceNode());
                    e1 = new Element(node, "10em", 10 * ii + "em");
                    ii++;

                    if (node.getNodeType().equalsIgnoreCase("Start")) {
                        e1.addEndPoint(createDotEndPoint(EndPointAnchor.RIGHT));
                    }
                    e1.setTitle(node.getNodeType());

                    if (nodeTypes.contains(node.getNodeType())) {
                        e1.setStyleClass("ui-diagram-element_special_case");
                    } else if (node.getNodeType().equalsIgnoreCase("Process")) {
                        e1.setStyleClass("rectangle-with-lines");
                    } else if (node.getNodeType().equalsIgnoreCase("Card")) {
                        e1.setStyleClass("rectangle-cut-corner");
                    } else {
                        e1.setStyleClass("ui-diagram-element");
                    }

                    e1.setId(String.valueOf(node.getId()));
                    mle.put(odsWorkflowLink.getSourceNode(), e1);
                    model.addElement(e1);
                } else {
                    e1 = mle.get(odsWorkflowLink.getSourceNode());
                }

                // Creează elementul destinație (e2)
                if (!mle.containsKey(odsWorkflowLink.getDestNode()) && Objects.nonNull(odsWorkflowLink.getDestNode())) {
                    OdsWorkflowNode node = odsWorkflowNodeRepository.getById(odsWorkflowLink.getDestNode());
                    e2 = new Element(node, 5 * ii + "em", 10 * ii + "em");
                    ii++;
                    e2.setTitle(node.getNodeType());

                    if (node.getNodeType().equalsIgnoreCase("End")) {
                        e2.addEndPoint(createRectangleEndPoint(EndPointAnchor.LEFT));
                    } else {
                        e2.addEndPoint(createDotEndPoint(EndPointAnchor.RIGHT));
                        e2.addEndPoint(createRectangleEndPoint(EndPointAnchor.LEFT));
                    }

                    if (nodeTypes.contains(node.getNodeType())) {
                        e2.setStyleClass("ui-diagram-element_special_case");
                    } else if (node.getNodeType().equalsIgnoreCase("Process")) {
                        e2.setStyleClass("rectangle-with-lines");
                    } else if (node.getNodeType().equalsIgnoreCase("Card")) {
                        e2.setStyleClass("rectangle-cut-corner");
                    } else {
                        e2.setStyleClass("ui-diagram-element");
                    }

                    e2.setId(String.valueOf(node.getId()));
                    mle.put(odsWorkflowLink.getDestNode(), e2);
                    model.addElement(e2);
                } else {
                    e2 = mle.get(odsWorkflowLink.getDestNode());
                }

                // Conectează elementele (nodurile)
                if (e1 != null && e2 != null) {
                    if (e2.getTitle().contains("End")) {
                        if (!e1.getEndPoints().isEmpty() && !e2.getEndPoints().isEmpty()) {
                            model.connect(createConnection(e1.getEndPoints().get(0), e2.getEndPoints().get(0), odsWorkflowLink.getLinkText()));
                        }
                    } else {
                        if (!e1.getEndPoints().isEmpty() && e2.getEndPoints().size() > 1) {
                            model.connect(createConnection(e1.getEndPoints().get(0), e2.getEndPoints().get(1), odsWorkflowLink.getLinkText()));
                        }
                    }
                }

            }
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

    public void resetWorkflow(){
        model = new DefaultDiagramModel();

        model.setMaxConnections(-1);
        model.setContainment(false);

        model.getDefaultConnectionOverlays().add(new ArrowOverlay(20, 20, 1, 1));

        StraightConnector connector = new StraightConnector();
        connector.setPaintStyle("{stroke:'#C7B097',strokeWidth:3}");
        connector.setHoverPaintStyle("{stroke:'#5C738B'}");
        model.setDefaultConnector(connector);

        odsWorkflowNodesList = odsWorkflowNodeRepository.findAll();
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

    public void resetModelForBCP() throws IOException {
        modelForBCP = new DefaultDiagramModel();

        modelForBCP.setMaxConnections(-1);
        modelForBCP.setContainment(false);

        modelForBCP.getDefaultConnectionOverlays().add(new ArrowOverlay(20, 20, 1, 1));

        StraightConnector connectorForBCP = new StraightConnector();
        connectorForBCP.setPaintStyle("{stroke:'#C7B097',strokeWidth:3}");
        connectorForBCP.setHoverPaintStyle("{stroke:'#5C738B'}");
        modelForBCP.setDefaultConnector(connectorForBCP);

        nameOfSelectedWorkflowInstanceActive = "";

        String path = FacesContext.getCurrentInstance().getExternalContext().getRequest().toString();

        if(path.contains("pages")){
            FacesContext.getCurrentInstance().getExternalContext().redirect("verifyStatusForBCP.xhtml");
        }else {
            FacesContext.getCurrentInstance().getExternalContext().redirect("pages/verifyStatusForBCP.xhtml");
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

    public void onWorkflowChange(){
        modelForBCP = new DefaultDiagramModel();

        modelForBCP.setMaxConnections(-1);
        modelForBCP.setContainment(false);

        modelForBCP.getDefaultConnectionOverlays().add(new ArrowOverlay(20, 20, 1, 1));

        StraightConnector connectorForBCP = new StraightConnector();
        connectorForBCP.setPaintStyle("{stroke:'#C7B097',strokeWidth:3}");
        connectorForBCP.setHoverPaintStyle("{stroke:'#5C738B'}");
        modelForBCP.setDefaultConnector(connectorForBCP);

        PrimeFaces.current().executeScript("PF('showButton').enable()");
    }

    public void createDiagramForBCP(){
        // first add start and stop
        Element start = new Element("Start", "4em", "2em");
        EndPoint endPointStart = createDotEndPoint(EndPointAnchor.BOTTOM);
        start.addEndPoint(endPointStart);
        start.setId(String.valueOf(0));

        if(Objects.nonNull(nameOfSelectedWorkflowInstanceActive) && !nameOfSelectedWorkflowInstanceActive.isEmpty()) {
            modelForBCP.addElement(start);
            List<OdsWorkflowLink> odsWorkflowLinkList = odsWorkflowLinkRepository.findAllByWorkflowId(String.valueOf(odsWorkflowRepository.findFirstByWorkflowNameEqualsIgnoreCase(nameOfSelectedWorkflowInstanceActive).getId()));
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
                        e1.setStyleClass("my-box-blue");
                        e1.setId(String.valueOf(node.getId()));
                        mle.put(odsWorkflowLink.getSourceNode(), e1);
                        modelForBCP.addElement(e1);
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
                        e2.setStyleClass("my-box-orange");
                        e2.setId(String.valueOf(node.getId()));
                        mle.put(odsWorkflowLink.getDestNode(), e2);

                        modelForBCP.addElement(e2);

                    } else {
                        e2 = mle.get(odsWorkflowLink.getDestNode());
                    }


                    if (!Objects.nonNull(odsWorkflowLink.getSourceNode())) {
                        modelForBCP.connect(createConnection(start.getEndPoints().get(0), e2.getEndPoints().get(1), null));
                    } else if (!Objects.nonNull(odsWorkflowLink.getDestNode())) {
                        Element end = new Element("End", "4em", 10 * ii + "em");
                        end.addEndPoint(createRectangleEndPoint(EndPointAnchor.TOP));
                        end.setId(String.valueOf(-1));

                        modelForBCP.addElement(end);

                        modelForBCP.connect(createConnection(e1.getEndPoints().get(0), end.getEndPoints().get(0), null));
                    } else {


                        modelForBCP.connect(createConnection(e1.getEndPoints().get(0), e2.getEndPoints().get(1), null));
                    }
                }

            } else {

                Element end = new Element("End", "4em", 15 + "em");
                end.addEndPoint(createRectangleEndPoint(EndPointAnchor.TOP));
                end.setId(String.valueOf(-1));
                modelForBCP.addElement(end);
            }
        }
    }
    public void elementSelectedForWorkflow() {
        String id = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("elementId");
        id = id.substring(13);
        System.out.println(id);

        if(odsWorkflowNodeRepository.existsById(Integer.valueOf(id))){
            selectedNode = odsWorkflowNodeRepository.getById(Integer.valueOf(id));
            PrimeFaces.current().ajax().update("elementDetailsWF:dialogElementDetailsWF");
            PrimeFaces.current().executeScript("PF('dialogElementDetailsWF').show()");

        }

    }



    public void elementSelectedForBCP() {
        String id = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("elementId");
        System.out.println(id);
        id = id.substring(13);

        if(odsWorkflowNodeRepository.existsById(Integer.valueOf(id))){
            selectedNode = odsWorkflowNodeRepository.getById(Integer.valueOf(id));
            numberOfActiveNodeInstance = odsWorkflowInstanceNodeRepository.countOdsWorkflowInstanceNodeByNodeNameEqualsIgnoreCaseAndNodeStatusEqualsIgnoreCase(selectedNode.getNodeName(),"Active");
            totalNodeInstance = odsWorkflowInstanceNodeRepository.countOdsWorkflowInstanceNodeByNodeNameEqualsIgnoreCase(selectedNode.getNodeName());
            PrimeFaces.current().ajax().update("elementDetailsBCP:dialogElementDetailsBCP");
            PrimeFaces.current().executeScript("PF('dialogElementDetailsBCP').show()");

        }
    }

    // ActionListener method to receive coordinates
    // This method is called when coordinates are sent from the frontend
    public void receiveCoordinates() {
        // Get the mouse coordinates from the request parameters
        Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        this.mouseX = Integer.parseInt(params.get("mouseX"));
        this.mouseY = Integer.parseInt(params.get("mouseY"));

        // Log the coordinates or handle them for positioning elements
        System.out.println("Received Coordinates - X: " + mouseX + ", Y: " + mouseY);

        // Use the coordinates to position an element, save to DB, or perform any other logic
    }





}
