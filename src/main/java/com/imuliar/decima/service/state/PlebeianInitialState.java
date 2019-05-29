package com.imuliar.decima.service.state;

import com.google.common.collect.Lists;
import com.imuliar.decima.dao.SlotRepository;
import com.imuliar.decima.entity.ParkingUser;
import com.imuliar.decima.entity.Slot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * //TODO add description <p></p>
 *
 * @author imuliar
 * @since //TODO specify version
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class PlebeianInitialState extends AbstractState {

    private static final String REQUEST_FREE_SLOTS_CALLBACK = "get_free_slots";

    private static final String CANCEL_BOOKING = "cancel_booking";

    @Autowired
    private SlotRepository slotRepository;

    @Override
    public void processUpdate(Long chatId, ParkingUser parkingUser, Update update) {
        if (update.hasCallbackQuery()) {
            switch (update.getCallbackQuery().getData()) {
                case REQUEST_FREE_SLOTS_CALLBACK:
                    displayFreeSlots(chatId, update);
                    break;

                case CANCEL_BOOKING:
                    cancellBooking();
                    displayInitialMessage(chatId);
                    break;
                default:
                    displayInitialMessage(chatId);

            }
        } else {
            displayInitialMessage(chatId);
        }
    }

    private void cancellBooking() {

    }

    private void displayFreeSlots(Long chatId, Update update) {
        List<Slot> freeSlots = slotRepository.findFreeSlots(LocalDate.now());
        if(CollectionUtils.isEmpty(freeSlots)){
            getMessageSender().popUpNotify(update.getCallbackQuery().getId(), "Can't find free slot for parking :(");
        }

        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<InlineKeyboardButton> buttons = freeSlots.stream()
                .map(slot -> new InlineKeyboardButton().setText(slot.getNumber().toString()).setCallbackData("slot#"+slot.getNumber()))
                .collect(Collectors.toList());
        List<List<InlineKeyboardButton>> keyboard = Lists.partition(buttons, 6);
        markupInline.setKeyboard(keyboard);
        getMessageSender().sendMessageWithKeyboard(chatId, "There are some parking slots available. Select one you'd like to book for today.", markupInline);
    }



    private void displayInitialMessage(Long chatId) {
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        markupInline.setKeyboard(keyboard);

        List<InlineKeyboardButton> buttons = new ArrayList<>();
        buttons.add(new InlineKeyboardButton().setText("Find parking slot").setCallbackData(REQUEST_FREE_SLOTS_CALLBACK));
        buttons.add(new InlineKeyboardButton().setText("Cancel").setCallbackData(TO_BEGINNING_CALLBACK));
        keyboard.add(buttons);
        getMessageSender().sendMessageWithKeyboard(chatId, "Choose the action, please.", markupInline);
    }
}
