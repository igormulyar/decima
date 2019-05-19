package com.imuliar.decima.service.impl;

import com.imuliar.decima.entity.ParkingUser;
import com.imuliar.decima.service.ResponseStrategy;
import com.imuliar.decima.service.session.SessionProvider;
import com.imuliar.decima.service.session.UserSession;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * <p>Encapsulates the common properties and behavior for any {@link ResponseStrategy} implementation.</p>
 *
 * @author imuliar
 * @since 0.0.1
 */
@Getter
@Setter
@Service
public class AbstractResponseStrategy implements ResponseStrategy {

    @Autowired
    protected SessionProvider sessionProvider;

    @Override
    public void response(Long chatId, ParkingUser parkingUser, Update update) {

        UserSession userSession = sessionProvider.provideSession(chatId);

        userSession.getContext().getCurrentState();

    }
}
