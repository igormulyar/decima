package com.imuliar;

import com.imuliar.decima.service.UpdateHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import javax.annotation.PostConstruct;

/**
 * <p>Main bot class</p>
 *
 * @author imuliar
 * @since 0.0.1
 */
@Component
public class DecimaBot extends TelegramLongPollingBot {

    private static final Logger LOGGER = LoggerFactory.getLogger(DecimaBot.class);

    @Value("${decima.botusername}")
    private String botUsername;

    @Value("${decima.bottoken}")
    private String botToken;

    private UpdateHandler updateHandler;

    static {
        LOGGER.info("DECIMA BOT LOADING");
    }

    @Autowired
    public DecimaBot(UpdateHandler updateHandler) {
        this.updateHandler = updateHandler;
    }

    @Override
    public void onUpdateReceived(Update update) {
        updateHandler.handle(update);
        LOGGER.info("UPDATE RECEIVED!");
        System.out.println(update.getMessage().getText());
        String command=update.getMessage().getText();
        System.out.println(command);

        SendMessage message = new SendMessage();
        message.setText("TEST, " + System.currentTimeMillis());
        message.setChatId(update.getMessage().getChatId());

        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }
}
