package ro.simavi.odysseus.platform.servicesImpl;

import org.springframework.stereotype.Service;
import ro.simavi.odysseus.platform.entities.HomeData;
import ro.simavi.odysseus.platform.services.HomeDataService;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Class used to retrieve data related to audit
 * Implementation of a public interface
 */
@Service
public class HomeDataServiceImpl implements HomeDataService
{
    /**
     * Dumy method for data audit
     * @return List of two dummy entries
     */
    @Override
    public List<HomeData> homeData(){
        List<HomeData> dummyList = new ArrayList<>();
        HomeData d1 = new HomeData();
        d1.setId( 1l );
        d1.setIp( "172.20.1.115" );
        d1.setOwner( "iacobc" );
        d1.setAuditSeverity( 0l );
        d1.setAuditType( "all" );
        d1.setRegisteredDate( new Date() );
        d1.setAuditContent( "Example of first line of audit" );
        dummyList.add( d1 );
        HomeData d2 = new HomeData();
        d2.setId( 2l );
        d2.setIp( "172.20.1.116" );
        d2.setOwner( "iacobc" );
        d2.setAuditSeverity( 1l );
        d2.setAuditType( "all" );
        d2.setRegisteredDate( new Date() );
        d2.setAuditContent( "Example of second line of audit" );
        dummyList.add( d2 );

        return dummyList;
    }

}
