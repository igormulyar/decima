package com.imuliar.decima;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.telegram.telegrambots.ApiContextInitializer;

@SpringBootApplication
@EnableCaching
@EntityScan("com.imuliar.decima.entity")
@EnableJpaRepositories
@EnableTransactionManagement
public abstract class DecimaApplication {

    public static void main(String[] args) {
        ApiContextInitializer.init();
        SpringApplication.run(DecimaApplication.class, args);
    }

    @Bean
    public MessageSource messageSource(){
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename("msg");
        messageSource.setDefaultEncoding("UTF-8");
        messageSource.setCacheSeconds(6);
        return messageSource;
    }
}
