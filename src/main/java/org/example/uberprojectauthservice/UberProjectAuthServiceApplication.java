package org.example.uberprojectauthservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan(basePackages = {
        "org.example.uberprojectentityservice.models",  // Shared entities
        "org.example.uberprojectauthservice.models"       // Auth service-specific entities
})
@EnableJpaRepositories(basePackages = {
        "org.example.uberprojectauthservice.repository"
})
@EnableJpaAuditing
public class UberProjectAuthServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(UberProjectAuthServiceApplication.class, args);
    }
}
