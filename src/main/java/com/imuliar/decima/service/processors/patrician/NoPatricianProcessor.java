package com.imuliar.decima.service.processors.patrician;

import com.imuliar.decima.entity.Booking;
import com.imuliar.decima.entity.Reservation;
import com.imuliar.decima.entity.Slot;
import com.imuliar.decima.entity.VacantPeriod;
import com.imuliar.decima.service.impl.BookingRequestsSupplier;
import com.imuliar.decima.service.processors.AbstractUpdateProcessor;
import java.time.LocalDate;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

import static com.imuliar.decima.service.util.Callbacks.NO;

/**
 * <p>Process "NO" poll answer</p>
 *
 * @author imuliar
 * @since 0.0.1
 */
@Service
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class NoPatricianProcessor extends AbstractUpdateProcessor {

    @Autowired
    private BookingRequestsSupplier bookingRequestsSupplier;

    @Override
    public boolean isMatch(Update update) {
        return callbackMatching.apply(update, NO);
    }

    @Override
    protected void doProcess(Update update, Long chatId) {
        LocalDate today = LocalDate.now();
        Integer userId = chatId.intValue();
        Reservation reservation = getReservationRepository().findByUserId(userId)
                .orElseThrow(() -> new IllegalStateException("Cannot find reservation for current slot owner"));
        Slot plebeianSlot = reservation.getSlot();
        if (getVacantPeriodRepository().findByUserIdAndDate(userId, today).isEmpty()) {
            VacantPeriod vacantPeriod = new VacantPeriod(userId, today, today);
            getVacantPeriodRepository().save(vacantPeriod);
            getMessagePublisher().sendMsgWithBackBtn(chatId, getMsg("msg.pat_press_no"));

            Optional<User> possibleRequester = bookingRequestsSupplier.pullOutRandomRequester();
            if(possibleRequester.isPresent()){
                User requester = possibleRequester.get();
                Booking booking = new Booking(requester.getId(), plebeianSlot, today);
                getBookingRepository().save(booking);
                String msgForRequester = getMessageSourceFacade().getMsg("msg.pleb_shared_for_you", requester.getLanguageCode(), new String[]{userId.toString(), plebeianSlot.getNumber()});
                getMessagePublisher().sendMsgWithBackBtn(requester.getId().longValue(), msgForRequester);
            }
        }

        reservation.setLastPollTimestamp(LocalDate.now());
        getReservationRepository().save(reservation);
    }
}
