package com.imuliar.decima.service;

import org.telegram.telegrambots.meta.api.objects.User;

/**
 * <p>Get appropriate strategy for the chat</p>
 *
 * @author imuliar
 * @since 0.0.1
 */
public interface ResponseStrategyFactory {

    ResponseStrategy getStrategy(User user, Long chatId);
}
