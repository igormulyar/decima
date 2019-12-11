package com.imuliar.decima;

import com.imuliar.decima.service.UpdateProcessor;
import com.imuliar.decima.service.state.SessionState;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.telegram.telegrambots.ApiContextInitializer;

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

    /*Session states*/
    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public SessionState ordinaryInitialState(){
        List<UpdateProcessor> updateProcessors = new ArrayList<>();
        updateProcessors.add(findRandomSlotPlebeianProcessor());
        updateProcessors.add(cancelBookingProcessor());
        updateProcessors.add(askSlotToFindUserProcessor());
        updateProcessors.add(showPlanProcessor());
        updateProcessors.add(defaultPlebeianProcessor());

        return new SessionState(updateProcessors);
    }

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public SessionState slotOwnerInitialState(){
        List<UpdateProcessor> updateProcessors = new ArrayList<>();
        updateProcessors.add(setFreePatricianProcessor());
        updateProcessors.add(bookSlotPatricianProcessor());
        updateProcessors.add(defaultPatricianProcessor());

        return new SessionState(updateProcessors);
    }

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public SessionState engagingUserSearchState(){
        List<UpdateProcessor> updateProcessors = new ArrayList<>();

        updateProcessors.add(searchUserBySlotProcessor());
        return new SessionState(updateProcessors);
    }

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public SessionState groupChatUpdateProcessingState(){
        return new SessionState(Collections.emptyList());
    }

    /*Update processors lookups*/
    @Lookup("defaultPlebeianProcessor")
    abstract UpdateProcessor defaultPlebeianProcessor();

    @Lookup("defaultPatricianProcessor")
    abstract UpdateProcessor defaultPatricianProcessor();

    @Lookup("bookSlotPatricianProcessor")
    abstract UpdateProcessor bookSlotPatricianProcessor();

    @Lookup("findRandomSlotPlebeianProcessor")
    abstract UpdateProcessor findRandomSlotPlebeianProcessor();

    @Lookup("setFreePatricianProcessor")
    abstract UpdateProcessor setFreePatricianProcessor();

    @Lookup("cancelBookingProcessor")
    abstract UpdateProcessor cancelBookingProcessor();

    @Lookup("askSlotToFindUserProcessor")
    abstract UpdateProcessor askSlotToFindUserProcessor();

    @Lookup("searchUserBySlotProcessor")
    abstract UpdateProcessor searchUserBySlotProcessor();

    @Lookup("showPlanProcessor")
    abstract UpdateProcessor showPlanProcessor();
}
