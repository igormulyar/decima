package com.imuliar.decima.service.processors;

import com.imuliar.decima.service.session.SessionState;
import com.imuliar.decima.service.util.InlineKeyboardMarkupBuilder;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.Optional;

import static com.imuliar.decima.service.util.Callbacks.ASK_SLOT_FOR_USER_SEARCH;
import static com.imuliar.decima.service.util.Callbacks.TO_BEGINNING;

/**
 * <p>Request input for searching the user by held slot</p>
 *
 * @author imuliar
 * @since 0.0.1
 */
public abstract class AbstractInputForUserSearchProcessor extends AbstractUpdateProcessor {

    @Override
    public boolean isMatch(Update update) {
        return callbackMatching.apply(update, ASK_SLOT_FOR_USER_SEARCH);
    }

    @Override
    protected void doProcess(Update update, Long chatId) {
        getMessagePublisher().sendMessage(chatId, getMsg("msg.enter_slot_number"), new InlineKeyboardMarkupBuilder()
                .addButton(new InlineKeyboardButton(getMsg("btn.back")).setCallbackData(TO_BEGINNING)).build());
    }

    @Override
    protected Optional<SessionState> getNextState() {
        return Optional.of(getStateFactory().engagingUserSearchPlebeianState());
    }
}
