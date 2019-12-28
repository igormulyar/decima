package com.imuliar.decima.service.impl;

import com.imuliar.decima.service.session.SessionProvider;
import com.imuliar.decima.service.session.SessionState;
import com.imuliar.decima.service.util.StateFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>Process updates come from users who don't own parking slot</p>
 *
 * @author imuliar
 * @since 0.0.1
 */
@Service
public class OrdinaryResponseStrategy extends AbstractResponseStrategy {

    @Autowired
    public OrdinaryResponseStrategy(SessionProvider sessionProvider, StateFactory stateFactory) {
        super(sessionProvider, stateFactory);
    }

    protected SessionState generateInitialState(){
        return getStateFactory().getOrdinaryInitialState();
    }
}
