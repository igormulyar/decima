package com.imuliar.decima.service.processors.plebeian;

import com.imuliar.decima.entity.Reservation;
import com.imuliar.decima.service.processors.AbstractUpdateProcessor;
import com.imuliar.decima.service.util.InlineKeyboardMarkupBuilder;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.time.LocalDate;

import static com.imuliar.decima.service.util.Callbacks.*;

/**
 * <p></p>
 *
 * @author imuliar
 * @since 0.0.1
 */
@Service
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class DoThePollPlebeianProcessor extends AbstractUpdateProcessor {

    @Override
    public boolean isMatch(Update update) {
        return callbackMatching.apply(update, POLL);
    }

    @Override
    protected void doProcess(Update update, Long chatId) {

        LocalDate today = LocalDate.now();
        for (Reservation reservation : getReservationRepository().findUnpolled(today)) {
            getMessagePublisher().sendMessageWithKeyboard(reservation.getUserId().longValue(), today.toString() + " POLL\n Are you going to park today?",
                    new InlineKeyboardMarkupBuilder()
                            .addButton(new InlineKeyboardButton("YES! I'm gonna park").setCallbackData(YES))
                            .addButtonAtNewRaw(new InlineKeyboardButton("NO! I'd like to share my slot with others!").setCallbackData(NO))
                            .build());
        }
        getMessagePublisher().sendMessageWithKeyboard(chatId, "Ok, I'll make a poll to find users who are not going to park today. " +
                        "You can try to request a slot for you later.",
                new InlineKeyboardMarkupBuilder()
                        .addButton(new InlineKeyboardButton("Back").setCallbackData(TO_BEGINNING))
                        .build());
    }
}
