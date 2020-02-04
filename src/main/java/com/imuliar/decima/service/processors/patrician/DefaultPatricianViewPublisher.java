package com.imuliar.decima.service.processors.patrician;

import com.imuliar.decima.dao.BookingRepository;
import com.imuliar.decima.dao.VacantPeriodRepository;
import com.imuliar.decima.entity.Booking;
import com.imuliar.decima.entity.VacantPeriod;
import com.imuliar.decima.service.impl.DecimaMessageSourceFacade;
import com.imuliar.decima.service.impl.MessagePublisher;
import com.imuliar.decima.service.util.InlineKeyboardMarkupBuilder;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import static com.imuliar.decima.service.util.Callbacks.ASK_SLOT_FOR_USER_SEARCH;
import static com.imuliar.decima.service.util.Callbacks.CANCEL_MY_BOOKING;
import static com.imuliar.decima.service.util.Callbacks.LIST_VACANT_PERIODS;
import static com.imuliar.decima.service.util.Callbacks.SET_FREE_TODAY;
import static com.imuliar.decima.service.util.Callbacks.SET_SHARING_PERIOD;
import static com.imuliar.decima.service.util.Callbacks.SHOW_PLAN;
import static com.imuliar.decima.service.util.Callbacks.TO_BEGINNING;

/**
 * <p>Publish view message and buttons for default patrician state</p>
 *
 * @author imuliar
 * @since 0.0.1
 */
@Service
public class DefaultPatricianViewPublisher {

    private VacantPeriodRepository vacantPeriodRepository;

    private BookingRepository bookingRepository;

    private MessagePublisher messagePublisher;

    private DecimaMessageSourceFacade msgSource;

    @Autowired
    public DefaultPatricianViewPublisher(VacantPeriodRepository vacantPeriodRepository, BookingRepository bookingRepository, MessagePublisher messagePublisher, DecimaMessageSourceFacade msgSource) {
        this.vacantPeriodRepository = vacantPeriodRepository;
        this.bookingRepository = bookingRepository;
        this.messagePublisher = messagePublisher;
        this.msgSource = msgSource;
    }

    void publish(Long chatId, String langCode) {
        List<VacantPeriod> sharingPeriods = vacantPeriodRepository.findByUserIdAndDate(chatId.intValue(), LocalDate.now());

        String messageText = msgSource.getMsg("msg.pat_greeting", langCode);

        InlineKeyboardMarkupBuilder keyboardBuilder = new InlineKeyboardMarkupBuilder();
        if (CollectionUtils.isEmpty(sharingPeriods)) {
            keyboardBuilder.addButton(new InlineKeyboardButton().setText(msgSource.getMsg("btn.share_today", langCode)).setCallbackData(SET_FREE_TODAY));
            if (!vacantPeriodRepository.findNotExpired(chatId.intValue(), LocalDate.now()).isEmpty()) {
                keyboardBuilder.addButtonAtNewRaw(new InlineKeyboardButton().setText(msgSource.getMsg("btn.list_periods", langCode)).setCallbackData(LIST_VACANT_PERIODS));
            }
        } else {
            keyboardBuilder.addButton(new InlineKeyboardButton().setText(msgSource.getMsg("btn.list_periods", langCode)).setCallbackData(LIST_VACANT_PERIODS));
            Optional<Booking> possibleBooking = bookingRepository.findByUserIdAndDate(chatId.intValue(), LocalDate.now());
            if (possibleBooking.isPresent()) {
                messageText = msgSource.getMsg("msg.pat_has_booking_note", langCode, possibleBooking.get().getSlot().getNumber()) + "\n" + messageText;
                keyboardBuilder.addButtonAtNewRaw(new InlineKeyboardButton().setText(msgSource.getMsg("btn.drop_booking", langCode)).setCallbackData(CANCEL_MY_BOOKING));
            }
        }
        InlineKeyboardMarkup keyboard = keyboardBuilder
                .addButtonAtNewRaw(new InlineKeyboardButton().setText(msgSource.getMsg("btn.set_period", langCode)).setCallbackData(SET_SHARING_PERIOD))
                .addButtonAtNewRaw(new InlineKeyboardButton().setText(msgSource.getMsg("btn.find_holder", langCode)).setCallbackData(ASK_SLOT_FOR_USER_SEARCH))
                .addButtonAtNewRaw(new InlineKeyboardButton().setText(msgSource.getMsg("btn.show_plan", langCode)).setCallbackData(SHOW_PLAN))
                .addButtonAtNewRaw(new InlineKeyboardButton().setText(msgSource.getMsg("btn.back", langCode)).setCallbackData(TO_BEGINNING))
                .build();

        messagePublisher.sendMessageWithKeyboard(chatId, messageText, keyboard);
    }
}
