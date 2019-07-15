package com.imuliar.decima.service.impl;

import com.imuliar.decima.entity.ParkingUser;
import com.imuliar.decima.service.ParkingUserService;
import com.imuliar.decima.service.ResponseStrategyFactory;
import com.imuliar.decima.service.UpdateHandler;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

import javax.annotation.PostConstruct;

/**
 * <p>Handle input updates from telegram server and forward for appropriate processing</p>
 *
 * @author imuliar
 * @since 0.0.1
 */
@Service
public class UpdateHandlerImpl implements UpdateHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(UpdateHandlerImpl.class);

    private ParkingUserService parkingUserService;

    private ResponseStrategyFactory responseStrategyFactory;

    private FakeDataLoader fakeDataLoader;

    @Autowired
    public UpdateHandlerImpl(ParkingUserService parkingUserService, ResponseStrategyFactory responseStrategyFactory, FakeDataLoader fakeDataLoader) {
        this.parkingUserService = parkingUserService;
        this.responseStrategyFactory = responseStrategyFactory;
        this.fakeDataLoader = fakeDataLoader;
    }

    @Override
    public void handle(Update update) {
        LOGGER.info("TELEGRAM UPDATE RECEIVED.");
        User telegramUser = resolveTelegramUser(update);
        Long chatId = resolveChatId(update);
        ParkingUser parkingUser = parkingUserService.findOrCreateParkingUser(telegramUser);

        responseStrategyFactory
                .getStrategy(parkingUser, chatId)
                .response(chatId, parkingUser, update);

        LOGGER.info("TELEGRAM UPDATE PROCESSED");
    }

    private Long resolveChatId(Update update) {
        return update.getMessage() != null
                ? update.getMessage().getChatId()
                : update.getCallbackQuery().getMessage().getChatId();
    }

    private User resolveTelegramUser(Update update) {
        return update.getMessage() != null
                ? update.getMessage().getFrom()
                : update.getCallbackQuery().getMessage().getFrom();
    }
}
