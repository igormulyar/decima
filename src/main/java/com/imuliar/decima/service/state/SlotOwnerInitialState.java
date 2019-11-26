package com.imuliar.decima.service.state;

import com.imuliar.decima.service.UpdateProcessor;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * <p>Initial state in session for processing updates received from slot owner</p>
 *
 * @author imuliar
 * @since 0.0.1
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class SlotOwnerInitialState extends AbstractState {

    @Autowired
    @Qualifier("slotOwnerInitialStateProcessors")
    private List<UpdateProcessor> updateProcessors;

    @Override
    List<UpdateProcessor> getUpdateProcessors() {
        return updateProcessors;
    }
}
