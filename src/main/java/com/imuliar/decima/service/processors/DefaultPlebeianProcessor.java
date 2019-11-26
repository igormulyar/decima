package com.imuliar.decima.service.processors;

import com.imuliar.decima.entity.Booking;
import com.imuliar.decima.entity.ParkingUser;
import com.imuliar.decima.service.state.AbstractState;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import static com.imuliar.decima.service.util.Callbacks.DROP_BOOKING_TPL;
import static com.imuliar.decima.service.util.Callbacks.FIND_FREE_SLOT;
import static com.imuliar.decima.service.util.Callbacks.FIND_USER_BY_ENGAGED_SLOT;
import static com.imuliar.decima.service.util.Callbacks.TO_BEGINNING;

/**
 * <p>Default update processor for ordinary user</p>
 *
 * @author imuliar
 * @since 0.1.1
 */
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class DefaultPlebeianProcessor extends AbstractUpdateProcessor {

    @Override
    public boolean isMatch(Update update) {
        return true;
    }

    @Override
    Optional<AbstractState> doProcess(Update update, ParkingUser parkingUser, Long chatId) {
        displayWithBookingCheck(chatId, parkingUser);
        return Optional.empty();
    }

    private void displayWithBookingCheck(Long chatId, ParkingUser parkingUser) {
        Optional<Booking> bookingFound = getBookingRepository().findByUserAndDate(parkingUser, LocalDate.now());
        if (bookingFound.isPresent()) {
            String bookedSlotNumber = bookingFound.get().getSlot().getNumber();
            displayForAlreadyBooked(chatId, bookedSlotNumber);
        } else {
            displayInitialMessage(chatId);
        }
    }

    private void displayForAlreadyBooked(Long chatId, String bookedSlotNumber) {
        String msg = String.format("You have booked the slot **#%s**. What are you going to do?", bookedSlotNumber);
        List<InlineKeyboardButton> buttons = new ArrayList<>();
        buttons.add(new InlineKeyboardButton().setText("Find User by engaged slot").setCallbackData(FIND_USER_BY_ENGAGED_SLOT));
        buttons.add(new InlineKeyboardButton().setText("Drop booking").setCallbackData(String.format(DROP_BOOKING_TPL, bookedSlotNumber)));
        buttons.add(new InlineKeyboardButton().setText("Cancel").setCallbackData(TO_BEGINNING));
        displayGeneralInitMessage(chatId, msg, buttons);
    }

    private void displayInitialMessage(Long chatId) {
        String message =
                "----------------------------------------\n" +
                        "*Choose the action, please.*\n" +
                        "----------------------------------------";
        List<InlineKeyboardButton> buttons = new ArrayList<>();
        buttons.add(new InlineKeyboardButton().setText("Find parking slot").setCallbackData(FIND_FREE_SLOT));
        buttons.add(new InlineKeyboardButton().setText("Cancel").setCallbackData(TO_BEGINNING));
        displayGeneralInitMessage(chatId, message, buttons);
    }

    private void displayGeneralInitMessage(Long chatId, String message, List<InlineKeyboardButton> buttonsLine) {
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        keyboard.add(buttonsLine);
        markupInline.setKeyboard(keyboard);
        getMessagePublisher().sendMessageWithKeyboard(chatId, message, markupInline);
    }
}
