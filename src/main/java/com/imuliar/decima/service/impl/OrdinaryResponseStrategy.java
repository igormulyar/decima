package com.imuliar.decima.service.impl;

import com.imuliar.decima.service.session.SessionProvider;
import com.imuliar.decima.service.state.AbstractState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.stereotype.Service;

/**
 * <p>Process updates come from users who don't own parking slot</p>
 *
 * @author imuliar
 * @since 0.0.1
 */
@Service
public abstract class OrdinaryResponseStrategy extends AbstractResponseStrategy {

    @Autowired
    public OrdinaryResponseStrategy(SessionProvider sessionProvider) {
        super(sessionProvider);
    }

    @Lookup("ordinaryInitialState")
    protected abstract AbstractState generateInitialState();
}
