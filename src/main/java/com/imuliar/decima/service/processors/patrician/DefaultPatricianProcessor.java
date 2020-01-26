package com.imuliar.decima.service.processors.patrician;

import com.imuliar.decima.service.processors.AbstractUpdateProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

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
        defaultPatricianViewPublisher.publish(chatId, session.getLangCode());
    }
}
