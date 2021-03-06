package com.imuliar.decima.service.processors.patrician;

import com.imuliar.decima.entity.Booking;
import com.imuliar.decima.entity.Reservation;
import com.imuliar.decima.entity.Slot;
import com.imuliar.decima.entity.VacantPeriod;
import com.imuliar.decima.service.impl.BookingRequestsSupplier;
import com.imuliar.decima.service.processors.AbstractUpdateProcessor;
import com.imuliar.decima.service.processors.SharePatricianSlotProcessor;
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

import static com.imuliar.decima.service.util.Callbacks.NO;
import static com.imuliar.decima.service.util.Callbacks.TO_BEGINNING;

/**
 * <p>Process "NO" poll answer</p>
 *
 * @author imuliar
 * @since 0.0.1
 */
@Service
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class NoPatricianProcessor extends SharePatricianSlotProcessor {

    @Override
    public boolean isMatch(Update update) {
        return callbackMatching.apply(update, NO);
    }

    @Override
    protected void publishNotificationToCurrentUser(Update update) {
        getMessagePublisher().reRenderMessage(update.getCallbackQuery().getMessage().getChatId(), update.getCallbackQuery().getMessage().getMessageId(),
                getMsg("msg.pat_press_no"), new InlineKeyboardMarkupBuilder()
                        .addButton(new InlineKeyboardButton(getMsg("btn.back")).setCallbackData(TO_BEGINNING)).build());
    }
}
