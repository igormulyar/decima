package com.imuliar.decima;

import com.imuliar.decima.service.UpdateProcessor;
import com.imuliar.decima.service.session.SessionState;
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
        updateProcessors.add(inputForUserSearchPlebeianProcessor());
        updateProcessors.add(showPlanProcessor());
        updateProcessors.add(sharePatricianSlotProcessor());
        updateProcessors.add(defaultPlebeianProcessor());

        return new SessionState(updateProcessors);
    }

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public SessionState slotOwnerInitialState(){
        List<UpdateProcessor> updateProcessors = new ArrayList<>();
        updateProcessors.add(sharePatricianSlotProcessor());
        updateProcessors.add(bookSlotPatricianProcessor());
        updateProcessors.add(inputForUserSearchPatricianProcessor());
        updateProcessors.add(showPlanProcessor());
        updateProcessors.add(setSharingPeriodProcessor());
        updateProcessors.add(defaultPatricianProcessor());

        return new SessionState(updateProcessors);
    }

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public SessionState engagingUserSearchPlebeianState(){
        List<UpdateProcessor> updateProcessors = new ArrayList<>();
        updateProcessors.add(searchUserBySlotPlebeianProcessor());
        return new SessionState(updateProcessors);
    }

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public SessionState engagingUserSearchPatricianState(){
        List<UpdateProcessor> updateProcessors = new ArrayList<>();
        updateProcessors.add(searchUserBySlotPatricianProcessor());
        return new SessionState(updateProcessors);
    }

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public SessionState pickStartDateState(){
        List<UpdateProcessor> updateProcessors = new ArrayList<>();
        updateProcessors.add(toPatricianBeginningProcessor());
        updateProcessors.add(yearBackProcessor());
        updateProcessors.add(yearForwardProcessor());
        updateProcessors.add(monthBackProcessor());
        updateProcessors.add(monthForwardProcessor());
        updateProcessors.add(pickStartDateProcessor());
        return new SessionState(updateProcessors);
    }

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public SessionState pickEndDateState(){
        List<UpdateProcessor> updateProcessors = new ArrayList<>();
        updateProcessors.add(toPatricianBeginningProcessor());
        updateProcessors.add(yearBackProcessor());
        updateProcessors.add(yearForwardProcessor());
        updateProcessors.add(monthBackProcessor());
        updateProcessors.add(monthForwardProcessor());
        updateProcessors.add(pickEndDateProcessor());
        return new SessionState(updateProcessors);
    }

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public SessionState confirmSharingPeriodState(){
        List<UpdateProcessor> updateProcessors = new ArrayList<>();
        updateProcessors.add(toPatricianBeginningProcessor());
        updateProcessors.add(saveVacantPeriodProcessor());
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

    @Lookup("sharePatricianSlotProcessor")
    abstract UpdateProcessor sharePatricianSlotProcessor();

    @Lookup("cancelBookingProcessor")
    abstract UpdateProcessor cancelBookingProcessor();

    @Lookup("inputForUserSearchPlebeianProcessor")
    abstract UpdateProcessor inputForUserSearchPlebeianProcessor();

    @Lookup("inputForUserSearchPatricianProcessor")
    abstract UpdateProcessor inputForUserSearchPatricianProcessor();

    @Lookup("searchUserBySlotPlebeianProcessor")
    abstract UpdateProcessor searchUserBySlotPlebeianProcessor();

    @Lookup("searchUserBySlotPatricianProcessor")
    abstract UpdateProcessor searchUserBySlotPatricianProcessor();

    @Lookup("showPlanProcessor")
    abstract UpdateProcessor showPlanProcessor();

    @Lookup("setSharingPeriodProcessor")
    abstract UpdateProcessor setSharingPeriodProcessor();

    @Lookup("toPatricianBeginningProcessor")
    abstract UpdateProcessor toPatricianBeginningProcessor();

    @Lookup("yearBackProcessor")
    abstract UpdateProcessor yearBackProcessor();

    @Lookup("yearForwardProcessor")
    abstract UpdateProcessor yearForwardProcessor();

    @Lookup("monthBackProcessor")
    abstract UpdateProcessor monthBackProcessor();

    @Lookup("monthForwardProcessor")
    abstract UpdateProcessor monthForwardProcessor();

    @Lookup("pickStartDateProcessor")
    abstract UpdateProcessor pickStartDateProcessor();

    @Lookup("pickEndDateProcessor")
    abstract UpdateProcessor pickEndDateProcessor();

    @Lookup("saveVacantPeriodProcessor")
    abstract UpdateProcessor saveVacantPeriodProcessor();
}
