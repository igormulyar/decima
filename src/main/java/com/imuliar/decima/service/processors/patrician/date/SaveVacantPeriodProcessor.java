package com.imuliar.decima.service.processors.patrician.date;

import com.imuliar.decima.entity.Reservation;
import com.imuliar.decima.entity.VacantPeriod;
import com.imuliar.decima.service.processors.AbstractUpdateProcessor;
import com.imuliar.decima.service.session.SessionState;
import com.imuliar.decima.service.util.InlineKeyboardMarkupBuilder;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.time.LocalDate;
import java.util.Optional;

import static com.imuliar.decima.service.session.ContextPropertyNames.END_DATE_PROP;
import static com.imuliar.decima.service.session.ContextPropertyNames.START_DATE_PROP;
import static com.imuliar.decima.service.util.Callbacks.SAVE_VACANT_PERIOD;
import static com.imuliar.decima.service.util.Callbacks.TO_BEGINNING;

/**
 * <p>Save vacant period</p>
 *
 * @author imuliar
 * @since 0.0.1
 */
@Service
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class SaveVacantPeriodProcessor extends AbstractUpdateProcessor {
    @Override
    public boolean isMatch(Update update) {
        return callbackMatching.apply(update, SAVE_VACANT_PERIOD);
    }

    @Override
    protected void doProcess(Update update, Long chatId) {
        LocalDate startDate = (LocalDate) getSession().getContext().get(START_DATE_PROP);
        LocalDate endDate = (LocalDate) getSession().getContext().get(END_DATE_PROP);
        Assert.notNull(startDate, "startDate is NULL!");
        Assert.notNull(endDate, "endDate is NULL!");
        Integer userId = chatId.intValue();

        VacantPeriod vacantPeriod = new VacantPeriod(userId, startDate, endDate);
        if (startDate.equals(LocalDate.now())) {
            Reservation reservation = getReservationRepository().findByUserId(userId)
                    .orElseThrow(() -> new IllegalStateException("Cannot find reservation by slot owner id"));
            reservation.setLastPollTimestamp(LocalDate.now());
            getReservationRepository().save(reservation);
        }
        getVacantPeriodRepository().save(vacantPeriod);
        getMessagePublisher().sendMessageWithKeyboard(chatId, getMsg("msg.set_period_shared", startDate.toString(), endDate.toString()),
                new InlineKeyboardMarkupBuilder().addButton(new InlineKeyboardButton(getMsg("btn.to_beginning")).setCallbackData(TO_BEGINNING)).build());
    }

    @Override
    protected Optional<SessionState> getNextState() {
        return Optional.of(getStateFactory().getSlotOwnerInitialState());
    }
}
