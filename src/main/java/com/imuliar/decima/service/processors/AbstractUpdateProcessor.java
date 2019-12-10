package com.imuliar.decima.service.processors;

import com.imuliar.decima.dao.*;
import com.imuliar.decima.entity.ParkingUser;
import com.imuliar.decima.service.UpdateProcessor;
import com.imuliar.decima.service.impl.MessagePublisher;
import com.imuliar.decima.service.session.UserSession;
import com.imuliar.decima.service.state.SessionState;
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

    BiFunction<Update, String, Boolean> callbackMatching = (update, callback) -> update.hasCallbackQuery() && update.getCallbackQuery().getData().equals(callback);
    BiFunction<Update, Pattern, Boolean> regexpEvaluating = (update, matchingPattern) -> update.hasCallbackQuery() && matchingPattern.matcher(update.getCallbackQuery().getData()).matches();
    BiFunction<Update, Pattern, Boolean> regexpMsgEvaluating = (update, matchingPattern) -> update.hasMessage() && matchingPattern.matcher(update.getMessage().getText()).matches();

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
    private ParkingUserRepository userRepository;

    @Autowired
    private BookingRepository bookingRepository;

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<SessionState> process(@Nonnull Update update, @Nonnull ParkingUser parkingUser) {
        Assert.notNull(update, "update is NULL");
        Assert.notNull(update, "parkingUser is NULL");
        Long chatId = resolveChatId(update);
        doProcess(update, parkingUser, chatId);
        return getNextState();
    }

    /**
     * Transition to the next Session state if required
     *
     * @return appropriate state implementation OR empty if not required. Empty by default
     */
    Optional<SessionState> getNextState() {
        return Optional.empty();
    }

    private Long resolveChatId(Update update) {
        return update.hasMessage()
                ? update.getMessage().getChatId()
                : update.getCallbackQuery().getMessage().getChatId();
    }

    abstract void doProcess(Update update, ParkingUser parkingUser, Long chatId);
}
