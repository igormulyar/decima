package com.imuliar.decima.service.processors.plebeian;

import com.imuliar.decima.entity.Reservation;
import com.imuliar.decima.service.impl.PollingService;
import com.imuliar.decima.service.processors.AbstractUpdateProcessor;
import com.imuliar.decima.service.util.InlineKeyboardMarkupBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.time.LocalDate;

import static com.imuliar.decima.service.util.Callbacks.*;

/**
 * <p>Handle user's request to poll slot owners</p>
 *
 * @author imuliar
 * @since 0.0.1
 */
@Service
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class DoThePollPlebeianProcessor extends AbstractUpdateProcessor {

    @Autowired
    private PollingService pollingService;

    @Override
    public boolean isMatch(Update update) {
        return callbackMatching.apply(update, POLL);
    }

    @Override
    protected void doProcess(Update update, Long chatId) {
        pollingService.doThePoll(update);
        getMessagePublisher().sendMsgWithBackBtn(chatId, getMsg("msg.poll_triggered"));
    }
}
