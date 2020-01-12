package com.imuliar.decima.service.processors.patrician;

import com.imuliar.decima.dao.VacantPeriodRepository;
import com.imuliar.decima.entity.VacantPeriod;
import com.imuliar.decima.service.impl.MessagePublisher;
import com.imuliar.decima.service.util.InlineKeyboardMarkupBuilder;
import com.vdurmont.emoji.EmojiParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.time.LocalDate;
import java.util.List;

import static com.imuliar.decima.service.util.Callbacks.*;
import static com.imuliar.decima.service.util.Callbacks.SHOW_PLAN;
import static com.imuliar.decima.service.util.Callbacks.TO_BEGINNING;

/**
 * <p>Publish view message and buttons for default patrician state</p>
 *
 * @author imuliar
 * @since 0.0.1
 */
@Service
public class DefaultPatricianViewPublisher {

    private static final String MESSAGE_TEXT = EmojiParser.parseToUnicode(":sunglasses::sunglasses::sunglasses: \n*As a parking slot owner I want to...*\n");

    private VacantPeriodRepository vacantPeriodRepository;

    private MessagePublisher messagePublisher;

    @Autowired
    public DefaultPatricianViewPublisher(VacantPeriodRepository vacantPeriodRepository, MessagePublisher messagePublisher) {
        this.vacantPeriodRepository = vacantPeriodRepository;
        this.messagePublisher = messagePublisher;
    }

    void publish(Long chatId){
        List<VacantPeriod> sharingPeriods = vacantPeriodRepository.findByUserIdAndDate(chatId.intValue(), LocalDate.now());

        InlineKeyboardMarkupBuilder keyboardBuilder = new InlineKeyboardMarkupBuilder();
        if (CollectionUtils.isEmpty(sharingPeriods)) {
            keyboardBuilder.addButton(new InlineKeyboardButton().setText("Share my slot today").setCallbackData(SET_FREE_TODAY));
            if (!vacantPeriodRepository.findNotExpired(chatId.intValue(), LocalDate.now()).isEmpty()) {
                keyboardBuilder.addButtonAtNewRaw(new InlineKeyboardButton().setText("List my sharing periods").setCallbackData(LIST_VACANT_PERIODS));
            }
        } else {
            keyboardBuilder.addButton(new InlineKeyboardButton().setText("List my sharing periods").setCallbackData(LIST_VACANT_PERIODS));
        }
        InlineKeyboardMarkup keyboard = keyboardBuilder
                .addButtonAtNewRaw(new InlineKeyboardButton().setText("Set slot sharing period").setCallbackData(SET_SHARING_PERIOD))
                .addButtonAtNewRaw(new InlineKeyboardButton().setText("Find current slot holder").setCallbackData(ASK_SLOT_FOR_USER_SEARCH))
                .addButtonAtNewRaw(new InlineKeyboardButton().setText("See parking plan").setCallbackData(SHOW_PLAN))
                .addButtonAtNewRaw(new InlineKeyboardButton().setText("Back").setCallbackData(TO_BEGINNING))
                .build();

        messagePublisher.sendMessageWithKeyboard(chatId, MESSAGE_TEXT, keyboard);
    }
}
