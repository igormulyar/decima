package com.imuliar.decima.service.processors;

import com.imuliar.decima.entity.Booking;
import com.imuliar.decima.entity.Reservation;
import com.imuliar.decima.entity.Slot;
import com.imuliar.decima.entity.VacantPeriod;
import com.imuliar.decima.service.impl.BookingRequestsSupplier;
import com.imuliar.decima.service.util.InlineKeyboardMarkupBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.Optional;

import static com.imuliar.decima.service.util.Callbacks.SET_FREE_TODAY;
import static com.imuliar.decima.service.util.Callbacks.TO_BEGINNING;

/**
 * <p>Process "set slot free" order for both types of users who has reservations</p>
 *
 * @author imuliar
 * @since 0.0.1
 */
@Service
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class SharePatricianSlotProcessor extends AbstractUpdateProcessor {

    @Autowired
    private BookingRequestsSupplier bookingRequestsSupplier;

    @Override
    public boolean isMatch(Update update) {
        return callbackMatching.apply(update, SET_FREE_TODAY);
    }

    @Override
    @Transactional
    protected void doProcess(Update update, Long chatId) {
        LocalDate today = LocalDate.now();
        Integer userId = chatId.intValue();
        Reservation reservation = getReservationRepository().findByUserId(chatId.intValue())
                .orElseThrow(() -> new IllegalStateException("This update should be processed by slot owner and reservation should exist"));
        Slot plebeianSlot = reservation.getSlot();

        if (getVacantPeriodRepository().hasIntersections(chatId.intValue(), LocalDate.now(), LocalDate.now())) {
            getMessagePublisher().popUpNotify(update.getCallbackQuery().getId(), getMsg("alert.slot_already_shared"));
        } else {
            VacantPeriod vacantPeriod = new VacantPeriod(userId, today, today);
            getVacantPeriodRepository().save(vacantPeriod);
            publishNotificationToCurrentUser(update);

            Optional<User> possibleRequester = bookingRequestsSupplier.pullOutRandomRequester();
            if(possibleRequester.isPresent()){
                User requester = possibleRequester.get();
                Booking booking = new Booking(requester.getId(), plebeianSlot, today);
                getBookingRepository().save(booking);
                String msgForRequester = getMessageSourceFacade().getMsg("msg.pleb_shared_for_you", requester.getLanguageCode(), userId.toString(), plebeianSlot.getNumber());
                getMessagePublisher().sendMsgWithBackBtn(requester.getId().longValue(), msgForRequester);
            }

        }

        reservation.setLastPollTimestamp(LocalDate.now());
        getReservationRepository().save(reservation);
    }

    protected void publishNotificationToCurrentUser(Update update) {
        getMessagePublisher().sendMessageWithKeyboard(update.getCallbackQuery().getMessage().getChatId(), getMsg("msg.your_slot_shared"), new InlineKeyboardMarkupBuilder()
                .addButton(new InlineKeyboardButton(getMsg("btn.back")).setCallbackData(TO_BEGINNING)).build());
    }
}
