package by.klevitov.eventpersistor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"by.klevitov.eventpersistor", "by.klevitov.eventradarcommon"})
public class EventPersistorApplication {
    public static void main(String[] args) {
        SpringApplication.run(EventPersistorApplication.class, args);
    }
}
