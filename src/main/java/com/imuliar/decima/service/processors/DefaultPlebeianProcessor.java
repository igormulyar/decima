package com.imuliar.decima.service.processors;

import com.imuliar.decima.entity.Booking;
import com.imuliar.decima.entity.ParkingUser;
import com.imuliar.decima.service.util.InlineKeyboardMarkupBuilder;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.time.LocalDate;
import java.util.Optional;

import static com.imuliar.decima.service.util.Callbacks.*;

/**
 * <p>Default update processor for ordinary user</p>
 *
 * @author imuliar
 * @since 0.1.1
 */
@Service
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class DefaultPlebeianProcessor extends AbstractUpdateProcessor {

    @Override
    public boolean isMatch(Update update) {
        return true;
    }

    @Override
    void doProcess(Update update, ParkingUser parkingUser, Long chatId) {
        Optional<Booking> bookingFound = getBookingRepository().findByUserAndDate(parkingUser, LocalDate.now());
        if (bookingFound.isPresent()) {
            String bookedSlotNumber = bookingFound.get().getSlot().getNumber();
            displayForAlreadyBooked(chatId, bookedSlotNumber);
        } else {
            displayInitialMessage(chatId);
        }
    }

    private void displayForAlreadyBooked(Long chatId, String bookedSlotNumber) {
        String message = String.format("You have booked the slot **#%s**. What are you going to do?", bookedSlotNumber);
        getMessagePublisher().sendMessageWithKeyboard(chatId, message, new InlineKeyboardMarkupBuilder()
                .addButton(new InlineKeyboardButton().setText("Find User by engaged slot").setCallbackData(ASK_SLOT_FOR_USER_SEARCH))
                .addButton(new InlineKeyboardButton().setText("Show parking plan").setCallbackData(SHOW_PLAN))
                .addButtonAtNewRaw(new InlineKeyboardButton().setText("Drop booking").setCallbackData(CANCEL_MY_BOOKING))
                .addButton(new InlineKeyboardButton().setText("Cancel").setCallbackData(TO_BEGINNING))
                .build());
    }

    private void displayInitialMessage(Long chatId) {
        String message = "*Choose the action, please.*";
        getMessagePublisher().sendMessageWithKeyboard(chatId, message, new InlineKeyboardMarkupBuilder()
                .addButton(new InlineKeyboardButton().setText("Find parking slot").setCallbackData(FIND_FREE_SLOT))
                .addButton(new InlineKeyboardButton().setText("Cancel").setCallbackData(TO_BEGINNING))
                .build());
    }
}
