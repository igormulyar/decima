package com.imuliar.decima.service.processors;

import com.imuliar.decima.entity.ParkingUser;
import com.imuliar.decima.service.state.SessionState;
import com.imuliar.decima.service.util.InlineKeyboardMarkupBuilder;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.time.LocalDate;
import java.util.Optional;

import static com.imuliar.decima.service.util.Callbacks.TO_BEGINNING;
import static com.imuliar.decima.service.util.RegexPatterns.ALPHANUMERIC_SLOT_NUMBER_PATTERN;

/**
 * <p>Search and display user by passed slot number</p>
 *
 * @author imuliar
 * @since 0.0.1
 */
@Service
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class SearchUserBySlotProcessor extends AbstractUpdateProcessor {

    @Override
    public boolean isMatch(Update update) {
        return true;
    }

    @Override
    void doProcess(Update update, ParkingUser parkingUser, Long chatId) {
        if (!regexpMsgEvaluating.apply(update, ALPHANUMERIC_SLOT_NUMBER_PATTERN)) {
            publishMessage(chatId, "Something wrong with input data format.");
        }

        String slotNumber = update.getMessage().getText().trim();
        Optional<ParkingUser> userFound = searchUser(slotNumber);
        if (userFound.isPresent()) {
            publishMessage(chatId, String.format("Slot # %s is held by %s now", slotNumber, userFound.get().getTelegramUsername()));
        } else {
            publishMessage(chatId, "Cannot find any user. Probably this slot is free for now.");
        }
    }

    private void publishMessage(Long chatId, String msg) {
        getMessagePublisher().sendMessageWithKeyboard(chatId, msg,
                new InlineKeyboardMarkupBuilder().addButton(new InlineKeyboardButton("Cancel").setCallbackData(TO_BEGINNING)).build());
    }

    private Optional<ParkingUser> searchUser(String slotNumberCriterion) {
        Optional<ParkingUser> booker = getUserRepository().findBooker(slotNumberCriterion, LocalDate.now());
        if (booker.isPresent()) {
            return booker;
        }
        return getUserRepository().findEngagingOwner(slotNumberCriterion, LocalDate.now());
    }

    @Override
    Optional<SessionState> getNextState() {
        return Optional.of(getStateFactory().getOrdinaryInitialState());
    }
}
