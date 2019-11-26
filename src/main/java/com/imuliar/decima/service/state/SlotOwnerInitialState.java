package com.imuliar.decima.service.state;

import com.imuliar.decima.dao.PollingProfileRepository;
import com.imuliar.decima.entity.*;
import com.imuliar.decima.service.UpdateProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static com.imuliar.decima.service.util.RegexPatterns.BOOK_MATCHING_PATTERN;
import static com.imuliar.decima.service.util.RegexPatterns.SET_FREE_MATCHING_PATTERN;

/**
 * <p>Initial state in session for processing updates received from slot owner</p>
 *
 * @author imuliar
 * @since 0.0.1
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class SlotOwnerInitialState extends AbstractState {

    @Autowired
    private List<UpdateProcessor> updateProcessors;

    @Override
    List<UpdateProcessor> getUpdateProcessors() {
        return updateProcessors;
    }
}
