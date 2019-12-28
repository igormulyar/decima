package com.imuliar.decima.service.session;

import com.imuliar.decima.service.UpdateProcessor;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

/**
 * <p>Encapsulates common properties and behavior for child states</p>
 *
 * @author imuliar
 * @since 0.0.1
 */
public class SessionState {

    private List<UpdateProcessor> updateProcessors;

    private UserSession userSession;

    public SessionState(List<UpdateProcessor> updateProcessors) {
        this.updateProcessors = updateProcessors;
    }

    public void processUpdate(Update update) {
        getUpdateProcessors().stream()
                .filter(processor -> processor.isMatch(update))
                .peek(p -> p.setSession(userSession))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Cannot find applicable processor to handle input update!"))
                .process(update)
                .ifPresent(this::proceedToNextState);
    }

    private void proceedToNextState(SessionState nextState) {
        nextState.setUserSession(userSession);
        userSession.setCurrentState(nextState);
    }

    public UserSession getUserSession() {
        return userSession;
    }

    public void setUserSession(UserSession userSession) {
        this.userSession = userSession;
    }

    List<UpdateProcessor> getUpdateProcessors() {
        return updateProcessors;
    }
}
