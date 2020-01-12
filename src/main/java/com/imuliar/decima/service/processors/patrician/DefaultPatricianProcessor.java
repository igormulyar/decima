package com.imuliar.decima.service.processors.patrician;

import com.imuliar.decima.entity.VacantPeriod;
import com.imuliar.decima.service.processors.AbstractUpdateProcessor;
import com.imuliar.decima.service.util.InlineKeyboardMarkupBuilder;
import com.vdurmont.emoji.EmojiParser;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import static com.imuliar.decima.service.util.Callbacks.ASK_SLOT_FOR_USER_SEARCH;
import static com.imuliar.decima.service.util.Callbacks.LIST_VACANT_PERIODS;
import static com.imuliar.decima.service.util.Callbacks.SET_FREE_TODAY;
import static com.imuliar.decima.service.util.Callbacks.SET_SHARING_PERIOD;
import static com.imuliar.decima.service.util.Callbacks.SHOW_PLAN;
import static com.imuliar.decima.service.util.Callbacks.TO_BEGINNING;

/**
 * <p></p>
 *
 * @author imuliar
 * @since 0.0.1
 */
@Service
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class DefaultPatricianProcessor extends AbstractUpdateProcessor {

    @Autowired
    private DefaultPatricianViewPublisher defaultPatricianViewPublisher;

    @Override
    public boolean isMatch(Update update) {
        return true;
    }

    @Override
    protected void doProcess(Update update, Long chatId) {
        defaultPatricianViewPublisher.publish(chatId);
    }
}
