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

    private static final String RELEASE_MESSAGE_PATTERN = "BOT NOTIFICATION:\nSlot # *%s* has been shared by [%s](tg://user?id=%d).";

    @Autowired
    private PollingProfileRepository pollingProfileRepository;

    @Override
    public boolean isMatch(Update update) {
        return callbackMatching.apply(update, SET_FREE_TODAY);
    }

    @Override
    protected void doProcess(Update update, ParkingUser parkingUser, Long chatId) {

        markAnswered(parkingUser);
        publishNotificationToCurrentUser(chatId);

        Reservation currentUserReservation = getReservationRepository().findByTelegramUserId(parkingUser.getTelegramUserId())
                .orElseThrow(() -> new IllegalStateException("Reservation should exist for user who has set his slot to be free."));
        Slot sharedSlot = currentUserReservation.getSlot();

        List<Reservation> otherReservations = getOtherReservations(currentUserReservation);
        if (CollectionUtils.isEmpty(otherReservations)) {
            publishNotificationToGroupChat(sharedSlot, parkingUser);
        } else {
            //ask other user
            for (Reservation candidateReservation : otherReservations) {
                boolean hasNotBookedYet = !getBookingRepository().findByUserAndDate(candidateReservation.getUser(), LocalDate.now()).isPresent();
                if (hasNotBookedYet) {
                    long candidateChatId = candidateReservation.getUser().getTelegramUserId().longValue();
                    getMessagePublisher().sendMessageWithKeyboard(candidateChatId, String.format("Slot # %s has been shared for today. Want to book it?", sharedSlot.getNumber()),
                            new InlineKeyboardMarkupBuilder()
                                    .addButton(new InlineKeyboardButton("Yes! Book it for me!").setCallbackData(String.format(BOOK_SLOT_CALLBACK_TPL, candidateReservation.getSlot().getNumber())))
                                    .addButtonAtNewRaw(new InlineKeyboardButton("No. Share with others").setCallbackData(SET_FREE_TODAY)).build());
                    return;
                }
            }
        }
    }

    private List<Reservation> getOtherReservations(Reservation currentUserReservation) {
        List<Reservation> allReservationsForSingleSlot = getReservationRepository().findBySlot(currentUserReservation.getSlot());
        allReservationsForSingleSlot.removeIf(r -> r.getPriority() <= currentUserReservation.getPriority());
        allReservationsForSingleSlot.sort(Comparator.comparing(Reservation::getPriority));
        return allReservationsForSingleSlot;
    }

    private void markAnswered(ParkingUser parkingUser) {
        PollingProfile pollingProfile = parkingUser.getPollingProfile();
        if (pollingProfile != null) {
            VacantPeriod vacantPeriod = new VacantPeriod(LocalDate.now(), LocalDate.now(), parkingUser);
            getVacantPeriodRepository().save(vacantPeriod);
            pollingProfile.setLastAnswerReceived(LocalDate.now());
            pollingProfileRepository.save(pollingProfile); // update
        }
    }

    private void publishNotificationToGroupChat(Slot sharedSlot, ParkingUser parkingUser) {
        getMessagePublisher()
                .sendSimpleMessageToGroup(String.format(RELEASE_MESSAGE_PATTERN, sharedSlot.getNumber(), parkingUser.toString(), parkingUser.getTelegramUserId()));
    }

    private void publishNotificationToCurrentUser(Long chatId) {
        String message = "You've successfully shared your parking slot with other users.\n By the end of this day it can be engaged by any other user " +
                "and you woun't be able to cancel sharing.";
        getMessagePublisher().sendMessageWithKeyboard(chatId, message, new InlineKeyboardMarkupBuilder()
                .addButton(new InlineKeyboardButton("Back").setCallbackData(TO_BEGINNING)).build());
    }
}
