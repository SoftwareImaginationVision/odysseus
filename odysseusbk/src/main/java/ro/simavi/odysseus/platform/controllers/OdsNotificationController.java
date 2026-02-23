package ro.simavi.odysseus.platform.controllers;

import lombok.Getter;
import lombok.Setter;
import org.primefaces.PrimeFaces;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;
import ro.simavi.odysseus.platform.entities.OdsNotification;
import ro.simavi.odysseus.platform.repositories.OdsNotificationRepository;
import ro.simavi.odysseus.platform.services.OdsNotificationService;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import java.time.Instant;
import java.util.List;
import java.util.Objects;

@Setter
@Getter
@Component
@Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class OdsNotificationController {
    @Autowired
    private OdsNotificationRepository odsNotificationRepository;

    @Autowired
    private OdsNotificationService odsNotificationService;

    private List<OdsNotification> odsNotificationList;

    private OdsNotification odsNotificationSelected;

    private String nameFilter;

    @PostConstruct
    public void init(){
        odsNotificationList = odsNotificationRepository.findAll();
        if(odsNotificationList.isEmpty()) {
            odsNotificationRepository.save(new OdsNotification(
                    "Critical Security Alert",
                    "Unauthorized access detected. Immediate action required.",
                    (short) 1,
                    Instant.parse("2023-04-10T08:45:00Z"),
                    "Security Team",
                    "SecurityApp",
                    Instant.parse("2023-04-12T23:59:59Z"),
                    "Open",
                    "User567",
                    "https://example.com/security/alert/123",
                    456,
                    "Security Breach",
                    "Security",
                    "Security Analyst"
            ));

            odsNotificationRepository.save(new OdsNotification(
                    "Product Update",
                    "Exciting new features rolled out in the latest release.",
                    (short) 3,
                    Instant.parse("2023-04-11T14:20:00Z"),
                    "Product Team",
                    "ProductApp",
                    Instant.parse("2023-04-15T23:59:59Z"),
                    "Open",
                    "All Users",
                    "https://example.com/announcement/789",
                    123,
                    "Feature Release",
                    "Product",
                    "Product Manager"
            ));

            odsNotificationRepository.save(new OdsNotification(
                    "System Maintenance",
                    "Scheduled maintenance for system improvements.",
                    (short) 2,
                    Instant.parse("2023-04-12T11:30:00Z"),
                    "IT Support",
                    "ITOps",
                    Instant.parse("2023-04-13T15:00:00Z"),
                    "Open",
                    "All Users",
                    "https://example.com/notification/987",
                    654,
                    "System Maintenance",
                    "IT",
                    "IT Support Specialist"
            ));

            odsNotificationRepository.save(new OdsNotification(
                    "Urgent Task Assignment",
                    "New urgent task assigned. Please review and take action.",
                    (short) 1,
                    Instant.parse("2023-04-13T09:00:00Z"),
                    "Task Management",
                    "TaskApp",
                    Instant.parse("2023-04-14T23:59:59Z"),
                    "Open",
                    "User789",
                    "https://example.com/task/456",
                    987,
                    "Urgent Task",
                    "Task",
                    "Task Coordinator"
            ));

            odsNotificationRepository.save(new OdsNotification(
                    "Meeting Reminder",
                    "Reminder: Team meeting at 2:00 PM today.",
                    (short) 2,
                    Instant.parse("2023-04-14T13:45:00Z"),
                    "HR Department",
                    "HRApp",
                    Instant.parse("2023-04-14T14:30:00Z"),
                    "Open",
                    "All Employees",
                    "https://example.com/meeting/321",
                    321,
                    "Team Meeting",
                    "HR",
                    "HR Coordinator"
            ));

            odsNotificationRepository.save(new OdsNotification(
                    "Performance Update",
                    "Yearly performance reviews scheduled. Prepare documentation.",
                    (short) 3,
                    Instant.parse("2023-04-15T10:30:00Z"),
                    "HR Department",
                    "HRApp",
                    Instant.parse("2023-04-17T23:59:59Z"),
                    "Open",
                    "All Employees",
                    "https://example.com/performance/update/654",
                    456,
                    "Performance Review",
                    "HR",
                    "HR Manager"
            ));

            odsNotificationRepository.save(new OdsNotification(
                    "Task Completion",
                    "Task successfully completed. Congratulations!",
                    (short) 2,
                    Instant.parse("2023-04-16T16:15:00Z"),
                    "Task Management",
                    "TaskApp",
                    Instant.parse("2023-04-18T23:59:59Z"),
                    "Open",
                    "User123",
                    "https://example.com/task/completion/987",
                    654,
                    "Completed Task",
                    "Task",
                    "Task Manager"
            ));

            odsNotificationRepository.save(new OdsNotification(
                    "Project Deadline Extension",
                    "Project deadline extended. Revised due date is April 30.",
                    (short) 1,
                    Instant.parse("2023-04-17T12:00:00Z"),
                    "Project Management",
                    "ProjectApp",
                    Instant.parse("2023-04-30T23:59:59Z"),
                    "Open",
                    "Project Team",
                    "https://example.com/project/deadline/789",
                    789,
                    "Deadline Extension",
                    "Project",
                    "Project Manager"
            ));

            odsNotificationRepository.save(new OdsNotification(
                    "Training Session",
                    "Upcoming training session on new software features.",
                    (short) 3,
                    Instant.parse("2023-04-18T09:30:00Z"),
                    "Training Department",
                    "TrainingApp",
                    Instant.parse("2023-04-19T12:00:00Z"),
                    "Open",
                    "All Employees",
                    "https://example.com/training/session/123",
                    987,
                    "Software Training",
                    "Training",
                    "Training Coordinator"
            ));

            odsNotificationRepository.save(new OdsNotification(
                    "Feedback Request",
                    "Provide feedback on the recent team collaboration survey.",
                    (short) 2,
                    Instant.parse("2023-04-19T14:45:00Z"),
                    "HR Department",
                    "HRApp",
                    Instant.parse("2023-04-21T23:59:59Z"),
                    "Open",
                    "All Employees",
                    "https://example.com/feedback/survey/456",
                    321,
                    "Team Collaboration",
                    "HR",
                    "HR Coordinator"
            ));
            odsNotificationList = odsNotificationRepository.findAll();
        }
    }

    public void saveOdsNotification(){
        if(Objects.nonNull(odsNotificationSelected)){
            OdsNotification odsNotification = odsNotificationService.saveOrEditOdsNotification(odsNotificationSelected);
            odsNotificationList = odsNotificationRepository.findAll();
            PrimeFaces.current().ajax().update("odsNotificationForm:dataTableOdsNotification");
            FacesMessage msg =new FacesMessage("Successfully saved!");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
    }

    public void deleteOdsNotification(){
        if(Objects.nonNull(odsNotificationSelected)){
            odsNotificationService.deleteOdsNotification(odsNotificationSelected);
            odsNotificationList.remove(odsNotificationSelected);
            odsNotificationSelected = null;
            PrimeFaces.current().ajax().update("odsNotificationForm:dataTableOdsNotification");
            FacesMessage msg =new FacesMessage("Successfully deleted!");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
    }

    public void createNewOdsNotification(){
        this.odsNotificationSelected = new OdsNotification();
    }

    public void filter(){
        if(nameFilter != null){
            odsNotificationList = odsNotificationRepository.findAllByApplicationNameContainingIgnoreCaseOrNotificationStatusContainingIgnoreCaseOrShortContentContainingIgnoreCase(nameFilter, nameFilter, nameFilter);
        }else {
            odsNotificationList = odsNotificationRepository.findAll();
        }
    }

    public void reset(){
        odsNotificationList = odsNotificationRepository.findAll();
        nameFilter = null;
    }
}
