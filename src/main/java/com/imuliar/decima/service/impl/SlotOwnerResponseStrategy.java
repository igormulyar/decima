package com.imuliar.decima.service.impl;

import com.imuliar.decima.service.session.SessionProvider;
import com.imuliar.decima.service.state.AbstractState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.stereotype.Service;

/**
 * <p>Process updates from the users who own parking slots</p>
 *
 * @author imuliar
 * @since 0.0.1
 */
@Service
public abstract class SlotOwnerResponseStrategy extends AbstractResponseStrategy {

    @Autowired
    public SlotOwnerResponseStrategy(SessionProvider sessionProvider) {
        super(sessionProvider);
    }

    @Lookup("slotOwnerInitialState")
    protected abstract AbstractState generateInitialState();
}
