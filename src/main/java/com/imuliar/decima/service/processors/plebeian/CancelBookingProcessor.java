package com.imuliar.decima.service.processors.plebeian;

import com.imuliar.decima.service.processors.AbstractUpdateProcessor;
import com.imuliar.decima.service.util.InlineKeyboardMarkupBuilder;
import com.vdurmont.emoji.EmojiParser;
import java.time.LocalDate;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import static com.imuliar.decima.service.util.Callbacks.CANCEL_MY_BOOKING;
import static com.imuliar.decima.service.util.Callbacks.TO_BEGINNING;

/**
 * <p>Process "cancel booking" action</p>
 *
 * @author imuliar
 * @since 0.0.1
 */
@Service
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CancelBookingProcessor extends AbstractUpdateProcessor {

    private static final String RELEASE_MESSAGE_PATTERN = ":information_source: BOT NOTIFICATION:\n[Someone](tg://user?id=%d) has shared their slot for today.";

    @Override
    public boolean isMatch(Update update) {
        return callbackMatching.apply(update, CANCEL_MY_BOOKING);
    }

    @Override
    protected void doProcess(Update update, Long chatId) {
        int userId = chatId.intValue();
        if (getBookingRepository().findByUserIdAndDate(userId, LocalDate.now()).isPresent()) {
            getBookingRepository().removeByUserIdAndDate(userId, LocalDate.now());
            getMessagePublisher().sendMessageWithKeyboard(chatId, getMsg("msg.drop_booking_confirmed"),
                    new InlineKeyboardMarkupBuilder()
                            .addButton(new InlineKeyboardButton(getMsg("btn.back")).setCallbackData(TO_BEGINNING)).build());
            getMessagePublisher().sendSimpleMessageToGroup(EmojiParser.parseToUnicode(String.format(RELEASE_MESSAGE_PATTERN, userId)));
        }
    }
}
