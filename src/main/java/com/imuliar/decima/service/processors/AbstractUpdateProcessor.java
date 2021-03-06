package com.imuliar.decima.service.processors;

import com.imuliar.decima.dao.BookingRepository;
import com.imuliar.decima.dao.ReservationRepository;
import com.imuliar.decima.dao.SlotRepository;
import com.imuliar.decima.dao.VacantPeriodRepository;
import com.imuliar.decima.service.UpdateProcessor;
import com.imuliar.decima.service.impl.DecimaMessageSourceFacade;
import com.imuliar.decima.service.impl.MessagePublisher;
import com.imuliar.decima.service.session.SessionState;
import com.imuliar.decima.service.session.UserSession;
import com.imuliar.decima.service.util.StateFactory;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.Assert;
import org.telegram.telegrambots.meta.api.objects.Update;

import javax.annotation.Nonnull;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.regex.Pattern;

/**
 * <p>Abstract {@link UpdateProcessor}</p>
 *
 * @author imuliar
 * @since 0.0.1
 */
@Getter
@Setter
public abstract class AbstractUpdateProcessor implements UpdateProcessor {

    protected BiFunction<Update, String, Boolean> callbackMatching =
            (update, callback) -> update.hasCallbackQuery() && update.getCallbackQuery().getData().equals(callback);
    protected BiFunction<Update, Pattern, Boolean> regexpEvaluating =
            (update, matchingPattern) -> update.hasCallbackQuery() && matchingPattern.matcher(update.getCallbackQuery().getData()).matches();
    protected BiFunction<Update, Pattern, Boolean> regexpMsgEvaluating =
            (update, matchingPattern) -> update.hasMessage() && matchingPattern.matcher(update.getMessage().getText()).matches();

    protected UserSession session;

    @Value("${decima.plan}")
    private String planImageUrl;

    @Autowired
    private StateFactory stateFactory;

    @Autowired
    private MessagePublisher messagePublisher;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private VacantPeriodRepository vacantPeriodRepository;

    @Autowired
    private SlotRepository slotRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private DecimaMessageSourceFacade messageSourceFacade;

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<SessionState> process(@Nonnull Update update) {
        Assert.notNull(update, "update is NULL");
        Long chatId = resolveChatId(update);
        doProcess(update, chatId);
        return getNextState();
    }

    protected String getMsg(String code) {
        return messageSourceFacade.getMsg(code, session.getLangCode());
    }

    protected String getMsg(String code, String... params) {
        return messageSourceFacade.getMsg(code, session.getLangCode(), params);
    }

    /**
     * Transition to the next Session state if required
     *
     * @return appropriate state implementation OR empty if not required. Empty by default
     */
    protected Optional<SessionState> getNextState() {
        return Optional.empty();
    }

    private Long resolveChatId(Update update) {
        return update.hasMessage()
                ? update.getMessage().getChatId()
                : update.getCallbackQuery().getMessage().getChatId();
    }

    protected abstract void doProcess(Update update, Long chatId);
}
