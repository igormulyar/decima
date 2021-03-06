package com.imuliar.decima.service.processors.patrician.date;

import com.imuliar.decima.service.processors.AbstractUpdateProcessor;
import com.imuliar.decima.service.session.SessionState;
import com.imuliar.decima.service.util.InlineKeyboardMarkupBuilder;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Optional;

import static com.imuliar.decima.service.session.ContextPropertyNames.*;
import static com.imuliar.decima.service.util.Callbacks.SAVE_VACANT_PERIOD;
import static com.imuliar.decima.service.util.Callbacks.TO_BEGINNING;
import static com.imuliar.decima.service.util.RegexPatterns.DATE_MATCHING_PATTERN;

/**
 * <p>Remember end date and ask to confirm</p>
 *
 * @author imuliar
 * @since 0.0.1
 */
@Service
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class PickEndDateProcessor extends AbstractUpdateProcessor {

    @Override
    public boolean isMatch(Update update) {
        return regexpEvaluating.apply(update, DATE_MATCHING_PATTERN);
    }

    @Override
    protected void doProcess(Update update, Long chatId) {
        LocalDate inputEndDate = LocalDate.parse(update.getCallbackQuery().getData(), DateTimeFormatter.ISO_DATE);
        LocalDate inputStartDate = (LocalDate) getSession().getContext().get(START_DATE_PROP);

        if (inputEndDate.isBefore(inputStartDate)) {
            getMessagePublisher().showPopUpNotification(update.getCallbackQuery().getId(), getMsg("alert.prior_date_err"));
            getSession().getContext().remove(END_DATE_PROP);
        } else if (getVacantPeriodRepository().hasIntersections(chatId.intValue(), inputStartDate, inputEndDate)) {
            getMessagePublisher().showPopUpNotification(update.getCallbackQuery().getId(), getMsg("alert.has_intersections_err"));
            getSession().getContext().remove(END_DATE_PROP);
        } else {
            getSession().getContext().put(END_DATE_PROP, inputEndDate);

            String msg = getMsg("msg.end_date_selected", inputEndDate.toString(), inputStartDate.toString(), inputEndDate.toString());
            getSession().getContext().put(PICK_DATE_MSG_PROP, msg);
            getMessagePublisher().sendMessage(chatId, msg, new InlineKeyboardMarkupBuilder()
                    .addButton(new InlineKeyboardButton("Confirm").setCallbackData(SAVE_VACANT_PERIOD))
                    .addButton(new InlineKeyboardButton("Cancel").setCallbackData(TO_BEGINNING)).build());
        }
    }

    @Override
    protected Optional<SessionState> getNextState() {
        Map<String, Object> context = getSession().getContext();
        if (context.get(END_DATE_PROP) == null) {
            return super.getNextState();
        } else {
            return Optional.of(getStateFactory().getConfirmSharingPeriodState());
        }
    }
}
