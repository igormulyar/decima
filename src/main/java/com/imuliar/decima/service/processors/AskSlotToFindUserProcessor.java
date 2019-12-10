package com.imuliar.decima.service.processors;

import com.imuliar.decima.entity.ParkingUser;
import com.imuliar.decima.service.state.SessionState;
import com.imuliar.decima.service.util.InlineKeyboardMarkupBuilder;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.Optional;

import static com.imuliar.decima.service.util.Callbacks.ASK_SLOT_FOR_USER_SEARCH;
import static com.imuliar.decima.service.util.Callbacks.TO_BEGINNING;

/**
 * <p></p>
 *
 * @author imuliar
 * @since 0.0.1
 */
@Service
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class AskSlotToFindUserProcessor extends AbstractUpdateProcessor {

    @Override
    public boolean isMatch(Update update) {
        return callbackMatching.apply(update, ASK_SLOT_FOR_USER_SEARCH);
    }

    @Override
    void doProcess(Update update, ParkingUser parkingUser, Long chatId) {
        getMessagePublisher().sendMessageWithKeyboard(chatId, "Type the slot number (EN):", new InlineKeyboardMarkupBuilder()
                .addButton(new InlineKeyboardButton("Cancel").setCallbackData(TO_BEGINNING)).build());
    }

    @Override
    Optional<SessionState> getNextState() {
        return Optional.of(getStateFactory().getEngagingUserSearchState());
    }
}
