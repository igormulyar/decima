package com.imuliar.decima.service.processors.plebeian;

import com.imuliar.decima.entity.Booking;
import com.imuliar.decima.entity.Slot;
import com.imuliar.decima.service.processors.AbstractUpdateProcessor;
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
    protected void doProcess(Update update, Long chatId) {

        if (!getBookingRepository().findByUserIdAndDate(chatId.intValue(), LocalDate.now()).isPresent()) {
            List<Slot> freeSlots = getSlotRepository().findFreeSlots(LocalDate.now());
            if (CollectionUtils.isEmpty(freeSlots)) {
                publishNotFoundMessage(chatId);
            } else {
                bookRandomSlot(chatId, freeSlots);
            }
        } else {
            getMessagePublisher().popUpNotify(update.getCallbackQuery().getId(), getMsg("alert.already_have_booked"));
        }
    }

    private void publishNotFoundMessage(Long chatId) {
        getMessagePublisher().sendMessageWithKeyboard(chatId, getMsg("msg.can't_find_but_can_poll"), new InlineKeyboardMarkupBuilder()
                .addButton(new InlineKeyboardButton(getMsg("btn.lets_poll")).setCallbackData(POLL))
                .addButton(new InlineKeyboardButton(getMsg("btn.back")).setCallbackData(TO_BEGINNING))
                .build());
    }

    private void bookRandomSlot(Long chatId, List<Slot> freeSlots) {
        Slot slotToBeBooked = freeSlots.get(freeSlots.size() - 1);
        Booking booking = new Booking(chatId.intValue(), slotToBeBooked, LocalDate.now());
        getBookingRepository().save(booking);

        getMessagePublisher().sendImage(chatId, "", getPlanImageUrl());
        String message = getMsg("msg.slot_booked", slotToBeBooked.getNumber());
        getMessagePublisher().sendMessageWithKeyboard(chatId, message, new InlineKeyboardMarkupBuilder()
                .addButton(new InlineKeyboardButton().setText(getMsg("btn.drop_booking")).setCallbackData(CANCEL_MY_BOOKING))
                .addButtonAtNewRaw(new InlineKeyboardButton().setText(getMsg("btn.back")).setCallbackData(TO_BEGINNING))
                .build());
    }
}
