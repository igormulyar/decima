package com.imuliar.decima.service.impl;

import com.imuliar.decima.service.UpdateHandler;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

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
        System.out.println("TELEGRAM UPDATE RECEIVED!!!");
    }
}
