package com.imuliar.decima.service.processors.patrician;

import com.imuliar.decima.entity.Booking;
import com.imuliar.decima.entity.Reservation;
import com.imuliar.decima.entity.VacantPeriod;
import com.imuliar.decima.service.processors.AbstractUpdateProcessor;
import com.imuliar.decima.service.session.SessionState;
import com.imuliar.decima.service.util.InlineKeyboardMarkupBuilder;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import static com.imuliar.decima.service.util.Callbacks.FIND_FREE_SLOT;
import static com.imuliar.decima.service.util.Callbacks.TO_BEGINNING;
import static com.imuliar.decima.service.util.RegexPatterns.PERIOD_REMOVE_PATTERN;

/**
 * <p>Process user's request to cancel sharing their slot with others</p>
 *
 * @author imuliar
 * @since 0.0.1
 */
@Service
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CancelSharingPatricianProcessor extends AbstractUpdateProcessor {

    @Override
    public boolean isMatch(Update update) {
        return regexpEvaluating.apply(update, PERIOD_REMOVE_PATTERN);
    }

    @Override
    protected void doProcess(Update update, Long chatId) {
        Long periodId = Long.valueOf(Arrays.asList(update.getCallbackQuery().getData().split(":")).get(1));
        VacantPeriod period = getVacantPeriodRepository().findById(periodId)
                .orElseThrow(() -> new IllegalStateException("Cannot find vacant period by id: " + periodId));
        Reservation reservation = getReservationRepository().findByUserId(period.getUserId())
                .orElseThrow(() -> new IllegalStateException("Cannot find reservation by user id"));

        Optional<Booking> possibleBooking = getBookingRepository().findBySlotNumberAndDate(reservation.getSlot().getNumber(), LocalDate.now());

        if (possibleBooking.isPresent() && period.getPeriodStart().equals(period.getPeriodEnd())) {
            getMessagePublisher().sendMessageWithKeyboard(chatId, getMsg("msg.pat_cant_cancel"), new InlineKeyboardMarkupBuilder()
                    .addButton(new InlineKeyboardButton(getMsg("btn.find_another_slot")).setCallbackData(FIND_FREE_SLOT))
                    .addButtonAtNewRaw(new InlineKeyboardButton(getMsg("btn.cancel")).setCallbackData(TO_BEGINNING)).build());
        } else if (possibleBooking.isPresent() && hasIntersection(period, possibleBooking.get())) {
            period.setPeriodStart(LocalDate.now().plusDays(1));
            getVacantPeriodRepository().save(period);
            publishMessage(chatId, getMsg("msg.period_record_changed", new String[] {period.getPeriodStart().toString(), period.getPeriodEnd().toString()}));
        } else {
            getVacantPeriodRepository().delete(period);
            publishMessage(chatId, getMsg("msg.period_canceled", new String[]{period.getPeriodStart().toString(), period.getPeriodEnd().toString()}));
        }
    }

    private void publishMessage(Long chatId, String msg) {
        getMessagePublisher().sendMessageWithKeyboard(chatId, msg, new InlineKeyboardMarkupBuilder()
                .addButton(new InlineKeyboardButton(getMsg("btn.back")).setCallbackData(TO_BEGINNING)).build());
    }

    private boolean hasIntersection(VacantPeriod vacantPeriod, Booking booking) {
        LocalDate periodStart = vacantPeriod.getPeriodStart();
        LocalDate periodEnd = vacantPeriod.getPeriodEnd();
        LocalDate bookingDate = booking.getDate();
        return bookingDate.isAfter(periodStart) && bookingDate.isBefore(periodEnd) || bookingDate.equals(periodStart) || bookingDate.equals(periodEnd);
    }

    @Override
    protected Optional<SessionState> getNextState() {
        return Optional.of(getStateFactory().getSlotOwnerInitialState());
    }
}
