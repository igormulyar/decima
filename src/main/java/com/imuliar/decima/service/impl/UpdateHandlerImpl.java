package com.imuliar.decima.service.impl;

import com.imuliar.decima.service.AccessProvider;
import com.imuliar.decima.service.ResponseStrategyFactory;
import com.imuliar.decima.service.UpdateHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
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

    private ResponseStrategyFactory responseStrategyFactory;

    private FakeDataLoader fakeDataLoader;

    private AccessProvider accessProvider;

    private MessagePublisher messagePublisher;

    @Autowired
    public UpdateHandlerImpl(ResponseStrategyFactory responseStrategyFactory, FakeDataLoader fakeDataLoader, AccessProvider accessProvider,
                             MessagePublisher messagePublisher) {
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
            responseStrategyFactory
                    .getStrategy(telegramUser, chatId)
                    .response(update);
        } else {
            messagePublisher.sendMessage(chatId, "*!!!ACCESS DENIED!!!* \nPLEASE CONTACT ADMINISTRATOR.");
        }

        LOGGER.debug("TELEGRAM UPDATE PROCESSED");
    }

    private Long resolveChatId(Update update) {
        return update.hasMessage()
                ? update.getMessage().getChatId()
                : update.getCallbackQuery().getMessage().getChatId();
    }

    private User resolveTelegramUser(Update update) {
        return update.getMessage() != null
                ? update.getMessage().getFrom()
                : update.getCallbackQuery().getFrom();
    }
}
