package com.imuliar.decima.service.processors;

import com.imuliar.decima.entity.Booking;
import com.imuliar.decima.entity.Slot;
import com.imuliar.decima.service.impl.BookingRequestsSupplier;
import com.imuliar.decima.service.util.InlineKeyboardMarkupBuilder;
import java.time.LocalDate;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import static com.imuliar.decima.service.util.Callbacks.CANCEL_MY_BOOKING;
import static com.imuliar.decima.service.util.Callbacks.TO_BEGINNING;

/**
 * <p>Process "cancel booking" action</p>
 *
 * @author imuliar
 * @since 0.0.1
 */
@Service
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CancelBookingProcessor extends AbstractUpdateProcessor {

    @Autowired
    private BookingRequestsSupplier bookingRequestsSupplier;

    @Override
    public boolean isMatch(Update update) {
        return callbackMatching.apply(update, CANCEL_MY_BOOKING);
    }

    @Override
    protected void doProcess(Update update, Long chatId) {
        Integer userId = chatId.intValue();
        LocalDate today = LocalDate.now();
        Optional<Booking> bookingToDrop = getBookingRepository().findByUserIdAndDate(userId, today);

        if (bookingToDrop.isPresent()) {
            Slot slot = bookingToDrop.get().getSlot();
            getBookingRepository().removeByUserIdAndDate(userId, today);
            getMessagePublisher().sendMessageWithKeyboard(chatId, getMsg("msg.drop_booking_confirmed"),
                    new InlineKeyboardMarkupBuilder()
                            .addButton(new InlineKeyboardButton(getMsg("btn.back")).setCallbackData(TO_BEGINNING)).build());

            Optional<User> possibleRequester = bookingRequestsSupplier.pullOutRandomRequester();
            if(possibleRequester.isPresent()){
                User requester = possibleRequester.get();
                Booking booking = new Booking(requester.getId(), slot, today);
                getBookingRepository().save(booking);
                String msgForRequester = getMessageSourceFacade().getMsg("msg.pleb_shared_for_you", requester.getLanguageCode(), userId.toString(), slot.getNumber());
                getMessagePublisher().sendMsgWithBackBtn(requester.getId().longValue(), msgForRequester);
            }
        }
    }
}
