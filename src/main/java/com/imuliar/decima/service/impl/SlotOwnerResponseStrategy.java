package com.imuliar.decima.service.impl;

import com.imuliar.decima.service.session.SessionProvider;
import com.imuliar.decima.service.state.SessionState;
import com.imuliar.decima.service.util.StateFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>Process updates from the users who own parking slots</p>
 *
 * @author imuliar
 * @since 0.0.1
 */
@Service
public class SlotOwnerResponseStrategy extends AbstractResponseStrategy {

    @Autowired
    public SlotOwnerResponseStrategy(SessionProvider sessionProvider, StateFactory stateFactory) {
        super(sessionProvider, stateFactory);
    }

    protected SessionState generateInitialState(){
        return getStateFactory().getSlotOwnerInitialState();
    }
}
