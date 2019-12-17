package com.imuliar.decima.service.processors;

import com.imuliar.decima.dao.PollingProfileRepository;
import com.imuliar.decima.entity.*;
import com.imuliar.decima.service.util.InlineKeyboardMarkupBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

import static com.imuliar.decima.service.util.Callbacks.*;

/**
 * <p>Process "set slot free" order for both types of users who has reservations</p>
 *
 * @author imuliar
 * @since 0.0.1
 */
@Service
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class SharePatricianSlotProcessor extends AbstractUpdateProcessor {

    private static final String RELEASE_MESSAGE_PATTERN = "BOT NOTIFICATION:\n[%s](tg://user?id=%d) has shared their slot for today.";

    @Autowired
    private PollingProfileRepository pollingProfileRepository;

    @Override
    public boolean isMatch(Update update) {
        return callbackMatching.apply(update, SET_FREE_TODAY);
    }

    @Override
    protected void doProcess(Update update, ParkingUser parkingUser, Long chatId) {
        if (getVacantPeriodRepository().hasIntersections(parkingUser.getTelegramUserId(), LocalDate.now(), LocalDate.now())) {
            getMessagePublisher().popUpNotify(update.getCallbackQuery().getId(), "Your slot is already shared today");
        } else {
            VacantPeriod vacantPeriod = new VacantPeriod(LocalDate.now(), LocalDate.now(), parkingUser);
            getVacantPeriodRepository().save(vacantPeriod);

            publishNotificationToCurrentUser(chatId);
            publishNotificationToGroupChat(parkingUser);
        }
        PollingProfile pollingProfile = parkingUser.getPollingProfile();
        pollingProfile.setLastAnswerReceived(LocalDate.now());
        pollingProfileRepository.save(pollingProfile); // update
    }

    private void publishNotificationToGroupChat(ParkingUser parkingUser) {
        getMessagePublisher()
                .sendSimpleMessageToGroup(String.format(RELEASE_MESSAGE_PATTERN, parkingUser.toString(), parkingUser.getTelegramUserId()));
    }

    private void publishNotificationToCurrentUser(Long chatId) {
        String message = "You've successfully shared your parking slot with other users.\n By the end of this day it can be engaged by any other user " +
                "and you woun't be able to cancel sharing.";
        getMessagePublisher().sendMessageWithKeyboard(chatId, message, new InlineKeyboardMarkupBuilder()
                .addButton(new InlineKeyboardButton("Back").setCallbackData(TO_BEGINNING)).build());
    }
}
