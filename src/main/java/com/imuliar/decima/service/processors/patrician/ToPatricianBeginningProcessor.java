package com.imuliar.decima.service.processors.patrician;

import com.imuliar.decima.entity.ParkingUser;
import com.imuliar.decima.service.processors.AbstractUpdateProcessor;
import com.imuliar.decima.service.state.SessionState;
import java.util.Optional;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

import static com.imuliar.decima.service.util.Callbacks.TO_BEGINNING;

/**
 * <p>Return to the initial Patrician state</p>
 *
 * @author imuliar
 * @since 0.0.1
 */
@Service
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ToPatricianBeginningProcessor extends AbstractUpdateProcessor {

    @Override
    public boolean isMatch(Update update) {
        return callbackMatching.apply(update, TO_BEGINNING);
    }

    @Override
    protected void doProcess(Update update, ParkingUser parkingUser, Long chatId) {
        getSession().getContext().clear();
        getMessagePublisher().popUpNotify(update.getCallbackQuery().getId(), "Action cancelled");
    }

    @Override
    protected Optional<SessionState> getNextState() {
        return Optional.of(getStateFactory().getSlotOwnerInitialState());
    }
}
