package com.imuliar.decima.service.impl;

import com.imuliar.decima.entity.ParkingUser;
import com.imuliar.decima.service.session.SessionProvider;
import com.imuliar.decima.service.state.SessionState;
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

    private final SessionState groupChatUpdateProcessingState;

    @Autowired
    public GroupChatResponseStrategy(SessionProvider sessionProvider, SessionState groupChatUpdateProcessingState) {
        super(sessionProvider);
        this.groupChatUpdateProcessingState = groupChatUpdateProcessingState;
    }

    @Override
    public void response(Long chatId, ParkingUser parkingUser, Update update) {
        groupChatUpdateProcessingState.processUpdate(chatId, parkingUser, update);
    }

    @Override
    protected SessionState generateInitialState() {
        return groupChatUpdateProcessingState;
    }
}
