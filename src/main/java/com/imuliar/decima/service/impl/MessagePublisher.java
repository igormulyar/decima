package com.imuliar.decima.service.impl;

import com.imuliar.decima.DecimaBot;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

/**
 * <p>Facade for sending the messages</p>
 *
 * @author imuliar
 * @since 0.0.1
 */
@Service
public class MessagePublisher {

    private DecimaBot bot;

    @Value("${decima.groupChatId}")
    private String groupChatId;

    @Autowired
    @Lazy
    public MessagePublisher(DecimaBot decimaBot) {
        this.bot = decimaBot;
    }

    public void sendMessageWithKeyboard(Long chatId, String message, InlineKeyboardMarkup keyboardMarkup) {
        bot.sendBotResponse(new SendMessage()
                .enableMarkdown(true)
                .setChatId(chatId)
                .setText(message)
                .setReplyMarkup(keyboardMarkup));
    }

    public void sendMessageWithKeyboardToGroup(String message, InlineKeyboardMarkup keyboardMarkup){
        sendMessageWithKeyboard(Long.valueOf(groupChatId), message, keyboardMarkup);
    }

    public void popUpNotify(String callbackQueryId, String messageText) {
        bot.sendBotResponse(new AnswerCallbackQuery()
                .setCallbackQueryId(callbackQueryId)
                .setShowAlert(true)
                .setText(messageText));
    }

    public void sendImage(Long chatId, String caption, String imageUrl){
        bot.sendPhoto(new SendPhoto()
                .setChatId(chatId)
                .setCaption(caption)
                .setPhoto(imageUrl));
    }
}
