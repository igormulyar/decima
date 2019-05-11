package com.imuliar;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.telegram.telegrambots.ApiContextInitializer;

@SpringBootApplication
@EntityScan("com.imuliar.decima.entity")
@EnableJpaRepositories
@EnableTransactionManagement
public class DecimaApplication {

    public static void main(String[] args) {
        ApiContextInitializer.init();
        SpringApplication.run(DecimaApplication.class, args);
    }
}
