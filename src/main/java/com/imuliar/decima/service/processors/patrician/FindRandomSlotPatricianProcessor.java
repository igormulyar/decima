package com.imuliar.decima.service.processors.patrician;

import com.imuliar.decima.entity.Reservation;
import com.imuliar.decima.entity.Slot;
import com.imuliar.decima.service.processors.AbstractUpdateProcessor;
import com.imuliar.decima.service.util.InlineKeyboardMarkupBuilder;
import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import static com.imuliar.decima.service.util.Callbacks.FIND_FREE_SLOT;
import static com.imuliar.decima.service.util.Callbacks.TO_BEGINNING;

/**
 * <p>Find a slot for patrician who's slot was booked after the sharing</p>
 *
 * @author imuliar
 * @since 0.0.1
 */
@Service
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class FindRandomSlotPatricianProcessor extends AbstractUpdateProcessor {

    @Override
    public boolean isMatch(Update update) {
        return callbackMatching.apply(update, FIND_FREE_SLOT);
    }

    @Override
    protected void doProcess(Update update, Long chatId) {
        LocalDate today = LocalDate.now();

        Reservation reservation = getReservationRepository().findByUserId(chatId.intValue())
                .orElseThrow(() -> new IllegalStateException("Can't find reservation"));
        if (getBookingRepository().findBySlotNumberAndDate(reservation.getSlot().getNumber(), today).isPresent()) {
            List<Slot> freeSlots = getSlotRepository().findFreeSlots(today);
            if (freeSlots.isEmpty()) {
                getMessagePublisher().sendMessage(chatId, getMsg("msg.cant_find_free"), new InlineKeyboardMarkupBuilder()
                        .addButton(new InlineKeyboardButton().setText(getMsg("btn.back")).setCallbackData(TO_BEGINNING)).build());
            } else {
                getMessagePublisher().sendImage(chatId, "", getPlanImageUrl());
                getMessagePublisher().sendMessage(chatId, getMsg("msg.book_success", freeSlots.get(0).getNumber()), new InlineKeyboardMarkupBuilder()
                        .addButton(new InlineKeyboardButton().setText(getMsg("btn.back")).setCallbackData(TO_BEGINNING)).build());
            }
        } else {
            getMessagePublisher().showPopUpNotification(update.getCallbackQuery().getId(), getMsg("alert.try_to_cancel_sharing", reservation.getSlot().getNumber()));
        }
    }
}
