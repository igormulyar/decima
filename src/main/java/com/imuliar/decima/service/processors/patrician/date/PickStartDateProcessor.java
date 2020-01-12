package com.imuliar.decima.service.processors.patrician.date;

import com.imuliar.decima.service.processors.AbstractUpdateProcessor;
import com.imuliar.decima.service.session.SessionState;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import static com.imuliar.decima.service.session.ContextPropertyNames.*;
import static com.imuliar.decima.service.util.RegexPatterns.DATE_MATCHING_PATTERN;

/**
 * <p>Remember start date and publish calendar for picking end date</p>
 *
 * @author imuliar
 * @since 0.0.1
 */
@Service
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class PickStartDateProcessor extends AbstractUpdateProcessor {

    @Override
    public boolean isMatch(Update update) {
        return regexpEvaluating.apply(update, DATE_MATCHING_PATTERN);
    }

    @Override
    protected void doProcess(Update update, Long chatId) {
        LocalDate inputStartDate = LocalDate.parse(update.getCallbackQuery().getData(), DateTimeFormatter.ISO_DATE);

        if (inputStartDate.isBefore(LocalDate.now())) {
            getMessagePublisher().popUpNotify(update.getCallbackQuery().getId(), "You cannot pick prior date");
            getSession().getContext().remove(START_DATE_PROP);
        } else {
            getSession().getContext().put(START_DATE_PROP, inputStartDate);

            getSession().getContext().put(CALENDAR_VIEW_DATE_PROP, LocalDate.now());
            String msg = "Start date selected: " + inputStartDate +
                    "\nNow please select the end date (inclusive) of slot sharing period";
            getSession().getContext().put(PICK_DATE_MSG_PROP, msg);
            getMessagePublisher().publishCalendar(chatId, msg, LocalDate.now());
        }
    }

    @Override
    protected Optional<SessionState> getNextState() {
        if (getSession().getContext().get(START_DATE_PROP) == null) {
            return super.getNextState();
        } else {
            return Optional.of(getStateFactory().getPickEndDateState());
        }
    }
}
