package com.imuliar.decima.service.processors.plebeian;

import com.imuliar.decima.service.processors.AbstractUpdateProcessor;
import com.imuliar.decima.service.session.SessionState;
import com.imuliar.decima.service.util.InlineKeyboardMarkupBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.Optional;

import static com.imuliar.decima.service.util.Callbacks.TO_BEGINNING;

/**
 * <p>Return to the initial Plebeian state</p>
 *
 * @author imuliar
 * @since 0.0.1
 */
@Service
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ToPlebeianBeginningProcessor extends AbstractUpdateProcessor {

    @Autowired
    private DefaultPlebeianViewPublisher defaultPlebeianViewPublisher;

    @Override
    public boolean isMatch(Update update) {
        return callbackMatching.apply(update, TO_BEGINNING);
    }

    @Override
    protected void doProcess(Update update, Long chatId) {
        defaultPlebeianViewPublisher.publish(chatId);
    }

    @Override
    protected Optional<SessionState> getNextState() {
        return Optional.of(getStateFactory().getOrdinaryInitialState());
    }
}
