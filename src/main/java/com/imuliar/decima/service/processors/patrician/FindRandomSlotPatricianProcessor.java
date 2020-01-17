package com.imuliar.decima.service.processors.patrician;

import com.imuliar.decima.entity.Slot;
import com.imuliar.decima.service.processors.AbstractUpdateProcessor;
import com.vdurmont.emoji.EmojiParser;
import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

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
        if(freeSlots.isEmpty()){
            getMessagePublisher().sendMsgWithBackBtn(chatId, EmojiParser.parseToUnicode("Can't find any free slot :pensive:"));
        } else {
            String msg = String.format("Congrats! :party: \nYou've got a slot # %s. \nYou can park your car there today", freeSlots.get(0).getNumber());
            getMessagePublisher().sendMsgWithBackBtn(chatId, EmojiParser.parseToUnicode(msg));
        }
    }
}
