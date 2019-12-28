package com.imuliar.decima.service.processors;

import com.imuliar.decima.entity.Booking;
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
    protected void doProcess(Update update, Long chatId) {
        if (!regexpMsgEvaluating.apply(update, ALPHANUMERIC_SLOT_NUMBER_PATTERN)) {
            publishMessage(chatId, "Something wrong with input data format.");
            return;
        }

        String slotNumber = update.getMessage().getText().trim();
        if(!getSlotRepository().findByNumber(slotNumber).isPresent()){
            publishMessage(chatId, String.format("Slot # %s doesn't exist.", slotNumber));
            return;
        }

        Optional<Integer> userIdFound = searchUserId(slotNumber);
        if (userIdFound.isPresent()) {
            publishMessage(chatId, String.format("Slot *# %s* now is held by [this user](tg://user?id=%d)", slotNumber, userIdFound.get()));
        } else {
            publishMessage(chatId, "Cannot find any user. Probably this slot is free for now.");
        }
    }

    private void publishMessage(Long chatId, String msg) {
        getMessagePublisher().sendMessageWithKeyboard(chatId, msg,
                new InlineKeyboardMarkupBuilder().addButton(new InlineKeyboardButton("Back").setCallbackData(TO_BEGINNING)).build());
    }

    private Optional<Integer> searchUserId(String slotNumberCriterion) {
        Optional<Booking> booking = getBookingRepository().findBySlotNumberAndDate(slotNumberCriterion, LocalDate.now());
        if (booking.isPresent()) {
            return booking.map(Booking::getUserId);
        }
        return getReservationRepository().findEngagingOwner(slotNumberCriterion, LocalDate.now());
    }
}
