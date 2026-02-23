package ro.simavi.odysseus.platform.servicesImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ro.simavi.odysseus.platform.entities.OdsAuditLog;
import ro.simavi.odysseus.platform.repositories.OdsAuditLogRepository;
import ro.simavi.odysseus.platform.services.OdsAuditLogService;

import java.time.Instant;
@Service
public class OdsAuditLogServiceImpl implements OdsAuditLogService {
    @Autowired
    private OdsAuditLogRepository odsAuditLogRepository;

    @Override
    public void startDocProcess(String className, String operation, String docId) {
        OdsAuditLog odsAuditLog = this.createDocAudit(className,operation, docId,null, null, null);
        odsAuditLog.setAuditLogcol("START-DOC-PROC");
        odsAuditLogRepository.save(odsAuditLog);
    }

    @Override
    public void startDocProcess(String className, String operation, String docId, String shortText, String longText) {
        OdsAuditLog odsAuditLog = this.createDocAudit(className,operation, docId,null, shortText, longText);
        odsAuditLog.setAuditLogcol("START-DOC-PROC");
        odsAuditLogRepository.save(odsAuditLog);
    }

    @Override
    public void endDocProcess(String className, String operation, String docId, String result) {
        OdsAuditLog odsAuditLog = this.createDocAudit(className,operation, docId,result, null, null);
        odsAuditLog.setAuditLogcol("END-DOC-PROC");
        odsAuditLogRepository.save(odsAuditLog);
    }

    @Override
    public void endDocProcess(String className, String operation, String docId, String result, String shortText, String longText) {
        OdsAuditLog odsAuditLog = this.createDocAudit(className,operation, docId,result, shortText, longText);
        odsAuditLog.setAuditLogcol("END-DOC-PROC");
        odsAuditLogRepository.save(odsAuditLog);
    }

    @Override
    public void startTravelProcess(String className, String operation, String travelId) {
        OdsAuditLog odsAuditLog = this.createTravelAudit(className,operation, travelId,null, null, null);
        odsAuditLog.setAuditLogcol("START-TRAVEL");
        odsAuditLogRepository.save(odsAuditLog);
    }

    @Override
    public void startTravelProcess(String className, String operation, String travelId, String shortText, String longText) {
        OdsAuditLog odsAuditLog = this.createTravelAudit(className,operation, travelId,null, shortText, longText);
        odsAuditLog.setAuditLogcol("START-TRAVEL");
        odsAuditLogRepository.save(odsAuditLog);
    }

    @Override
    public void endTravelProcess(String className, String operation, String travelId, String result) {
        OdsAuditLog odsAuditLog = this.createTravelAudit(className,operation, travelId,result, null, null);
        odsAuditLog.setAuditLogcol("END-TRAVEL");
        odsAuditLogRepository.save(odsAuditLog);
    }

    @Override
    public void endTravelProcess(String className, String operation, String travelId, String result, String shortText, String longText) {
        OdsAuditLog odsAuditLog = this.createTravelAudit(className,operation, travelId,result, shortText, longText);
        odsAuditLog.setAuditLogcol("END-TRAVEL");
        odsAuditLogRepository.save(odsAuditLog);
    }

    @Override
    public void startTravelerProcess(String className, String operation, String travelerId) {
        OdsAuditLog odsAuditLog = this.createTravelerAudit(className,operation, travelerId,null, null, null);
        odsAuditLog.setAuditLogcol("START-TRAVELER");
        odsAuditLogRepository.save(odsAuditLog);
    }

    @Override
    public void startTravelerProcess(String className, String operation, String travelerId, String shortText, String longText) {
        OdsAuditLog odsAuditLog = this.createTravelerAudit(className,operation, travelerId,null, shortText, longText);
        odsAuditLog.setAuditLogcol("START-TRAVELER");
        odsAuditLogRepository.save(odsAuditLog);
    }

    @Override
    public void endTravelerProcess(String className, String operation, String travelerId, String result) {
        OdsAuditLog odsAuditLog = this.createTravelerAudit(className,operation, travelerId,result, null, null);
        odsAuditLog.setAuditLogcol("END-TRAVELER");
        odsAuditLogRepository.save(odsAuditLog);
    }

