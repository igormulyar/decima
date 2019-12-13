package com.imuliar.decima.service.processors.plebeian;

import com.imuliar.decima.service.processors.AbstractSearchUserBySlotProcessor;
import com.imuliar.decima.service.state.SessionState;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 *
 * @author imuliar
 * @since 0.0.1
 */
@Service
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class SearchUserBySlotPlebeianProcessor extends AbstractSearchUserBySlotProcessor {

    @Override
    protected Optional<SessionState> getNextState() {
        return Optional.of(getStateFactory().getOrdinaryInitialState());
    }
}
