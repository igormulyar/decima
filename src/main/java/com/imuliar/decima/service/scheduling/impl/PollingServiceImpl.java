package com.imuliar.decima.service.scheduling.impl;

import com.imuliar.decima.dao.ParkingUserRepository;
import com.imuliar.decima.entity.ParkingUser;
import com.imuliar.decima.service.impl.MessagePublisher;
import com.imuliar.decima.service.scheduling.PollingService;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import com.imuliar.decima.service.util.Callbacks;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

/**
 * <p>Creates scheduled task for polling user</p>
 *
 * @author imuliar
 * @since 0.0.1
 */
@Service
public class PollingServiceImpl implements PollingService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PollingServiceImpl.class);

    private static final String POLLING_MSG = "Are you going to park your car today?";

    private static final String YES_CALLBACK = "yes_callback";

    private final ParkingUserRepository userRepository;

    private final MessagePublisher messagePublisher;

    @Autowired
    public PollingServiceImpl(ParkingUserRepository userRepository, MessagePublisher messagePublisher) {
        this.userRepository = userRepository;
        this.messagePublisher = messagePublisher;
    }

    //@Scheduled(cron = "2 0 6,7,8,9,10,11,12,13 ? * MON-FRI *") //prod schedule
    //@Scheduled(cron = "0 * * ? * *") //test schedule
    @Override
    public void runPoll() {
        LOGGER.debug("START POLLING TASK.");
        userRepository
                .findByStartPollingHour(LocalTime.now().getHour(), LocalDate.now())
                .forEach(this::poll);
        LOGGER.debug("FINISH POLLING TASK.");
    }

    @Override
    public void poll(ParkingUser user) {
        List<InlineKeyboardButton> buttonsLine = new ArrayList<>();
        buttonsLine.add(new InlineKeyboardButton().setText("Yes!").setCallbackData(Callbacks.YES));
        buttonsLine.add(new InlineKeyboardButton().setText("No, my place is free today").setCallbackData(buildNoCallbackData(user.getTelegramUserId())));
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        keyboard.add(buttonsLine);
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        markupInline.setKeyboard(keyboard);
        messagePublisher.sendMessageWithKeyboard(Long.valueOf(user.getTelegramUserId()), POLLING_MSG, markupInline);
    }

    private String buildNoCallbackData(Integer userTelegramId) {
        return String.format("setfree#%d#%s", userTelegramId, LocalDate.now().format(DateTimeFormatter.ISO_DATE));
    }
}
