package com.imuliar.decima.service.processors.patrician;

import com.imuliar.decima.service.processors.AbstractInputForUserSearchProcessor;
import com.imuliar.decima.service.session.SessionState;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author imuliar
 * @since 0.0.1
 */
@Service
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class InputForUserSearchPatricianProcessor extends AbstractInputForUserSearchProcessor {

    @Override
    protected Optional<SessionState> getNextState() {
        return Optional.of(getStateFactory().engagingUserSearchPatricianState());
    }
}
