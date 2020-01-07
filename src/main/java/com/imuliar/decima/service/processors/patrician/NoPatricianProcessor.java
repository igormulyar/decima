package com.imuliar.decima.service.processors.patrician;

import com.imuliar.decima.entity.Reservation;
import com.imuliar.decima.service.processors.AbstractUpdateProcessor;
import com.imuliar.decima.service.util.InlineKeyboardMarkupBuilder;
import com.vdurmont.emoji.EmojiParser;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.time.LocalDate;

import static com.imuliar.decima.service.util.Callbacks.NO;
import static com.imuliar.decima.service.util.Callbacks.TO_BEGINNING;

/**
 * <p>Process "NO" poll answer</p>
 *
 * @author imuliar
 * @since 0.0.1
 */
@Service
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class NoPatricianProcessor extends AbstractUpdateProcessor {

    private static final String RELEASE_MESSAGE_PATTERN = ":information_source: BOT NOTIFICATION:\n[Someone](tg://user?id=%d) has shared their slot for today.";

    @Override
    public boolean isMatch(Update update) {
        return callbackMatching.apply(update, NO);
    }

    @Override
    protected void doProcess(Update update, Long chatId) {
        Reservation reservation = getReservationRepository().findByUserId(chatId.intValue())
                .orElseThrow(() -> new IllegalStateException("Cannot find reservation for current slot owner"));
        reservation.setLastPollTimestamp(LocalDate.now());
        getReservationRepository().save(reservation);

        publishNotificationToCurrentUser(chatId);
        publishNotificationToGroupChat(chatId.intValue());
    }

    private void publishNotificationToGroupChat(Integer userId) {
        getMessagePublisher()
                .sendSimpleMessageToGroup(EmojiParser.parseToUnicode(String.format(RELEASE_MESSAGE_PATTERN, userId)));
    }

    private void publishNotificationToCurrentUser(Long chatId) {
        String message = EmojiParser.parseToUnicode(":clap: You've successfully shared your parking slot with other users.\n By the end of this day it can be engaged by any other user " +
                "and you woun't be able to cancel sharing.");
        getMessagePublisher().sendMessageWithKeyboard(chatId, message, new InlineKeyboardMarkupBuilder()
                .addButton(new InlineKeyboardButton("Back").setCallbackData(TO_BEGINNING)).build());
    }
}
