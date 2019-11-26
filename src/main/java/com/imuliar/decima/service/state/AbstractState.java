package com.imuliar.decima.service.state;

import com.imuliar.decima.entity.ParkingUser;
import com.imuliar.decima.service.UpdateProcessor;
import com.imuliar.decima.service.session.UserSession;
import java.util.List;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * <p>Encapsulates common properties and behavior for child states</p>
 *
 * @author imuliar
 * @since 0.0.1
 */
public abstract class AbstractState {

    private UserSession userSession;

    abstract List<UpdateProcessor> getUpdateProcessors();

    public void processUpdate(Long chatId, ParkingUser parkingUser, Update update) {
        getUpdateProcessors().stream()
                .filter(processor -> processor.isMatch(update))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Cannot find applicable processor to handle input update!"))
                .process(update, parkingUser)
                .ifPresent(this::proceedToNextState);
    }

    private void proceedToNextState(AbstractState nextState) {
        nextState.setUserSession(userSession);
        userSession.setCurrentState(nextState);
    }

    public UserSession getUserSession() {
        return userSession;
    }

    public void setUserSession(UserSession userSession) {
        this.userSession = userSession;
    }
}
