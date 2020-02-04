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

import java.util.Arrays;
import java.util.Optional;

import static com.imuliar.decima.service.util.Callbacks.PERIOD_REMOVE_TPL;
import static com.imuliar.decima.service.util.Callbacks.TO_BEGINNING;
import static com.imuliar.decima.service.util.RegexPatterns.PERIOD_ID_PATTERN;

/**
 * <p>View action buttons for managing chosen vacant period</p>
 *
 * @author imuliar
 * @since 0.0.1
 */
@Service
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ManipulatePeriodPatricianProcessor extends AbstractUpdateProcessor {

    @Override
    public boolean isMatch(Update update) {
        return regexpEvaluating.apply(update, PERIOD_ID_PATTERN);
    }

    @Override
    protected void doProcess(Update update, Long chatId) {
        Long periodId = Long.valueOf(Arrays.asList(update.getCallbackQuery().getData().split(":")).get(1));
        VacantPeriod vacantPeriod = getVacantPeriodRepository().findById(periodId)
                .orElseThrow(() -> new IllegalStateException("Cannot find vacant period by id: " + periodId));
        String msg = getMsg("msg.manipulate_period", vacantPeriod.getPeriodStart().toString(), vacantPeriod.getPeriodEnd().toString());
        getMessagePublisher().sendMessageWithKeyboard(chatId, msg, new InlineKeyboardMarkupBuilder()
                .addButton(new InlineKeyboardButton(getMsg("btn.cancel_sharing")).setCallbackData(String.format(PERIOD_REMOVE_TPL, periodId)))
                .addButton(new InlineKeyboardButton(getMsg("btn.back")).setCallbackData(TO_BEGINNING))
                .build());
    }

    @Override
    protected Optional<SessionState> getNextState() {
        return Optional.of(getStateFactory().getManagePeriodState());
    }
}
