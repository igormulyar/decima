package com.imuliar.decima.service.impl;

import com.imuliar.decima.service.session.SessionProvider;
import com.imuliar.decima.service.state.AbstractState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.stereotype.Service;

/**
 * //TODO add description <p></p>
 *
 * @author imuliar
 * @since //TODO specify version
 */
@Service
public abstract class PatricianResponseStrategy extends AbstractResponseStrategy {

    @Autowired
    public PatricianResponseStrategy(SessionProvider sessionProvider) {
        super(sessionProvider);
    }

    @Lookup
    protected abstract AbstractState generateInitialState();

}
