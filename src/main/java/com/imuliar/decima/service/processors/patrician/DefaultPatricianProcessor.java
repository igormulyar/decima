package com.imuliar.decima.service.processors.patrician;

import com.imuliar.decima.entity.VacantPeriod;
import com.imuliar.decima.service.processors.AbstractUpdateProcessor;
import com.imuliar.decima.service.util.InlineKeyboardMarkupBuilder;
import com.vdurmont.emoji.EmojiParser;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.time.LocalDate;
import java.util.List;

import static com.imuliar.decima.service.util.Callbacks.*;

/**
 * <p></p>
 *
 * @author imuliar
 * @since 0.0.1
 */
@Service
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class DefaultPatricianProcessor extends AbstractUpdateProcessor {

    private static final String MESSAGE_TEXT = EmojiParser.parseToUnicode(":sunglasses::sunglasses::sunglasses: \n*As a parking slot owner I want to...*\n");

    @Override
    public boolean isMatch(Update update) {
        return true;
    }

    @Override
    protected void doProcess(Update update, Long chatId) {
        List<VacantPeriod> sharingPeriods = getVacantPeriodRepository().findByUserIdAndDate(chatId.intValue(), LocalDate.now());

        InlineKeyboardMarkupBuilder keyboardBuilder = new InlineKeyboardMarkupBuilder();
        if (CollectionUtils.isEmpty(sharingPeriods)) {
            keyboardBuilder
                    .addButton(new InlineKeyboardButton().setText("Share my slot today").setCallbackData(SET_FREE_TODAY))
                    .addButtonAtNewRaw(new InlineKeyboardButton().setText("Set slot sharing period").setCallbackData(SET_SHARING_PERIOD))
                    .addButtonAtNewRaw(new InlineKeyboardButton().setText("List my sharing periods").setCallbackData(SHOW_VACANT_PERIODS));

        } else {
            keyboardBuilder
                    .addButton(new InlineKeyboardButton().setText("List my sharing periods").setCallbackData(SHOW_VACANT_PERIODS));
        }
        InlineKeyboardMarkup keyboard = keyboardBuilder
                .addButtonAtNewRaw(new InlineKeyboardButton().setText("Find current slot holder").setCallbackData(ASK_SLOT_FOR_USER_SEARCH))
                .addButtonAtNewRaw(new InlineKeyboardButton().setText("See parking plan").setCallbackData(SHOW_PLAN))
                .addButtonAtNewRaw(new InlineKeyboardButton().setText("Back").setCallbackData(TO_BEGINNING))
                .build();

        getMessagePublisher().sendMessageWithKeyboard(chatId, MESSAGE_TEXT, keyboard);
    }
}
