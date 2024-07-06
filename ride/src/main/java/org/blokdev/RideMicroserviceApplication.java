package org.blokdev;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class RideMicroserviceApplication {
    public static void main(String[] args) {
        SpringApplication.run(RideMicroserviceApplication.class, args);
    }
}
