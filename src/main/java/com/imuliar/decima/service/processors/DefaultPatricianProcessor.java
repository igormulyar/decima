package com.imuliar.decima.service.processors;

import com.imuliar.decima.entity.ParkingUser;
import com.imuliar.decima.service.state.AbstractState;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

/**
 * <p></p>
 *
 * @author imuliar
 * @since 0.0.1
 */
@Service
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class DefaultPatricianProcessor extends AbstractUpdateProcessor {

    @Override
    public boolean isMatch(Update update) {
        return true;
    }

    @Override
    Optional<AbstractState> doProcess(Update update, ParkingUser parkingUser, Long chatId) {
        String messageLable = "As a parking slot owner I want to...";
        String buttonLabel1 = "Share my parking slot for today";
        String buttonLabel2 = "Set slot availability for others for period";
        String buttonLabel3 = "Cancel slot sharing for today";
        String buttonLabel4 = "See slot list";
        String buttonLabel5 = "See parking plan";

        InlineKeyboardMarkup inlineMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        keyboard.add(Collections.singletonList(new InlineKeyboardButton().setText(buttonLabel1).setCallbackData("")));
        keyboard.add(Collections.singletonList(new InlineKeyboardButton().setText(buttonLabel2).setCallbackData("")));
        keyboard.add(Collections.singletonList(new InlineKeyboardButton().setText(buttonLabel3).setCallbackData("")));
        keyboard.add(Collections.singletonList(new InlineKeyboardButton().setText(buttonLabel4).setCallbackData("")));
        keyboard.add(Collections.singletonList(new InlineKeyboardButton().setText(buttonLabel5).setCallbackData("")));
        inlineMarkup.setKeyboard(keyboard);
        getMessagePublisher().sendMessageWithKeyboard(chatId, messageLable, inlineMarkup);
        return Optional.empty();
    }

}
