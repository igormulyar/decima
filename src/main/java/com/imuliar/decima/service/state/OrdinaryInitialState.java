package com.imuliar.decima.service.state;

import com.google.common.collect.Lists;
import com.imuliar.decima.dao.BookingRepository;
import com.imuliar.decima.dao.SlotRepository;
import com.imuliar.decima.entity.Booking;
import com.imuliar.decima.entity.ParkingUser;
import com.imuliar.decima.entity.Slot;

import java.util.Collections;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

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
public class OrdinaryInitialState extends AbstractState {

    private static final String REQUEST_FREE_SLOTS_CALLBACK = "get_free_slots";

    private static final String DROP_BOOKING_CALLBACK = "drop_booking:%d";

    private static final String FIND_NEIGHBOURS_CALLBACK = "find_neighbours";

    private static final String BOOK_SLOT_CALLBACK = "book_slot:%d";

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
                .map(slot -> new InlineKeyboardButton().setText(slot.getNumber().toString()).setCallbackData(String.format(BOOK_SLOT_CALLBACK, slot.getNumber())))
                .collect(Collectors.toList());
        List<List<InlineKeyboardButton>> keyboard = Lists.partition(buttons, 6);
        markupInline.setKeyboard(keyboard);
        getMessageSender().sendImage(chatId, null, getPlanImageUrl());
        getMessageSender().sendMessageWithKeyboard(chatId, "There are some parking slots available. Select one you'd like to book for today.", markupInline);
    }

    private void displayWithBookingCheck(Long chatId, ParkingUser parkingUser){
        Optional<Booking> bookingFound = bookingRepository.findByUserAndDate(parkingUser, LocalDate.now());
        if(bookingFound.isPresent()){
            String bookedSlotNumber = bookingFound.get().getSlot().getNumber();
            displayForAlreadyBooked(chatId, bookedSlotNumber);
        } else {
            displayInitialMessage(chatId);
        }
    }

    private void displayInitialMessage(Long chatId){
        String message =
                "----------------------------------------\n" +
                        "*Choose the action, please.*\n" +
                        "----------------------------------------";
        List<InlineKeyboardButton> buttons = new ArrayList<>();
        buttons.add(new InlineKeyboardButton().setText("Find parking slot").setCallbackData(REQUEST_FREE_SLOTS_CALLBACK));
        buttons.add(new InlineKeyboardButton().setText("Cancel").setCallbackData(TO_BEGINNING_CALLBACK));
        displayGeneralInitMessage(chatId, message, buttons);
    }

    private void displayForAlreadyBooked(Long chatId, String bookedSlotNumber){
        String msg = String.format("You have booked the slot #%d. What are you going to do?", bookedSlotNumber);
        List<InlineKeyboardButton> buttons = new ArrayList<>();
        buttons.add(new InlineKeyboardButton().setText("Find neighbours").setCallbackData(FIND_NEIGHBOURS_CALLBACK));
        buttons.add(new InlineKeyboardButton().setText("Drop booking").setCallbackData(String.format(DROP_BOOKING_CALLBACK, bookedSlotNumber)));
        buttons.add(new InlineKeyboardButton().setText("Cancel").setCallbackData(TO_BEGINNING_CALLBACK));
        displayGeneralInitMessage(chatId, msg, buttons);
    }

    private void displayGeneralInitMessage(Long chatId, String message, List<InlineKeyboardButton> buttonsLine) {
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        keyboard.add(buttonsLine);
        markupInline.setKeyboard(keyboard);
        getMessageSender().sendMessageWithKeyboard(chatId, message, markupInline);
    }
}
