package com.imuliar.decima.service.impl;

import com.imuliar.decima.dao.ReservationRepository;
import com.imuliar.decima.entity.Reservation;
import com.imuliar.decima.service.util.InlineKeyboardMarkupBuilder;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import static com.imuliar.decima.service.util.Callbacks.NO;
import static com.imuliar.decima.service.util.Callbacks.YES;

/**
 * <p>Process existing requests by schedule</p>
 *
 * @author imuliar
 * @since 0.0.1
 */
@Service
public class PollingService {

    @Autowired
    private Map<Integer, User> slotRequestBuffer;

    @Autowired
    private MessagePublisher messagePublisher;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private DecimaMessageSourceFacade messageSource;

    /**
     * <p>Process direct request to poll patricians</p>
     *
     * @param update telegram update dto
     */
    public void doThePoll(Update update) {
        User telegramUser = update.getCallbackQuery().getFrom();
        slotRequestBuffer.put(telegramUser.getId(), telegramUser);

        if (isWithinActiveTimeRange()) {
            runTask();
        }
    }

    /**
     * <p>Run scheduled for every 7 AM</p>
     * <p>Run poll if at least one plebeian user requested a slot this night</p>
     */
    @Scheduled(cron = "0 0 7 * * ?")
    void runBySchedule() {
        if (!slotRequestBuffer.isEmpty()) {
            runTask();
        }
    }

    /**
     * <p>Every midnight resets values due to the new day starting.</p>
     */
    @Scheduled(cron = "0 0 0 * * ?")
    void resetPostponedRequests() {
        slotRequestBuffer.clear();
    }

    private void runTask() {
        LocalDate today = LocalDate.now();
        for (Reservation res : reservationRepository.findUnpolled(today)) {
            messagePublisher.sendMessage(res.getUserId().longValue(), messageSource.getMsg("msg.poll_gonna_park", res.getLanguageCode(), today.toString()),
                    new InlineKeyboardMarkupBuilder()
                            .addButton(new InlineKeyboardButton(messageSource.getMsg("btn.yes_gonna_park", res.getLanguageCode())).setCallbackData(YES))
                            .addButtonAtNewRaw(new InlineKeyboardButton(messageSource.getMsg("btn.no_slot_free_today", res.getLanguageCode())).setCallbackData(NO))
                            .build());
        }
    }

    private boolean isWithinActiveTimeRange() {
        LocalTime now = LocalTime.now();
        LocalTime lowerBoundary = LocalTime.of(7, 0);
        LocalTime upperBoundary = LocalTime.of(21, 0);
        return now.isAfter(lowerBoundary) && now.isBefore(upperBoundary);
    }
}
