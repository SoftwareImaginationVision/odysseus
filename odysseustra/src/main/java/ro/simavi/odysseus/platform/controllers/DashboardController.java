package ro.simavi.odysseus.platform.controllers;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import ro.simavi.odysseus.platform.entities.HomeData;
import ro.simavi.odysseus.platform.entities.HomeSetting;
import ro.simavi.odysseus.platform.services.HomeDataService;


import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import java.util.*;

@Getter
@Setter
@Component
@Scope("session")
public class DashboardController {

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private HomeDataService auditDataService;

    private Date fromDate;

    private Date toDate;

    private String currentOwner;

    private String currentApp;

    private String currentModule;
    private HomeSetting auditSetting;

    public HomeSetting getAuditSetting()
    {
        return auditSetting;
    }

    public void setAuditSetting( HomeSetting auditSetting )
    {
        this.auditSetting = auditSetting;
    }

    public List<HomeData> getAuditDataList()
    {
        return auditDataList;
    }

    public void setAuditDataList( List<HomeData> auditDataList )
    {
        this.auditDataList = auditDataList;
    }

    List<HomeData> auditDataList;

    List<HomeData> selectedAuditDataList;

    @PostConstruct
    private void postConstruct(){
        auditDataList = auditDataService.homeData();
    }

    public boolean isRendered(String place){
        boolean bret;
//        //Logger.getLogger(this.getClass().getName()).info("ROLES-->>"+userRoles.toString());
//        switch (place){
//            case "menu.variables":
//                bret= isVariableAccesible();
//                break;
//            case "page.ws":
//                bret= isWSAccesible();
//                break;
//            case "menu.categ":
//                bret= isCategAccesible();
//                break;
//            case "menu.questions":
//                bret= isQuestionAccesible();
//                break;
//            case "menu.questionnaires":
//                bret= isQuestionnaireAccesible();
//                break;
//            case "menu.translations":
//                bret= isTranslationAccesible();
//                break;
//            case "menu.data-upload":
//                bret= isDataUploadAccesible();
//                break;
//            case "menu.result-sets":
//                bret= isDataresultsAccesible();
//                break;
//            case "menu.analytics":
//                bret= isAnalyticsAccesible();
//                break;
//            case "menu.qb":
//                bret= isQbAccesible();
//                break;
//            case "menu.es":
//                bret= isESAccesible();
//                break;
//            case "menu.EUSurvey":
//                bret= isEUSurveyAccesible();
//                break;
//            case "menu.mesco-admin":
//                bret= isAadminAccesible();
//                break;
//            case "menu.util":
//                bret= isAadminAccesible();
//                break;
//            case "menu.results":
//                bret= isDataresultsAccesible();
//                break;
//            case "menu.all-metadata":
//                bret= isAllMetaDataAccesible();
//                break;
//            case "menu.all-data":
//                bret= isAllDataAccesible();
//                break;
//            case "menu.all-analysis":
//                bret= isAllAnalysisAccesible();
//                break;
//            case "menu.clinical":
//                bret= isAllAnalysisAccesible();
//                break;
//            case "menu.diagnosis":
//                bret= isAllAnalysisAccesible();
//                break;
//            case "menu.mesco-tests":
//                bret= isTests();
//                break;
//            case "menu.kb":
//                bret= isKbAccesible();
//                break;
//            case "menu.options":
//                bret= isTests();
//                break;
//            case "menu.datasets":
//                bret= isDatasetsAccesible();
//                break;
//            default:
//                bret= true;
//        }
    return true;
    }


    public void save(){
        return;
    }

    public void update(){
        return;
    }

    public void filterContext() {
        return;}

    public void resetFilter() {
        return;}

    public void showMessage(String summary, String detail){
        FacesMessage msg = new FacesMessage(summary , detail);
        FacesContext.getCurrentInstance().addMessage(null,msg);
    }

    public void showMessage(String summary){
        FacesMessage msg = new FacesMessage(summary);
        FacesContext.getCurrentInstance().addMessage(null,msg);
    }
    public void showMessage(String summary, Object detail){
        FacesMessage msg = new FacesMessage(summary , detail.toString());
        FacesContext.getCurrentInstance().addMessage(null,msg);
    }


}

