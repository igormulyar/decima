package com.imuliar.decima;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.meta.api.objects.User;

@SpringBootApplication
@EnableCaching
@EnableScheduling
@EntityScan("com.imuliar.decima.entity")
@EnableJpaRepositories
@EnableTransactionManagement
public abstract class DecimaApplication {

    public static void main(String[] args) {
        ApiContextInitializer.init();
        SpringApplication.run(DecimaApplication.class, args);
    }

    @Bean
    public MessageSource messageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename("msg");
        messageSource.setDefaultEncoding("UTF-8");
        messageSource.setCacheSeconds(6);
        return messageSource;
    }

    @Bean
    public Map<Integer, User> slotRequestBuffer() {
        return new ConcurrentHashMap<>();
    }
}
