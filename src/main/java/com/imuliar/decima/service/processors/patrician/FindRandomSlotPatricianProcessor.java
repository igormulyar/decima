package com.imuliar.decima.service.processors.patrician;

import com.imuliar.decima.entity.Slot;
import com.imuliar.decima.service.processors.AbstractUpdateProcessor;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.time.LocalDate;
import java.util.List;

import static com.imuliar.decima.service.util.Callbacks.FIND_FREE_SLOT;

/**
 * <p>Find a slot for patrician who's slot was booked after the sharing</p>
 *
 * @author imuliar
 * @since 0.0.1
 */
@Service
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class FindRandomSlotPatricianProcessor extends AbstractUpdateProcessor {

    @Override
    public boolean isMatch(Update update) {
        return callbackMatching.apply(update, FIND_FREE_SLOT);
    }

    @Override
    protected void doProcess(Update update, Long chatId) {
        List<Slot> freeSlots = getSlotRepository().findFreeSlots(LocalDate.now());
        if (freeSlots.isEmpty()) {
            getMessagePublisher().sendMsgWithBackBtn(chatId, getMsg("msg.cant_find_free"));
        } else {
            getMessagePublisher().sendMsgWithBackBtn(chatId, getMsg("msg.book_success", freeSlots.get(0).getNumber()));
        }
    }
}
