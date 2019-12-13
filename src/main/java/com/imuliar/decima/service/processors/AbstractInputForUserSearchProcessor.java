package com.imuliar.decima.service.processors;

import com.imuliar.decima.entity.ParkingUser;
import com.imuliar.decima.service.state.SessionState;
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
    protected void doProcess(Update update, ParkingUser parkingUser, Long chatId) {
        getMessagePublisher().sendMessageWithKeyboard(chatId, "Type the slot number (EN):", new InlineKeyboardMarkupBuilder()
                .addButton(new InlineKeyboardButton("Back").setCallbackData(TO_BEGINNING)).build());
    }

    @Override
    protected Optional<SessionState> getNextState() {
        return Optional.of(getStateFactory().engagingUserSearchPlebeianState());
    }
}
