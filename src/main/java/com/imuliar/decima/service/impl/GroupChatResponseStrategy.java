package com.imuliar.decima.service.impl;

import com.imuliar.decima.service.session.SessionProvider;
import com.imuliar.decima.service.session.SessionState;
import com.imuliar.decima.service.util.StateFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * <p>Process updates come from the group chat</p>
 *
 * @author imuliar
 * @since 0.0.1
 */
@Service
public class GroupChatResponseStrategy extends AbstractResponseStrategy {

    @Autowired
    public GroupChatResponseStrategy(SessionProvider sessionProvider, StateFactory stateFactory) {
        super(sessionProvider, stateFactory);
    }

    @Override
    public void response(Long chatId, Update update) {
        //temporary disable processing of updates from the group chat
        //generateInitialState().processUpdate(update);
    }

    @Override
    protected SessionState generateInitialState() {
        return getStateFactory().getGroupChatUpdateProcessingState();
    }
}
