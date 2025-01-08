package by.klevitov.eventwebapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableFeignClients(basePackages = "by.klevitov.eventradarcommon")
@ComponentScan(basePackages = {"by.klevitov.eventwebapp", "by.klevitov.eventradarcommon"})
public class EventWebAppApplication {
    public static void main(String[] args) {
        SpringApplication.run(EventWebAppApplication.class, args);
    }
}
