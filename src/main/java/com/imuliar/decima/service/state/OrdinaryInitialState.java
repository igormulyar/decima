package com.imuliar.decima.service.state;

import com.imuliar.decima.service.UpdateProcessor;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * <p>Initial state for user who doesn't own a slot</p>
 *
 * @author imuliar
 * @since 0.0.1
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class OrdinaryInitialState extends AbstractState {

    @Autowired
    private List<UpdateProcessor> updateProcessors;

    @Override
    List<UpdateProcessor> getUpdateProcessors() {
        return updateProcessors;
    }
}
