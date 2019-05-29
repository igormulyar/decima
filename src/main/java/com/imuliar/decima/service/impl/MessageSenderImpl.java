package com.imuliar.decima.service.impl;

import com.imuliar.decima.DecimaBot;
import com.imuliar.decima.service.MessageSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

/**
 * //TODO add description <p></p>
 *
 * @author imuliar
 * @since //TODO specify version
 */
@Service
public class MessageSenderImpl implements MessageSender {

    private DecimaBot bot;

    @Autowired
    public MessageSenderImpl(DecimaBot decimaBot) {
        this.bot = decimaBot;
    }

    @Override
    public void sendMessageWithKeyboard(Long chatId, String message, InlineKeyboardMarkup keyboardMarkup) {
        bot.sendBotResponse(new SendMessage()
                .enableMarkdown(true)
                .setChatId(chatId)
                .setText(message)
                .setReplyMarkup(keyboardMarkup));
    }

    @Override
    public void popUpNotify(String callbackQueryId, String messageText) {
        bot.sendBotResponse(new AnswerCallbackQuery()
                .setCallbackQueryId(callbackQueryId)
                .setShowAlert(true)
                .setText(messageText));
    }
}
