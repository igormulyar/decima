package com.imuliar.decima.service;

/**
 * <p>Get appropriate strategy for the chat</p>
 *
 * @author imuliar
 * @since 0.0.1
 */
public interface ResponseStrategyFactory {

    ResponseStrategy getStrategy(Integer userId, Long chatId);
}
