package com.imuliar.decima.service.processors.plebeian;

import com.imuliar.decima.dao.BookingRepository;
import com.imuliar.decima.entity.Booking;
import com.imuliar.decima.service.impl.MessagePublisher;
import com.imuliar.decima.service.util.InlineKeyboardMarkupBuilder;
import com.vdurmont.emoji.EmojiParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.time.LocalDate;
import java.util.Optional;

import static com.imuliar.decima.service.util.Callbacks.*;
import static com.imuliar.decima.service.util.Callbacks.FIND_FREE_SLOT;
import static com.imuliar.decima.service.util.Callbacks.TO_BEGINNING;

/**
 * <p>Publish default message for patrician</p>
 *
 * @author imuliar
 * @since 0.0.1
 */
@Service
public class DefaultPlebeianViewPublisher {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private MessagePublisher messagePublisher;

    void publish(Long chatId){
        Optional<Booking> bookingFound = bookingRepository.findByUserIdAndDate(chatId.intValue(), LocalDate.now());
        if (bookingFound.isPresent()) {
            String bookedSlotNumber = bookingFound.get().getSlot().getNumber();
            displayForAlreadyBooked(chatId, bookedSlotNumber);
        } else {
            displayInitialMessage(chatId);
        }
    }

    private void displayForAlreadyBooked(Long chatId, String bookedSlotNumber) {
        String message = String.format(EmojiParser.parseToUnicode(":sunglasses:\nToday you hold the slot *# %s*. \nChoose the action."), bookedSlotNumber);
        messagePublisher.sendMessageWithKeyboard(chatId, message, new InlineKeyboardMarkupBuilder()
                .addButton(new InlineKeyboardButton().setText("Find slot holder").setCallbackData(ASK_SLOT_FOR_USER_SEARCH))
                .addButton(new InlineKeyboardButton().setText("Show parking plan").setCallbackData(SHOW_PLAN))
                .addButtonAtNewRaw(new InlineKeyboardButton().setText("Drop booking").setCallbackData(CANCEL_MY_BOOKING))
                .addButton(new InlineKeyboardButton().setText("Back").setCallbackData(TO_BEGINNING))
                .build());
    }

    private void displayInitialMessage(Long chatId) {
        String message = EmojiParser.parseToUnicode(":label: \n*Choose the action, please. :arrow_down:*\n");
        messagePublisher.sendMessageWithKeyboard(chatId, message, new InlineKeyboardMarkupBuilder()
                .addButton(new InlineKeyboardButton().setText("Find parking slot").setCallbackData(FIND_FREE_SLOT))
                .addButton(new InlineKeyboardButton().setText("Back").setCallbackData(TO_BEGINNING))
                .build());
    }
}
