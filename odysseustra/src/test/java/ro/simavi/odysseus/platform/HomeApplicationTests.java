package ro.simavi.odysseus.platform;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.logging.Logger;
@WebAppConfiguration
@SpringBootTest
@ActiveProfiles("test")
class HomeApplicationTests
{

    @Test
    void contextLoads() {
    }
//    @Test
//    void testFirst() {
//        Logger.getLogger(this.getClass().getName()).info("--> START Of test texcution");
//    }
//
//    @Test
//    void testLast() {
//        Logger.getLogger(this.getClass().getName()).info("<-- END Of test texcution");
//    }
}
