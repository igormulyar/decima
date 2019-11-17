package com.imuliar.decima.service.state;

import com.google.common.collect.Lists;
import com.imuliar.decima.dao.BookingRepository;
import com.imuliar.decima.dao.SlotRepository;
import com.imuliar.decima.entity.Booking;
import com.imuliar.decima.entity.ParkingUser;
import com.imuliar.decima.entity.Slot;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import static com.imuliar.decima.service.util.Callbacks.*;
import static com.imuliar.decima.service.util.RegexPatterns.DROP_BOOKING_MATCHING_PATTERN;

/**
 * <p>Initial state for user who doesn't own a slot</p>
 *
 * @author imuliar
 * @since 0.0.1
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class OrdinaryInitialState extends AbstractState {

    private static final String SHOW_ENGAGED_SLOTS = "list_engaged_slots_cbk";

    private static final String BOOK_SLOT_CALLBACK = "book_slot:%s";

    @Autowired
    private SlotRepository slotRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Override
    public void processUpdate(Long chatId, ParkingUser parkingUser, Update update) {
        if (update.hasCallbackQuery()) {
            String callbackString = update.getCallbackQuery().getData();
            if (callbackString.equals(FIND_FREE_SLOTS)) {
                displayFreeSlots(chatId, update);
            } else if (DROP_BOOKING_MATCHING_PATTERN.matcher(callbackString).matches()) {
                //TODO drop the booking
            } else {
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
        /*List<Slot> freeSlots = slotRepository.findFreeSlots(LocalDate.now());*/
        List<Slot> freeSlots = Collections.emptyList(); //TODO replace with proper call
        if (CollectionUtils.isEmpty(freeSlots)) {
            getMessagePublisher().popUpNotify(update.getCallbackQuery().getId(), "Can't find free slot for parking :(");
        }

        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<InlineKeyboardButton> buttons = freeSlots.stream()
                .map(slot -> new InlineKeyboardButton().setText(slot.getNumber()).setCallbackData(String.format(BOOK_SLOT_CALLBACK, slot.getNumber())))
                .collect(Collectors.toList());
        List<List<InlineKeyboardButton>> keyboard = Lists.partition(buttons, 6);
        markupInline.setKeyboard(keyboard);
        getMessagePublisher().sendImage(chatId, null, getPlanImageUrl());
        getMessagePublisher().sendMessageWithKeyboard(chatId, "There are some parking slots available. Select one you'd like to book for today.", markupInline);
    }

    private void displayWithBookingCheck(Long chatId, ParkingUser parkingUser) {
        Optional<Booking> bookingFound = bookingRepository.findByUserAndDate(parkingUser, LocalDate.now());
        if (bookingFound.isPresent()) {
            String bookedSlotNumber = bookingFound.get().getSlot().getNumber();
            displayForAlreadyBooked(chatId, bookedSlotNumber);
        } else {
            displayInitialMessage(chatId);
        }
    }

    private void displayInitialMessage(Long chatId) {
        String message =
                "----------------------------------------\n" +
                        "*Choose the action, please.*\n" +
                        "----------------------------------------";
        List<InlineKeyboardButton> buttons = new ArrayList<>();
        buttons.add(new InlineKeyboardButton().setText("Find parking slot").setCallbackData(FIND_FREE_SLOTS));
        buttons.add(new InlineKeyboardButton().setText("Cancel").setCallbackData(TO_BEGINNING_CALLBACK));
        displayGeneralInitMessage(chatId, message, buttons);
    }

    private void displayForAlreadyBooked(Long chatId, String bookedSlotNumber) {
        String msg = String.format("You have booked the slot #%s. What are you going to do?", bookedSlotNumber);
        List<InlineKeyboardButton> buttons = new ArrayList<>();
        buttons.add(new InlineKeyboardButton().setText("Find neighbours").setCallbackData(SHOW_ENGAGED_SLOTS));
        buttons.add(new InlineKeyboardButton().setText("Drop booking").setCallbackData(String.format(DROP_BOOKING_TPL, bookedSlotNumber)));
        buttons.add(new InlineKeyboardButton().setText("Cancel").setCallbackData(TO_BEGINNING_CALLBACK));
        displayGeneralInitMessage(chatId, msg, buttons);
    }

    private void displayGeneralInitMessage(Long chatId, String message, List<InlineKeyboardButton> buttonsLine) {
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        keyboard.add(buttonsLine);
        markupInline.setKeyboard(keyboard);
        getMessagePublisher().sendMessageWithKeyboard(chatId, message, markupInline);
    }
}
