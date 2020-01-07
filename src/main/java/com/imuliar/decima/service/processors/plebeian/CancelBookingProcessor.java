package com.imuliar.decima.service.processors.plebeian;

import com.imuliar.decima.service.processors.AbstractUpdateProcessor;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.time.LocalDate;

import static com.imuliar.decima.service.util.Callbacks.CANCEL_MY_BOOKING;

/**
 * <p>Process "cancel booking" action</p>
 *
 * @author imuliar
 * @since 0.0.1
 */
@Service
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CancelBookingProcessor extends AbstractUpdateProcessor {

    @Override
    public boolean isMatch(Update update) {
        return callbackMatching.apply(update, CANCEL_MY_BOOKING);
    }

    @Override
    protected void doProcess(Update update, Long chatId) {
        getBookingRepository().removeByUserIdAndDate(chatId.intValue(), LocalDate.now());
        getMessagePublisher().popUpNotify(update.getCallbackQuery().getId(), "Booking has been dropped.");
    }
}