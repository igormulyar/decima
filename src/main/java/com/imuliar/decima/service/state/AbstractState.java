package com.imuliar.decima.service.state;

import com.imuliar.decima.dao.*;
import com.imuliar.decima.entity.ParkingUser;
import com.imuliar.decima.service.impl.MessagePublisher;
import com.imuliar.decima.service.session.UserSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * <p>Encapsulates common properties and behavior for child states</p>
 *
 * @author imuliar
 * @since 0.0.1
 */
public abstract class AbstractState {

    protected static final String TO_BEGINNING_CALLBACK = "go_to_beginning";

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

    UserSession userSession;

    public abstract void processUpdate(Long chatId, ParkingUser parkingUser, Update update);

    public MessagePublisher getMessagePublisher() {
        return messagePublisher;
    }

    public ReservationRepository getReservationRepository() {
        return reservationRepository;
    }

    public UserSession getUserSession() {
        return userSession;
    }

    public void setUserSession(UserSession userSession) {
        this.userSession = userSession;
    }

    public String getPlanImageUrl() {
        return planImageUrl;
    }

    public void setPlanImageUrl(String planImageUrl) {
        this.planImageUrl = planImageUrl;
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
