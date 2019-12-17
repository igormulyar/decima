package com.imuliar.decima.service.processors.patrician.date;

import com.imuliar.decima.entity.ParkingUser;
import com.imuliar.decima.service.processors.AbstractUpdateProcessor;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.time.LocalDate;

import static com.imuliar.decima.service.session.ContextPropertyNames.CALENDAR_VIEW_DATE_PROP;
import static com.imuliar.decima.service.session.ContextPropertyNames.PICK_DATE_MSG_PROP;
import static com.imuliar.decima.service.util.Callbacks.MONTH_BACK_CALLBACK;

/**
 * <p>Rerender calendar to view previous month</p>
 *
 * @author imuliar
 * @since 0.0.1
 */
@Service
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class MonthBackProcessor extends AbstractUpdateProcessor {

    @Override
    public boolean isMatch(Update update) {
        return callbackMatching.apply(update, MONTH_BACK_CALLBACK);
    }

    @Override
    protected void doProcess(Update update, ParkingUser parkingUser, Long chatId) {
        LocalDate contextViewDate = (LocalDate)getSession().getContext().get(CALENDAR_VIEW_DATE_PROP);
        LocalDate monthBack = contextViewDate.minusMonths(1);
        getSession().getContext().put(CALENDAR_VIEW_DATE_PROP, monthBack);
        String msg = (String) getSession().getContext().get(PICK_DATE_MSG_PROP);

        getMessagePublisher().reRenderCalendar(chatId, update.getCallbackQuery().getMessage().getMessageId(), msg, monthBack);
    }
}
