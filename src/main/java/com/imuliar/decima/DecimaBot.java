package com.imuliar.decima;

import com.imuliar.decima.service.UpdateHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.Serializable;

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
    }

    public <T extends Serializable, M extends BotApiMethod<T>> void sendBotResponse(M method) {
        try {
            execute(method);
        } catch (Exception e) {
            LOGGER.error("Bot execute method exception!", e);
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
