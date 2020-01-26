package com.imuliar.decima.service.processors.patrician;

import com.imuliar.decima.dao.VacantPeriodRepository;
import com.imuliar.decima.entity.VacantPeriod;
import com.imuliar.decima.service.impl.DecimaMessageSourceFacade;
import com.imuliar.decima.service.impl.MessagePublisher;
import com.imuliar.decima.service.util.InlineKeyboardMarkupBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.time.LocalDate;
import java.util.List;

import static com.imuliar.decima.service.util.Callbacks.*;

/**
 * <p>Publish view message and buttons for default patrician state</p>
 *
 * @author imuliar
 * @since 0.0.1
 */
@Service
public class DefaultPatricianViewPublisher {

    private VacantPeriodRepository vacantPeriodRepository;

    private MessagePublisher messagePublisher;

    private DecimaMessageSourceFacade msgSource;

    @Autowired
    public DefaultPatricianViewPublisher(VacantPeriodRepository vacantPeriodRepository, MessagePublisher messagePublisher, DecimaMessageSourceFacade msgSource) {
        this.vacantPeriodRepository = vacantPeriodRepository;
        this.messagePublisher = messagePublisher;
        this.msgSource = msgSource;
    }

    void publish(Long chatId, String langCode) {
        List<VacantPeriod> sharingPeriods = vacantPeriodRepository.findByUserIdAndDate(chatId.intValue(), LocalDate.now());

        InlineKeyboardMarkupBuilder keyboardBuilder = new InlineKeyboardMarkupBuilder();
        if (CollectionUtils.isEmpty(sharingPeriods)) {
            keyboardBuilder.addButton(new InlineKeyboardButton().setText(msgSource.getMsg("btn.share_today", langCode)).setCallbackData(SET_FREE_TODAY));
            if (!vacantPeriodRepository.findNotExpired(chatId.intValue(), LocalDate.now()).isEmpty()) {
                keyboardBuilder.addButtonAtNewRaw(new InlineKeyboardButton().setText(msgSource.getMsg("btn.list_periods", langCode)).setCallbackData(LIST_VACANT_PERIODS));
            }
        } else {
            keyboardBuilder.addButton(new InlineKeyboardButton().setText(msgSource.getMsg("btn.list_periods", langCode)).setCallbackData(LIST_VACANT_PERIODS));
        }
        InlineKeyboardMarkup keyboard = keyboardBuilder
                .addButtonAtNewRaw(new InlineKeyboardButton().setText(msgSource.getMsg("btn.set_period", langCode)).setCallbackData(SET_SHARING_PERIOD))
                .addButtonAtNewRaw(new InlineKeyboardButton().setText(msgSource.getMsg("btn.find_holder", langCode)).setCallbackData(ASK_SLOT_FOR_USER_SEARCH))
                .addButtonAtNewRaw(new InlineKeyboardButton().setText(msgSource.getMsg("btn.show_plan", langCode)).setCallbackData(SHOW_PLAN))
                .addButtonAtNewRaw(new InlineKeyboardButton().setText(msgSource.getMsg("btn.back", langCode)).setCallbackData(TO_BEGINNING))
                .build();

        messagePublisher.sendMessageWithKeyboard(chatId, msgSource.getMsg("msg.pat_greeting", langCode), keyboard);
    }
}
