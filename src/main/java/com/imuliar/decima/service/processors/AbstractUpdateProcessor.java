package com.imuliar.decima.service.processors;

import com.imuliar.decima.dao.BookingRepository;
import com.imuliar.decima.dao.ParkingUserRepository;
import com.imuliar.decima.dao.ReservationRepository;
import com.imuliar.decima.dao.SlotRepository;
import com.imuliar.decima.dao.VacantPeriodRepository;
import com.imuliar.decima.entity.ParkingUser;
import com.imuliar.decima.service.UpdateProcessor;
import com.imuliar.decima.service.impl.MessagePublisher;
import com.imuliar.decima.service.state.SessionState;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.regex.Pattern;
import javax.annotation.Nonnull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.Assert;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * <p>Abstract {@link UpdateProcessor}</p>
 *
 * @author imuliar
 * @since 0.0.1
 */
public abstract class AbstractUpdateProcessor implements UpdateProcessor {

    BiFunction<Update, String, Boolean> callbackMatching = (update, callback) -> update.hasCallbackQuery() && update.getCallbackQuery().getData().equals(callback);
    BiFunction<Update, Pattern, Boolean> regexpEvaluating = (update, matchingPattern) -> update.hasCallbackQuery() && matchingPattern.matcher(update.getCallbackQuery().getData()).matches();

    @Value("${decima.plan}")
    private String planImageUrl;

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

    @Override
    public Optional<SessionState> process(@Nonnull Update update, @Nonnull ParkingUser parkingUser) {
        Assert.notNull(update, "update is NULL");
        Assert.notNull(update, "parkingUser is NULL");
        Long chatId = resolveChatId(update);
        return doProcess(update, parkingUser, chatId);
    }

    private Long resolveChatId(Update update) {
        return update.hasMessage()
                ? update.getMessage().getChatId()
                : update.getCallbackQuery().getMessage().getChatId();
    }

    abstract Optional<SessionState> doProcess(Update update, ParkingUser parkingUser, Long chatId);

    public String getPlanImageUrl() {
        return planImageUrl;
    }

    public MessagePublisher getMessagePublisher() {
        return messagePublisher;
    }

    public ReservationRepository getReservationRepository() {
        return reservationRepository;
    }

    public VacantPeriodRepository getVacantPeriodRepository() {
        return vacantPeriodRepository;
    }

    public SlotRepository getSlotRepository() {
        return slotRepository;
    }

    public ParkingUserRepository getUserRepository() {
        return userRepository;
    }

    public BookingRepository getBookingRepository() {
        return bookingRepository;
    }
}
