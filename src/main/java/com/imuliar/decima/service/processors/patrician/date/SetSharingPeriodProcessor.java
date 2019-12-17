package com.imuliar.decima.service.processors.patrician.date;

import com.imuliar.decima.entity.ParkingUser;
import com.imuliar.decima.service.processors.AbstractUpdateProcessor;
import com.imuliar.decima.service.state.SessionState;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.time.LocalDate;
import java.util.Optional;

import static com.imuliar.decima.service.session.ContextPropertyNames.CALENDAR_VIEW_DATE_PROP;
import static com.imuliar.decima.service.session.ContextPropertyNames.PICK_DATE_MSG_PROP;
import static com.imuliar.decima.service.util.Callbacks.SET_SHARING_PERIOD;

/**
 * <p>Print "Select from-date" message</p>
 *
 * @author imuliar
 * @since 0.0.1
 */
@Service
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class SetSharingPeriodProcessor extends AbstractUpdateProcessor {

    @Override
    public boolean isMatch(Update update) {
        return callbackMatching.apply(update, SET_SHARING_PERIOD);
    }

    @Override
    protected void doProcess(Update update, ParkingUser parkingUser, Long chatId) {
        getSession().getContext().put(CALENDAR_VIEW_DATE_PROP, LocalDate.now());
        String msg = "Select the start date of slot sharing period";
        getSession().getContext().put(PICK_DATE_MSG_PROP, msg);
        getMessagePublisher().publishCalendar(chatId, msg, LocalDate.now());
    }

    @Override
    protected Optional<SessionState> getNextState() {
        return Optional.of(getStateFactory().getPickStartDateState());
    }
}
