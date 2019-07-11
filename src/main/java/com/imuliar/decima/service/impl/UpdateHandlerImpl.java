package com.imuliar.decima.service.impl;

import com.imuliar.decima.entity.ParkingUser;
import com.imuliar.decima.service.ParkingUserService;
import com.imuliar.decima.service.ResponseStrategyFactory;
import com.imuliar.decima.service.UpdateHandler;
import lombok.AllArgsConstructor;
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

    private ParkingUserService parkingUserService;

    private ResponseStrategyFactory responseStrategyFactory;

    private FakeDataLoader fakeDataLoader;

    @Autowired
    public UpdateHandlerImpl(ParkingUserService parkingUserService, ResponseStrategyFactory responseStrategyFactory, FakeDataLoader fakeDataLoader) {
        this.parkingUserService = parkingUserService;
        this.responseStrategyFactory = responseStrategyFactory;
        this.fakeDataLoader = fakeDataLoader;
    }

    @PostConstruct
    public void loadFakeData() {
        fakeDataLoader.loadDataFull();
    }

    @Override
    public void handle(Update update) {
        User telegramUser = resolveTelegramUser(update);
        if (!hasAccess(telegramUser)) {
            //print NO ACCESS MESSAGE
            return;
        }
        Long chatId = resolveChatId(update);
        ParkingUser parkingUser = parkingUserService.findOrCreateParkingUser(telegramUser);

        responseStrategyFactory
                .getStrategy(parkingUser)
                .response(chatId, parkingUser, update);

        System.out.println("TELEGRAM UPDATE RECEIVED!!!");
    }


    private boolean hasAccess(User telegramUser) {
        return true;
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
