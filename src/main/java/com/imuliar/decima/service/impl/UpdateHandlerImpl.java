package com.imuliar.decima.service.impl;

import com.imuliar.decima.entity.ParkingUser;
import com.imuliar.decima.entity.Role;
import com.imuliar.decima.service.UpdateHandler;
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
public class UpdateHandlerImpl implements UpdateHandler {

    @Override
    public void handle(Update update) {

        User telegramUser = update.getMessage() != null
                ? update.getMessage().getFrom()
                : update.getCallbackQuery().getMessage().getFrom();

        Long chatId = update.getMessage() != null
                ? update.getMessage().getChatId()
                : update.getCallbackQuery().getMessage().getChatId();

        Optional<ParkingUser> resolvedParkingUser = resolveParkingUser(telegramUser);
        if(resolvedParkingUser.isPresent()){
            ParkingUser parkingUser = resolvedParkingUser.get();
            if(parkingUser.getRole() == Role.PLEBEIAN){
                //TODO call plebeian strategy
            }else {
                //TODO call patrician strategy
            }
        } else {
            //TODO respondAccessForbidden(chatId);
        }


        System.out.println("TELEGRAM UPDATE RECEIVED!!!");
    }

    private Optional<ParkingUser> resolveParkingUser(User telegramUser) {
        return Optional.empty();

    }
}
