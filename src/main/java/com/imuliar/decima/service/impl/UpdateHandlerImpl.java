package com.imuliar.decima.service.impl;

import com.imuliar.decima.entity.ParkingUser;
import com.imuliar.decima.entity.Role;
import com.imuliar.decima.service.ParkingUserService;
import com.imuliar.decima.service.ResponseStrategy;
import com.imuliar.decima.service.UpdateHandler;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.Optional;

/**
 * <p>Handle input updates from telegram server and forward for appropriate processing</p>
 *
 * @author imuliar
 * @since 0.0.1
 */
@Service
@AllArgsConstructor
public class UpdateHandlerImpl implements UpdateHandler {

    @Autowired
    private ParkingUserService parkingUserService;

    @Qualifier("plebeianResponseStrategy")
    private ResponseStrategy plebeianResponseStrategy;

    @Qualifier("patricianResponseStrategy")
    private ResponseStrategy patricianResponseStrategy;

    @Override
    public void handle(Update update) {
        User telegramUser = update.getMessage() != null
                ? update.getMessage().getFrom()
                : update.getCallbackQuery().getMessage().getFrom();
        Long chatId = update.getMessage() != null
                ? update.getMessage().getChatId()
                : update.getCallbackQuery().getMessage().getChatId();

        if (!hasAccess(telegramUser)) {
            //print NO ACCESS MESSAGE
            return;
        }

        ParkingUser parkingUser = parkingUserService.findOrCreateParkingUser(telegramUser);

        ResponseStrategy responseStrategy = parkingUser.getRole() == Role.PLEBEIAN
                ? plebeianResponseStrategy
                : patricianResponseStrategy;

        responseStrategy.response(chatId, parkingUser, update);


        System.out.println("TELEGRAM UPDATE RECEIVED!!!");
    }

    private boolean hasAccess(User telegramUser) {
        return true;
    }
}
