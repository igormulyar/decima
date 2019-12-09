package com.imuliar.decima.service.processors;

import com.imuliar.decima.entity.ParkingUser;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.time.LocalDate;

import static com.imuliar.decima.service.util.Callbacks.CANCEL_MY_BOOKING;

/**
 * <p>Process "cancel booking" action</p>
 *
 * @author imuliar
 * @since 0.0.1
 */
@Service
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CancelBookingProcessor extends AbstractUpdateProcessor {

    @Override
    public boolean isMatch(Update update) {
        return callbackMatching.apply(update, CANCEL_MY_BOOKING);
    }

    @Override
    void doProcess(Update update, ParkingUser parkingUser, Long chatId) {
        getBookingRepository().deleteBooking(parkingUser.getTelegramUserId(), LocalDate.now());
        getMessagePublisher().popUpNotify(update.getCallbackQuery().getId(), "Booking has been dropped.");
    }
}
