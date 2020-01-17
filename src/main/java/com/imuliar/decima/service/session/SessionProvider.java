package com.imuliar.decima.service.session;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * <p>Service provides a new or existent {@link UserSession} if not expired yet</p>
 *
 * @author imuliar
 * @since 0.0.1
 */
public interface SessionProvider {

    UserSession provideSession(Update update);
}
