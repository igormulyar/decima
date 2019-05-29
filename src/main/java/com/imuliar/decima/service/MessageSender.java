package com.imuliar.decima.service;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

/**
 * //TODO add description <p></p>
 *
 * @author imuliar
 * @since //TODO specify version
 */
public interface MessageSender {

    void sendMessageWithKeyboard(Long chatId, String message, InlineKeyboardMarkup keyboardMarkup);

    void popUpNotify(String callbackQueryId, String messageText);
}
