package com.imuliar.decima.service.processors.patrician;

import com.imuliar.decima.entity.Reservation;
import com.imuliar.decima.service.processors.AbstractUpdateProcessor;
import com.imuliar.decima.service.util.InlineKeyboardMarkupBuilder;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.time.LocalDate;

import static com.imuliar.decima.service.util.Callbacks.TO_BEGINNING;
import static com.imuliar.decima.service.util.Callbacks.YES;

/**
 * <p>Process "YES" poll answer</p>
 *
 * @author imuliar
 * @since 0.0.1
 */
@Service
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class YesPatricianProcessor extends AbstractUpdateProcessor {

    @Override
    public boolean isMatch(Update update) {
        return callbackMatching.apply(update, YES);
    }

    @Override
    protected void doProcess(Update update, Long chatId) {
        Reservation reservation = getReservationRepository().findByUserId(chatId.intValue())
                .orElseThrow(() -> new IllegalStateException("Cannot find reservation for current slot owner"));
        reservation.setLastPollTimestamp(LocalDate.now());
        getReservationRepository().save(reservation);
        getMessagePublisher().sendMessageWithKeyboard(chatId, "Answer accepted.", new InlineKeyboardMarkupBuilder()
                .addButton(new InlineKeyboardButton("Back").setCallbackData(TO_BEGINNING)).build());
    }
}
