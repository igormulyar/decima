package com.imuliar.decima.service.processors.patrician;

import com.imuliar.decima.entity.VacantPeriod;
import com.imuliar.decima.service.processors.AbstractUpdateProcessor;
import com.imuliar.decima.service.session.SessionState;
import com.imuliar.decima.service.util.InlineKeyboardMarkupBuilder;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static com.imuliar.decima.service.util.Callbacks.*;

/**
 * <p>Display the list of scheduled vacant periods</p>
 *
 * @author imuliar
 * @since 0.0.1
 */
@Service
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ListPeriodsPatricianProcessor extends AbstractUpdateProcessor {

    @Override
    public boolean isMatch(Update update) {
        return callbackMatching.apply(update, LIST_VACANT_PERIODS);
    }

    @Override
    protected void doProcess(Update update, Long chatId) {

        List<VacantPeriod> scheduledPeriods = getVacantPeriodRepository().findNotExpired(chatId.intValue(), LocalDate.now());
        scheduledPeriods.sort(Comparator.comparing(VacantPeriod::getPeriodStart));

        InlineKeyboardMarkupBuilder keyboardBuilder = new InlineKeyboardMarkupBuilder();
        for (VacantPeriod period : scheduledPeriods) {
            LocalDate start = period.getPeriodStart();
            LocalDate end = period.getPeriodEnd();
            String buttonLabel = start.equals(end) ? start.toString() : String.format("%s - %s", period.getPeriodStart().toString(), period.getPeriodEnd().toString());
            String buttonCallback = String.format(PERIOD_ID_TPL, period.getId());
            keyboardBuilder.addButtonAtNewRaw(new InlineKeyboardButton(buttonLabel).setCallbackData(buttonCallback));
        }
        keyboardBuilder.addButtonAtNewRaw(new InlineKeyboardButton(getMsg("btn.back")).setCallbackData(TO_BEGINNING));

        getMessagePublisher().sendMessage(chatId, getMsg("msg.list_periods"), keyboardBuilder.build());
    }

    @Override
    protected Optional<SessionState> getNextState() {
        return Optional.of(getStateFactory().getListPeriodsState());
    }
}