    @Override
    public void endTravelerProcess(String className, String operation, String travelerId, String result, String shortText, String longText) {
        OdsAuditLog odsAuditLog = this.createTravelerAudit(className,operation, travelerId,result, shortText, longText);
        odsAuditLog.setAuditLogcol("END-TRAVELER");
        odsAuditLogRepository.save(odsAuditLog);
    }

    @Override
    public void startGeneralProcess(OdsAuditLog odsAuditLog) {

        odsAuditLog.setAuditLogcol("START-GENERAL");
        odsAuditLogRepository.save(odsAuditLog);
    }

    @Override
    public void endGeneralProcess(OdsAuditLog odsAuditLog) {
        odsAuditLog.setAuditLogcol("END-GENERAL");
        odsAuditLogRepository.save(odsAuditLog);
    }

    @Override
    public void auditMessage(OdsAuditLog odsAuditLog) {
        odsAuditLog.setAuditLogcol("MESSAGE");
        odsAuditLogRepository.save(odsAuditLog);
    }

    @Override
    public void auditAction(String className, String operation, String shortMes, String longMes) {
        OdsAuditLog odsAuditLog = this.createTravelerAudit(className,operation, null,null, shortMes, longMes );
        odsAuditLog.setAuditLogcol("ACTION");
        odsAuditLogRepository.save(odsAuditLog);
    }

    @Override
    public void startCarProcess(String className, String operation, String carId) {
        OdsAuditLog odsAuditLog = this.createTravelAudit(className,operation, carId,null, null, null);
        odsAuditLog.setAuditLogcol("START-CAR");
        odsAuditLogRepository.save(odsAuditLog);
    }

    @Override
    public void endCarProcess(String className, String operation, String carId, String result) {
        OdsAuditLog odsAuditLog = this.createTravelAudit(className,operation, carId,result, null, null);
        odsAuditLog.setAuditLogcol("END-CAR");
        odsAuditLogRepository.save(odsAuditLog);
    }

    private OdsAuditLog createDocAudit(String className, String operation, String docId,
                                       String result, String shortText, String longText){
        OdsAuditLog odsAuditLog = new OdsAuditLog();
        odsAuditLog.setCreatedBy(className);
        odsAuditLog.setLogType(operation);
        odsAuditLog.setDocumentId(docId);
        odsAuditLog.setOperationResult(result);
        odsAuditLog.setShortText(shortText);
        odsAuditLog.setLogText(longText);
        odsAuditLog.setApplication("OdysseusBK");
        odsAuditLog.setCreatedBy(className);
        odsAuditLog.setCreationDate(Instant.now());
        return odsAuditLog;
    }

    private OdsAuditLog createTravelAudit(String className, String operation, String travelId,
                                       String result, String shortText, String longText){
        OdsAuditLog odsAuditLog = new OdsAuditLog();
        odsAuditLog.setCreatedBy(className);
        odsAuditLog.setLogType(operation);
        odsAuditLog.setTransactionId(travelId);
        odsAuditLog.setOperationResult(result);
        odsAuditLog.setShortText(shortText);
        odsAuditLog.setLogText(longText);
        odsAuditLog.setApplication("OdysseusBK");
        odsAuditLog.setCreatedBy(className);
        odsAuditLog.setCreationDate(Instant.now());
        return odsAuditLog;
    }

    private OdsAuditLog createTravelerAudit(String className, String operation, String travelerId,
                                          String result, String shortText, String longText){
        OdsAuditLog odsAuditLog = new OdsAuditLog();
        odsAuditLog.setCreatedBy(className);
        odsAuditLog.setLogType(operation);
        odsAuditLog.setTravelerId(travelerId);
        odsAuditLog.setOperationResult(result);
        odsAuditLog.setShortText(shortText);
        odsAuditLog.setLogText(longText);
        odsAuditLog.setApplication("OdysseusBK");
        odsAuditLog.setCreatedBy(className);
        odsAuditLog.setCreationDate(Instant.now());
        return odsAuditLog;
    }
}
