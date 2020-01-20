package com.imuliar.decima.service.processors.plebeian;

import com.imuliar.decima.dao.BookingRepository;
import com.imuliar.decima.entity.Booking;
import com.imuliar.decima.service.impl.DecimaMessageSourceFacade;
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

    @Autowired
    private DecimaMessageSourceFacade msgSource;

    void publish(Long chatId, String langCode){
        Optional<Booking> bookingFound = bookingRepository.findByUserIdAndDate(chatId.intValue(), LocalDate.now());
        if (bookingFound.isPresent()) {
            String bookedSlotNumber = bookingFound.get().getSlot().getNumber();
            displayForAlreadyBooked(chatId, bookedSlotNumber, langCode);
        } else {
            displayInitialMessage(chatId, langCode);
        }
    }

    private void displayForAlreadyBooked(Long chatId, String bookedSlotNumber, String langCode) {
        String message = msgSource.getMsg("msg.bleb_you_hold", langCode, new String[]{bookedSlotNumber});
        messagePublisher.sendMessageWithKeyboard(chatId, message, new InlineKeyboardMarkupBuilder()
                .addButton(new InlineKeyboardButton().setText(msgSource.getMsg("btn.find_holder", langCode)).setCallbackData(ASK_SLOT_FOR_USER_SEARCH))
                .addButtonAtNewRaw(new InlineKeyboardButton().setText(msgSource.getMsg("btn.show_plan", langCode)).setCallbackData(SHOW_PLAN))
                .addButtonAtNewRaw(new InlineKeyboardButton().setText(msgSource.getMsg("btn.drop_booking", langCode)).setCallbackData(CANCEL_MY_BOOKING))
                .addButtonAtNewRaw(new InlineKeyboardButton().setText(msgSource.getMsg("btn.back", langCode)).setCallbackData(TO_BEGINNING))
                .build());
    }

    private void displayInitialMessage(Long chatId, String langCode) {
        String message = msgSource.getMsg("msg.pleb_choose_action", langCode);
        messagePublisher.sendMessageWithKeyboard(chatId, message, new InlineKeyboardMarkupBuilder()
                .addButton(new InlineKeyboardButton().setText(msgSource.getMsg("btn.find_slot", langCode)).setCallbackData(FIND_FREE_SLOT))
                .addButtonAtNewRaw(new InlineKeyboardButton().setText(msgSource.getMsg("btn.back", langCode)).setCallbackData(TO_BEGINNING))
                .build());
    }
}
