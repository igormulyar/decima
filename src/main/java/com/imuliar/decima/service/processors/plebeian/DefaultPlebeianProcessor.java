package com.imuliar.decima.service.processors.plebeian;

import com.imuliar.decima.service.processors.AbstractUpdateProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * <p>Default update processor for ordinary user</p>
 *
 * @author imuliar
 * @since 0.1.1
 */
@Service
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class DefaultPlebeianProcessor extends AbstractUpdateProcessor {

    @Autowired
    private DefaultPlebeianViewPublisher defaultPlebeianViewPublisher;

    @Override
    public boolean isMatch(Update update) {
        return true;
    }

    @Override
    protected void doProcess(Update update, Long chatId) {
        defaultPlebeianViewPublisher.publish(chatId, getSession().getLangCode());
    }
}
