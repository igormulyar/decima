package com.imuliar.decima.service.processors;

import com.imuliar.decima.entity.Booking;
import com.imuliar.decima.entity.ParkingUser;
import com.imuliar.decima.entity.Slot;
import com.imuliar.decima.service.util.InlineKeyboardMarkupBuilder;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.time.LocalDate;
import java.util.List;

import static com.imuliar.decima.service.util.Callbacks.*;

/**
 * <p>Show free slots to ordinary user</p>
 *
 * @author imuliar
 * @since 0.0.1
 */
@Service
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class FindRandomSlotPlebeianProcessor extends AbstractUpdateProcessor {

    @Override
    public boolean isMatch(Update update) {
        return callbackMatching.apply(update, FIND_FREE_SLOT);
    }

    @Override
    void doProcess(Update update, ParkingUser parkingUser, Long chatId) {
        List<Slot> freeSlots = getSlotRepository().findFreeSlots(LocalDate.now());
        if (CollectionUtils.isEmpty(freeSlots)) {
            publishNotFoundPopupMessage(update);
        } else {
            bookRandomSlot(parkingUser, chatId, freeSlots);
        }
    }

    private void publishNotFoundPopupMessage(Update update) {
        getMessagePublisher().popUpNotify(update.getCallbackQuery().getId(), "Can't find free slot for parking :(");
    }

    private void bookRandomSlot(ParkingUser parkingUser, Long chatId, List<Slot> freeSlots) {
        Slot slotToBeBooked = freeSlots.get(freeSlots.size() - 1);
        Booking booking = new Booking(parkingUser, slotToBeBooked, LocalDate.now());
        getBookingRepository().save(booking);
        String message = String.format("The slot *# %s* is booked for you. You can park your car there today.\n ***\n***\n %s", slotToBeBooked.getNumber(), getPlanImageUrl());
        getMessagePublisher().sendMessageWithKeyboard(chatId, message, new InlineKeyboardMarkupBuilder()
                .addButton(new InlineKeyboardButton().setText("Cancel booking").setCallbackData(CANCEL_MY_BOOKING))
                .addButtonAtNewRaw(new InlineKeyboardButton().setText("Back").setCallbackData(TO_BEGINNING))
                .build());
    }
}
