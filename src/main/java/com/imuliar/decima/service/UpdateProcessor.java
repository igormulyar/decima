package com.imuliar.decima.service;

import com.imuliar.decima.service.session.UserSession;
import com.imuliar.decima.service.session.SessionState;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Optional;

/**
 * <p>Single update processing rule</p>
 *
 * @author imuliar
 * @since 0.0.1
 */
public interface UpdateProcessor {

    /**
     * Check if this processor is applicable for current update processing
     *
     * @param update update received
     * @return {@literal} TRUE if received update should be processed by this processor implementation, {@literal FALSE} - if not
     */
    boolean isMatch(Update update);

    /**
     * Business logic of particular update processing
     *
     * @param update update received
     * @return next session state if required
     */
    Optional<SessionState> process(Update update);

    void setSession(UserSession userSession);

    UserSession getSession();
}
