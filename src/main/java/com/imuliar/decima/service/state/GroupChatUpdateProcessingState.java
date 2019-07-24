package com.imuliar.decima.service.state;

import com.imuliar.decima.entity.Booking;
import com.imuliar.decima.entity.ParkingUser;
import java.util.regex.Pattern;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * <p>Single state for processing updates from the group chat</p>
 *
 * @author imuliar
 * @since 0.0.1
 */
@Component
public class GroupChatUpdateProcessingState extends AbstractState {

    private static final Pattern BOOK_SLOT_MATCHING_PATTERN = Pattern.compile(""); //TODO provide pattern

    @Override
    public void processUpdate(Long chatId, ParkingUser parkingUser, Update update) {
        //TODO implement processing

        //parse callback
        if (update.hasCallbackQuery()) {
            String callbackString = update.getCallbackQuery().getData();
            if (BOOK_SLOT_MATCHING_PATTERN.matcher(callbackString).matches()) {
                Booking booking = parseBookingActionCallbackData(callbackString);
            }
        }

    }

    private Booking parseBookingActionCallbackData(String callbackString) {

        return null;
    }
}
