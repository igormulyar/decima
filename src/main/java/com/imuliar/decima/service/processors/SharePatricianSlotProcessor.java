package com.imuliar.decima.service.processors;

import com.imuliar.decima.entity.Reservation;
import com.imuliar.decima.entity.VacantPeriod;
import com.imuliar.decima.service.util.InlineKeyboardMarkupBuilder;
import com.vdurmont.emoji.EmojiParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import javax.transaction.Transactional;
import java.time.LocalDate;

import static com.imuliar.decima.service.util.Callbacks.SET_FREE_TODAY;
import static com.imuliar.decima.service.util.Callbacks.TO_BEGINNING;

/**
 * <p>Process "set slot free" order for both types of users who has reservations</p>
 *
 * @author imuliar
 * @since 0.0.1
 */
@Service
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class SharePatricianSlotProcessor extends AbstractUpdateProcessor {

    @Value("${decima.bot.id}")
    private Integer botId;

    @Override
    public boolean isMatch(Update update) {
        return callbackMatching.apply(update, SET_FREE_TODAY);
    }

    @Override
    @Transactional
    protected void doProcess(Update update, Long chatId) {
        Reservation reservation = getReservationRepository().findByUserId(chatId.intValue())
                .orElseThrow(() -> new IllegalStateException("This update should be processed by slot owner and reservation should exist"));

        if (getVacantPeriodRepository().hasIntersections(chatId.intValue(), LocalDate.now(), LocalDate.now())) {
            getMessagePublisher().popUpNotify(update.getCallbackQuery().getId(), "Your slot is already shared today");
        } else {
            VacantPeriod vacantPeriod = new VacantPeriod(chatId.intValue(), LocalDate.now(), LocalDate.now());
            getVacantPeriodRepository().save(vacantPeriod);

            publishNotificationToCurrentUser(chatId);
            publishNotificationToGroupChat(chatId.intValue());
        }

        reservation.setLastPollTimestamp(LocalDate.now());
        getReservationRepository().save(reservation);
    }

    private void publishNotificationToGroupChat(Integer userId) {
        String message = ":information_source: [BOT NOTIFICATION](tg://user?id=%d) :\n[Someone](tg://user?id=%d) has shared their slot for today.";
        getMessagePublisher()
                .sendSimpleMessageToGroup(EmojiParser.parseToUnicode(String.format(message, botId, userId)));
    }

    private void publishNotificationToCurrentUser(Long chatId) {
        String message = EmojiParser.parseToUnicode(":clap: You've successfully shared your parking slot with other users.\n By the end of this day it can be engaged by any other user " +
                "and you woun't be able to cancel sharing.");
        getMessagePublisher().sendMessageWithKeyboard(chatId, message, new InlineKeyboardMarkupBuilder()
                .addButton(new InlineKeyboardButton(getMsg("btn.back")).setCallbackData(TO_BEGINNING)).build());
    }
}
