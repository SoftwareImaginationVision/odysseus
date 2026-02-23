package ro.simavi.odysseus.platform.entities;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class HomeData
{
    private Long id;

    private String auditType;

    private Long auditSeverity;

    private Date registeredDate;

    private String owner;

    private String ip;

    private String auditContent;
}
