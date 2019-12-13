package com.imuliar.decima.service.processors;

import com.imuliar.decima.entity.ParkingUser;
import com.imuliar.decima.service.util.InlineKeyboardMarkupBuilder;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.time.LocalDate;
import java.util.Optional;

import static com.imuliar.decima.service.util.Callbacks.TO_BEGINNING;
import static com.imuliar.decima.service.util.RegexPatterns.ALPHANUMERIC_SLOT_NUMBER_PATTERN;

/**
 * <p>Search user by slot and publish results</p>
 *
 * @author imuliar
 * @since 0.0.1
 */
public abstract class AbstractSearchUserBySlotProcessor extends AbstractUpdateProcessor {

    @Override
    public boolean isMatch(Update update) {
        return true;
    }

    @Override
    protected void doProcess(Update update, ParkingUser parkingUser, Long chatId) {
        if (!regexpMsgEvaluating.apply(update, ALPHANUMERIC_SLOT_NUMBER_PATTERN)) {
            publishMessage(chatId, "Something wrong with input data format.");
        }

        String slotNumber = update.getMessage().getText().trim();
        Optional<ParkingUser> userFound = searchUser(slotNumber);
        if (userFound.isPresent()) {
            publishMessage(chatId, String.format("Slot *# %s* now is held by [%s](tg://user?id=%d)", slotNumber, userFound.get().toString(), userFound.get().getTelegramUserId()));
        } else {
            publishMessage(chatId, "Cannot find any user. Probably this slot is free for now.");
        }
    }

    private void publishMessage(Long chatId, String msg) {
        getMessagePublisher().sendMessageWithKeyboard(chatId, msg,
                new InlineKeyboardMarkupBuilder().addButton(new InlineKeyboardButton("Back").setCallbackData(TO_BEGINNING)).build());
    }

    private Optional<ParkingUser> searchUser(String slotNumberCriterion) {
        Optional<ParkingUser> booker = getUserRepository().findBooker(slotNumberCriterion, LocalDate.now());
        if (booker.isPresent()) {
            return booker;
        }
        return getUserRepository().findEngagingOwner(slotNumberCriterion, LocalDate.now());
    }
}
