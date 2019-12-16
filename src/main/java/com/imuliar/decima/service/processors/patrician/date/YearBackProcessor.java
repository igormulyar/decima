package com.imuliar.decima.service.processors.patrician.date;

import com.imuliar.decima.entity.ParkingUser;
import com.imuliar.decima.service.processors.AbstractUpdateProcessor;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

import static com.imuliar.decima.service.util.Callbacks.YEAR_BACK_CALLBACK;

/**
 * <p>Go to the calendar's year before</p>
 *
 * @author imuliar
 * @since 0.0.1
 */
@Service
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class YearBackProcessor extends AbstractUpdateProcessor {

    @Override
    public boolean isMatch(Update update) {
        return callbackMatching.apply(update, YEAR_BACK_CALLBACK);
    }

    @Override
    protected void doProcess(Update update, ParkingUser parkingUser, Long chatId) {
        getMessagePublisher().
    }
}
