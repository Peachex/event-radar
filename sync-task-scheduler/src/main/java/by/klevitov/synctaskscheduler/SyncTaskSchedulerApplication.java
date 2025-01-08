package by.klevitov.synctaskscheduler;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"by.klevitov.synctaskscheduler", "by.klevitov.eventradarcommon"})
public class SyncTaskSchedulerApplication {
    public static void main(String[] args) {
        SpringApplication.run(SyncTaskSchedulerApplication.class, args);
    }
}
