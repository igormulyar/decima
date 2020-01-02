package com.imuliar.decima.service.processors.patrician;

import com.imuliar.decima.service.processors.AbstractUpdateProcessor;
import com.imuliar.decima.service.session.SessionState;
import java.util.Optional;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

import static com.imuliar.decima.service.util.RegexPatterns.PERIOD_ID_PATTERN;

/**
 * <p>View action buttons for managing chosen vacant period</p>
 *
 * @author imuliar
 * @since 0.0.1
 */
@Service
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ManipulatePeriodPatricianProcessor extends AbstractUpdateProcessor {

    @Override
    public boolean isMatch(Update update) {
        return regexpEvaluating.apply(update, PERIOD_ID_PATTERN);
    }

    @Override
    protected void doProcess(Update update, Long chatId) {

    }

    @Override
    protected Optional<SessionState> getNextState() {
        //TODO
        return super.getNextState();
    }
}
