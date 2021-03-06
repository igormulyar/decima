package com.imuliar.decima.service.processors;

import com.imuliar.decima.service.util.InlineKeyboardMarkupBuilder;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import static com.imuliar.decima.service.util.Callbacks.SHOW_PLAN;
import static com.imuliar.decima.service.util.Callbacks.TO_BEGINNING;

/**
 * <p>Publish link to the plan of parking</p>
 *
 * @author imuliar
 * @since 0.0.1
 */
@Service
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ShowPlanProcessor extends AbstractUpdateProcessor {

    @Override
    public boolean isMatch(Update update) {
        return callbackMatching.apply(update, SHOW_PLAN);
    }

    @Override
    protected void doProcess(Update update, Long chatId) {
        getMessagePublisher().sendImageWithKeyboard(chatId,
                getMsg("msg.plan_caption"), getPlanImageUrl(),
                new InlineKeyboardMarkupBuilder()
                        .addButton(new InlineKeyboardButton(getMsg("btn.back")).setCallbackData(TO_BEGINNING))
                        .build());
    }
}
