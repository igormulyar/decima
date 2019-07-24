package com.imuliar.decima.service.impl;

import com.imuliar.decima.entity.ParkingUser;
import com.imuliar.decima.service.session.SessionProvider;
import com.imuliar.decima.service.state.AbstractState;
import com.imuliar.decima.service.state.GroupChatUpdateProcessingState;
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

    private final GroupChatUpdateProcessingState groupChatUpdateProcessingState;

    @Autowired
    public GroupChatResponseStrategy(SessionProvider sessionProvider, GroupChatUpdateProcessingState groupChatUpdateProcessingState) {
        super(sessionProvider);
        this.groupChatUpdateProcessingState = groupChatUpdateProcessingState;
    }

    @Override
    public void response(Long chatId, ParkingUser parkingUser, Update update) {
        groupChatUpdateProcessingState.processUpdate(chatId, parkingUser, update);
    }

    @Override
    protected AbstractState generateInitialState() {
        return groupChatUpdateProcessingState;
    }
}
