package ro.simavi.odysseus.platform.entities;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Getter
@Setter
@Component
public class HomeSetting
{
    private Long id;

    //@Value("${mescobrad.home.type}")
    private String auditType;

    //@Value("${mescobrad.home.severity}")
    private Long auditSeverity;

    //@Value("${mescobrad.home.applications}")
    private String auditAppList;

   // @Value("${mescobrad.home.events}")
    private String auditEventsList;

    private List<String> auditApplication;

    private List<String> auditEvents;

    public HomeSetting(){}

    public HomeSetting(Long l){
        id=1l;
        auditType ="all";
        auditSeverity=3L;
        if(auditAppList!=null && auditAppList.length()>0){
            String[] ls = auditAppList.split( ";" );

            for ( String s : ls )
            {
                auditApplication.add( s );
            }
        }
        else{
            auditApplication.add( "MES-CoBraD" );
        }

        if(auditEventsList!=null && auditEventsList.length()>0){
            String[] ls = auditEventsList.split( ";" );
            for ( String s : ls )
            {
                auditEvents.add( s );
            }
        }
        else{
            auditEvents.add( "all" );
        }
    }
}
