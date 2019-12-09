package com.imuliar.decima.service.impl;

import com.imuliar.decima.entity.ParkingUser;
import com.imuliar.decima.service.AccessProvider;
import com.imuliar.decima.service.ParkingUserService;
import com.imuliar.decima.service.ResponseStrategyFactory;
import com.imuliar.decima.service.UpdateHandler;
import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

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

    private AccessProvider accessProvider;

    private MessagePublisher messagePublisher;

    @Autowired
    public UpdateHandlerImpl(ParkingUserService parkingUserService, ResponseStrategyFactory responseStrategyFactory,
                             FakeDataLoader fakeDataLoader, AccessProvider accessProvider, MessagePublisher messagePublisher) {
        this.parkingUserService = parkingUserService;
        this.responseStrategyFactory = responseStrategyFactory;
        this.fakeDataLoader = fakeDataLoader;
        this.accessProvider = accessProvider;
        this.messagePublisher = messagePublisher;
    }

    @PostConstruct
    void loadFakeData() {
        fakeDataLoader.loadData();
    }

    @Override
    public void handle(Update update) {
        Assert.notNull(update, "update is NULL");

        User telegramUser = resolveTelegramUser(update);
        Long chatId = resolveChatId(update);

        if (accessProvider.isPermitted(telegramUser)) {
            ParkingUser parkingUser = parkingUserService.findOrCreateParkingUser(telegramUser);
            responseStrategyFactory
                    .getStrategy(parkingUser, chatId)
                    .response(chatId, parkingUser, update);
        } else {
            messagePublisher.sendSimpleMessage(chatId, "ACCESS DENIED");
        }

        LOGGER.info("TELEGRAM UPDATE PROCESSED");
    }

    private Long resolveChatId(Update update) {
        return update.hasMessage()
                ? update.getMessage().getChatId()
                : update.getCallbackQuery().getMessage().getChatId();
    }

    private User resolveTelegramUser(Update update) {
        return update.getMessage() != null
                ? update.getMessage().getFrom()
                : update.getCallbackQuery().getMessage().getFrom();
    }
}
