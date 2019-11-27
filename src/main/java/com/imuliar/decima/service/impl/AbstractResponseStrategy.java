package com.imuliar.decima.service.impl;

import com.imuliar.decima.entity.ParkingUser;
import com.imuliar.decima.service.ResponseStrategy;
import com.imuliar.decima.service.session.SessionProvider;
import com.imuliar.decima.service.session.UserSession;
import com.imuliar.decima.service.state.SessionState;
import lombok.Getter;
import lombok.Setter;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * <p>Encapsulates the common properties and behavior for any {@link ResponseStrategy} implementation.</p>
 *
 * @author imuliar
 * @since 0.0.1
 */
@Getter
@Setter
public abstract class AbstractResponseStrategy implements ResponseStrategy {

    protected SessionProvider sessionProvider;

    public AbstractResponseStrategy(SessionProvider sessionProvider) {
        this.sessionProvider = sessionProvider;
    }

    @Override
    public void response(Long chatId, ParkingUser parkingUser, Update update) {
        UserSession session = sessionProvider.provideSession(chatId);
        if(session.getCurrentState() == null){
            SessionState initialState = generateInitialState();
            session.setCurrentState(initialState);
            initialState.setUserSession(session);
        }

        session.getCurrentState().processUpdate(chatId, parkingUser, update);
    }

    protected abstract SessionState generateInitialState();
}
