package com.imuliar.decima;

import com.imuliar.decima.service.UpdateProcessor;
import com.imuliar.decima.service.session.SessionState;
import com.imuliar.decima.service.util.UpdateProcessorFactory;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

/**
 * <p>User's session states configuration</p>
 *
 * @author imuliar
 * @since 0.0.1
 */
@Configuration
public class StateConfig {

    @Autowired
    private UpdateProcessorFactory processorFactory;

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public SessionState ordinaryInitialState(){
        List<UpdateProcessor> updateProcessors = new ArrayList<>();
        updateProcessors.add(processorFactory.findRandomSlotPlebeianProcessor());
        updateProcessors.add(processorFactory.cancelBookingProcessor());
        updateProcessors.add(processorFactory.inputForUserSearchPlebeianProcessor());
        updateProcessors.add(processorFactory.showPlanProcessor());
        updateProcessors.add(processorFactory.sharePatricianSlotProcessor());
        updateProcessors.add(processorFactory.doThePollPlebeianProcessor());

        updateProcessors.add(processorFactory.defaultPlebeianProcessor());
        return new SessionState(updateProcessors);
    }

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public SessionState slotOwnerInitialState(){
        List<UpdateProcessor> updateProcessors = createPatricianCommonProcessors();
        updateProcessors.add(processorFactory.sharePatricianSlotProcessor());
        updateProcessors.add(processorFactory.bookSlotPatricianProcessor());
        updateProcessors.add(processorFactory.inputForUserSearchPatricianProcessor());
        updateProcessors.add(processorFactory.showPlanProcessor());
        updateProcessors.add(processorFactory.setSharingPeriodProcessor());
        updateProcessors.add(processorFactory.listPeriodsPatricianProcessor());
        updateProcessors.add(processorFactory.cancelBookingProcessor());
        updateProcessors.add(processorFactory.defaultPatricianProcessor());
        return new SessionState(updateProcessors);
    }

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public SessionState engagingUserSearchPlebeianState(){
        List<UpdateProcessor> updateProcessors = new ArrayList<>();
        updateProcessors.add(processorFactory.toPlebeianBeginningProcessor());
        updateProcessors.add(processorFactory.searchUserBySlotPlebeianProcessor());
        return new SessionState(updateProcessors);
    }

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public SessionState engagingUserSearchPatricianState(){
        List<UpdateProcessor> updateProcessors = createPatricianCommonProcessors();
        updateProcessors.add(processorFactory.searchUserBySlotPatricianProcessor());
        return new SessionState(updateProcessors);
    }

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public SessionState pickStartDateState(){
        List<UpdateProcessor> updateProcessors = createPatricianCommonProcessors();
        updateProcessors.add(processorFactory.yearBackProcessor());
        updateProcessors.add(processorFactory.yearForwardProcessor());
        updateProcessors.add(processorFactory.monthBackProcessor());
        updateProcessors.add(processorFactory.monthForwardProcessor());
        updateProcessors.add(processorFactory.pickStartDateProcessor());
        return new SessionState(updateProcessors);
    }

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public SessionState pickEndDateState(){
        List<UpdateProcessor> updateProcessors = createPatricianCommonProcessors();
        updateProcessors.add(processorFactory.yearBackProcessor());
        updateProcessors.add(processorFactory.yearForwardProcessor());
        updateProcessors.add(processorFactory.monthBackProcessor());
        updateProcessors.add(processorFactory.monthForwardProcessor());
        updateProcessors.add(processorFactory.pickEndDateProcessor());
        return new SessionState(updateProcessors);
    }

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public SessionState confirmSharingPeriodState(){
        List<UpdateProcessor> updateProcessors = createPatricianCommonProcessors();
        updateProcessors.add(processorFactory.saveVacantPeriodProcessor());
        return new SessionState(updateProcessors);
    }

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public SessionState listPeriodsState(){
        List<UpdateProcessor> updateProcessors = createPatricianCommonProcessors();
        updateProcessors.add(processorFactory.manipulatePeriodPatricianProcessor());
        return new SessionState(updateProcessors);
    }

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public SessionState managePeriodState(){
        List<UpdateProcessor> updateProcessors = createPatricianCommonProcessors();
        updateProcessors.add(processorFactory.cancelSharingPatricianProcessor());
        return new SessionState(updateProcessors);
    }

    private List<UpdateProcessor> createPatricianCommonProcessors(){
        List<UpdateProcessor> updateProcessors = new ArrayList<>();
        updateProcessors.add(processorFactory.toPatricianBeginningProcessor());
        updateProcessors.add(processorFactory.yesPatricianProcessor());
        updateProcessors.add(processorFactory.noPatricianProcessor());
        return updateProcessors;
    }

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public SessionState groupChatUpdateProcessingState(){
        return new SessionState(Collections.emptyList());
    }

}
