package ro.simavi.odysseus.platform.controllers;


import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.context.WebApplicationContext;
import ro.simavi.odysseus.platform.entities.OdsAuditLog;
import ro.simavi.odysseus.platform.repositories.OdsAuditLogRepository;
import ro.simavi.odysseus.platform.servicesImpl.OdsAuditLogServiceImpl;

import javax.annotation.PostConstruct;
import java.util.List;

@Controller
@Setter
@Getter
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class OdsAuditLogsController {

    private final OdsAuditLogServiceImpl odsAuditLogService;
    private final OdsAuditLogRepository odsAuditLogRepository;
    private OdsAuditLog odsAuditLogSelected;
    private List<OdsAuditLog> odsAuditLogList;

    private String nameFilter;

    @Autowired
    public OdsAuditLogsController(OdsAuditLogServiceImpl odsAuditLogService, OdsAuditLogRepository odsAuditLogRepository) {
        this.odsAuditLogService = odsAuditLogService;
        this.odsAuditLogRepository = odsAuditLogRepository;

    }

    @PostConstruct
    private void init() {
        odsAuditLogList =  odsAuditLogRepository.findLast10000();
    }

    public void onPageLoad(){
        odsAuditLogList =  odsAuditLogRepository.findLast10000();
    }

    public void filter(){
        if(nameFilter != null){
            odsAuditLogList = odsAuditLogRepository.findAllByShortTextContainingIgnoreCaseOrLogTextContainingIgnoreCaseOrLogTypeContainingIgnoreCaseOrAuditLogcolContainingIgnoreCaseOrCreatedByContainsIgnoreCaseOrOperationResultContainsIgnoreCaseOrDocumentIdContainsIgnoreCaseOrTravelerIdContainsIgnoreCase(nameFilter, nameFilter, nameFilter, nameFilter, nameFilter, nameFilter, nameFilter, nameFilter);
        }else {
            odsAuditLogList = odsAuditLogRepository.findLast10000();
        }
    }

    public void reset(){
        odsAuditLogList = odsAuditLogRepository.findLast10000();
        nameFilter = null;
    }
}
