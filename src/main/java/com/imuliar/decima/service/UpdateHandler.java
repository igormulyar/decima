package com.imuliar.decima.service;

import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * <p>Handle input updates from telegram server and forward for appropriate processing</p>
 *
 * @author imuliar
 * @since 0.0.1
 */
public interface UpdateHandler {

    void handle(Update update);
}
