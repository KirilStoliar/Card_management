package com.stoliar.cardManagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.stoliar.cardManagement.repository")
@EntityScan(basePackages = "com.stoliar.cardManagement.model")
public class CardManagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(CardManagementApplication.class, args);
    }

}
