package com.imuliar.decima.service.processors;

import com.imuliar.decima.entity.Booking;
import com.imuliar.decima.entity.ParkingUser;
import com.imuliar.decima.entity.Slot;
import com.imuliar.decima.service.state.AbstractState;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

import static com.imuliar.decima.service.util.RegexPatterns.BOOK_MATCHING_PATTERN;

/**
 * <p></p>
 *
 * @author imuliar
 * @since 0.0.1
 */
@Service
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class BookSlotPatricianProcessor extends AbstractUpdateProcessor {

    @Override
    public boolean isMatch(Update update) {
        return regexpEvaluating.apply(update, BOOK_MATCHING_PATTERN);
    }

    @Override
    Optional<AbstractState> doProcess(Update update, ParkingUser parkingUser, Long chatId) {
        String callbackString = update.getCallbackQuery().getData();
        List<String> splitStringData = Arrays.asList(callbackString.split("#"));
        Slot slot = getSlotRepository().findByNumber(splitStringData.get(0))
                .orElseThrow(() -> new IllegalStateException("Booked slot should exist"));
        LocalDate bookingDate = LocalDate.parse(splitStringData.get(2), DateTimeFormatter.ISO_DATE);
        Booking booking = new Booking(parkingUser, slot, bookingDate);
        getBookingRepository().save(booking);
        getMessagePublisher().popUpNotify(update.getCallbackQuery().getId(), "Successfully booked!");
        return Optional.empty();
    }
}
