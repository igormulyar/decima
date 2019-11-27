package com.imuliar.decima.service.processors;

import com.google.common.collect.Lists;
import com.imuliar.decima.entity.ParkingUser;
import com.imuliar.decima.entity.Slot;
import com.imuliar.decima.service.state.SessionState;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import static com.imuliar.decima.service.util.Callbacks.BOOK_SLOT_CALLBACK_TPL;
import static com.imuliar.decima.service.util.Callbacks.FIND_FREE_SLOT;

/**
 * <p>Show free slots to ordinary user</p>
 *
 * @author imuliar
 * @since 0.0.1
 */
@Service
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class FindRandomSlotPlebeianProcessor extends AbstractUpdateProcessor {

    @Override
    public boolean isMatch(Update update) {
        return callbackMatching.apply(update, FIND_FREE_SLOT);
    }

    @Override
    Optional<SessionState> doProcess(Update update, ParkingUser parkingUser, Long chatId) {
        List<Slot> freeSlots = getSlotRepository().findFreeSlots(LocalDate.now());
        if (CollectionUtils.isEmpty(freeSlots)) {
            publishNotFoundPopupMessage(update);
        } else {
            publishSlotList(chatId, freeSlots);
        }
        return Optional.empty();
    }

    private void publishNotFoundPopupMessage(Update update) {
        getMessagePublisher().popUpNotify(update.getCallbackQuery().getId(), "Can't find free slot for parking :(");
    }

    private void publishSlotList(Long chatId, List<Slot> freeSlots) {
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<InlineKeyboardButton> buttons = freeSlots.stream()
                .map(slot -> new InlineKeyboardButton().setText(slot.getNumber()).setCallbackData(String.format(BOOK_SLOT_CALLBACK_TPL, slot.getNumber())))
                .collect(Collectors.toList());
        List<List<InlineKeyboardButton>> keyboard = Lists.partition(buttons, 6);
        markupInline.setKeyboard(keyboard);
        getMessagePublisher().sendImage(chatId, null, getPlanImageUrl());
        getMessagePublisher().sendMessageWithKeyboard(chatId, "There are some parking slots available. Select one you'd like to book for today.", markupInline);
    }
}
