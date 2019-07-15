package com.imuliar.decima.service.state;

import com.imuliar.decima.entity.ParkingUser;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * <p>Initial state in session for processing updates received from slot owner</p>
 *
 * @author imuliar
 * @since 0.0.1
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class SlotOwnerInitialState extends AbstractState {

    @Override
    public void processUpdate(Long chatId, ParkingUser parkingUser, Update update) {
        if(update.hasCallbackQuery()){
            String callbackData = update.getCallbackQuery().getData();

        }

    }
}
