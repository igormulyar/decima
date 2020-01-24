package com.imuliar.decima.service.processors.patrician;

import com.imuliar.decima.entity.Booking;
import com.imuliar.decima.entity.Slot;
import com.imuliar.decima.service.processors.AbstractUpdateProcessor;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

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
    protected void doProcess(Update update, Long chatId) {
        String callbackString = update.getCallbackQuery().getData();
        List<String> splitStringData = Arrays.asList(callbackString.split("#"));
        Slot slot = getSlotRepository().findByNumber(splitStringData.get(0))
                .orElseThrow(() -> new IllegalStateException("Booked slot should exist"));
        LocalDate bookingDate = LocalDate.parse(splitStringData.get(2), DateTimeFormatter.ISO_DATE);
        Booking booking = new Booking(chatId.intValue(), slot, bookingDate);
        getBookingRepository().save(booking);
        getMessagePublisher().popUpNotify(update.getCallbackQuery().getId(), getMsg("alert.pat_booked"));
    }
}
