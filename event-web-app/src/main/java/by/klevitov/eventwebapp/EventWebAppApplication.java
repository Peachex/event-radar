package by.klevitov.eventwebapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class EventWebAppApplication {
    public static void main(String[] args) {
        SpringApplication.run(EventWebAppApplication.class, args);
    }
}
