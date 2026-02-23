package ro.simavi.odysseus.platform;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;


@SpringBootApplication
public class HomeApplication
{
    public static void main(String[] args) {
        SpringApplication.run( HomeApplication.class, args);
    }
}
