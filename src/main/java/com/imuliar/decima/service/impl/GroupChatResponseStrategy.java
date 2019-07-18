package com.imuliar.decima.service.impl;

import com.imuliar.decima.service.session.SessionProvider;
import com.imuliar.decima.service.state.AbstractState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>Process updates come from the group chat</p>
 *
 * @author imuliar
 * @since 0.0.1
 */
@Service
public class GroupChatResponseStrategy extends AbstractResponseStrategy {

    @Autowired
    public GroupChatResponseStrategy(SessionProvider sessionProvider) {
        super(sessionProvider);
    }

    @Override
    protected AbstractState generateInitialState() {
        return null;
    }
}
