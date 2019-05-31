package com.imuliar.decima.service.state;

import com.google.common.collect.Lists;
import com.imuliar.decima.dao.BookingRepository;
import com.imuliar.decima.dao.SlotRepository;
import com.imuliar.decima.entity.Booking;
import com.imuliar.decima.entity.ParkingUser;
import com.imuliar.decima.entity.Slot;
import java.util.Optional;
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

    private static final String DROP_BOOKING_CALLBACK = "drop_booking:%d";

    private static final String CANCEL_BOOKING = "cancel_booking";

    @Autowired
    private SlotRepository slotRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Override
    public void processUpdate(Long chatId, ParkingUser parkingUser, Update update) {
        if (update.hasCallbackQuery()) {
            switch (update.getCallbackQuery().getData()) {
                case REQUEST_FREE_SLOTS_CALLBACK:
                    displayFreeSlots(chatId, update);
                    break;
                case DROP_BOOKING_CALLBACK:
                    dropBooking(chatId);
                    break;
                default:
                    displayWithBookingCheck(chatId, parkingUser);
            }
        } else {
            displayWithBookingCheck(chatId, parkingUser);
        }
    }

    private void dropBooking(Long chatId) {

        //DO THE BOOKING DROP STUFF
        displayInitialMessage(chatId);
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

    private void displayWithBookingCheck(Long chatId, ParkingUser parkingUser){
        Optional<Booking> bookingFound = bookingRepository.findByUserAndDate(parkingUser, LocalDate.now());
        if(bookingFound.isPresent()){
            Integer bookedSlotNumber = bookingFound.get().getSlot().getNumber();
            String callback = String.format(DROP_BOOKING_CALLBACK, bookedSlotNumber);
            displayTwoButtonsMessage(chatId, new InlineKeyboardButton().setText("Find parking slot").setCallbackData(callback));
        } else {
            displayInitialMessage(chatId);
        }
    }

    private void displayInitialMessage(Long chatId){
        displayTwoButtonsMessage(chatId, new InlineKeyboardButton().setText("Find parking slot").setCallbackData(REQUEST_FREE_SLOTS_CALLBACK));
    }

    /**
     * First button should be passed via mainActionButton param,
     * second is a "Cancel" action button hardcoded
     */
    private void displayTwoButtonsMessage(Long chatId, InlineKeyboardButton mainActionButton) {
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        markupInline.setKeyboard(keyboard);

        List<InlineKeyboardButton> buttons = new ArrayList<>();
        buttons.add(mainActionButton);
        buttons.add(new InlineKeyboardButton().setText("Cancel").setCallbackData(TO_BEGINNING_CALLBACK));
        keyboard.add(buttons);
        String message = "  ----------------------------------------\n" +
                "*Choose the action, please.*\n" +
                "----------------------------------------";
        getMessageSender().sendMessageWithKeyboard(chatId, message, markupInline);
    }
}
