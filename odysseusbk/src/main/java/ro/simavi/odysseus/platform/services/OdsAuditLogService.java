package ro.simavi.odysseus.platform.services;

import org.springframework.stereotype.Service;
import ro.simavi.odysseus.platform.entities.OdsAuditLog;
@Service
public interface OdsAuditLogService {
    public  void startDocProcess(String className, String operation, String docId);
    public  void startDocProcess(String className, String operation, String docId, String shortText, String longText);
    public  void endDocProcess(String className, String operation, String docId, String result);
    public  void endDocProcess(String className, String operation, String docId, String result, String shortText, String longText);

    public  void startTravelProcess(String className, String operation, String travelId);
    public  void startTravelProcess(String className, String operation, String travelId, String shortText, String longText);
    public  void endTravelProcess(String className, String operation, String travelId, String result);
    public  void endTravelProcess(String className, String operation, String travelId, String result, String shortText, String longText);

    public  void startTravelerProcess(String className, String operation, String travelerId);
    public  void startTravelerProcess(String className, String operation, String traveerlId, String shortText, String longText);
    public  void endTravelerProcess(String className, String operation, String travelerId, String result);
    public  void endTravelerProcess(String className, String operation, String travelerId, String result, String shortText, String longText);

    public void startGeneralProcess(OdsAuditLog odsAuditLog);
    public void endGeneralProcess(OdsAuditLog odsAuditLog);
    public void auditMessage(OdsAuditLog odsAuditLog);

    public void auditAction(String className, String operation, String shortMes, String longMes);

    public  void startCarProcess(String className, String operation, String carId);
    public  void endCarProcess(String className, String operation, String carId, String result);


}
