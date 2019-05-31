package com.imuliar.decima.service.state;

import com.imuliar.decima.entity.ParkingUser;
import com.imuliar.decima.service.MessageSender;
import com.imuliar.decima.service.session.UserSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * //TODO add description <p></p>
 *
 * @author imuliar
 * @since //TODO specify version
 */
public abstract class AbstractState {

    protected static final String TO_BEGINNING_CALLBACK = "get_free_slots";

    @Autowired
    private MessageSender messageSender;

    UserSession userSession;

    public abstract void processUpdate(Long chatId, ParkingUser parkingUser, Update update);

    public MessageSender getMessageSender() {
        return messageSender;
    }

    public UserSession getUserSession() {
        return userSession;
    }

    public void setUserSession(UserSession userSession) {
        this.userSession = userSession;
    }
}