package com.imuliar.decima.service.state;

import com.imuliar.decima.dao.PollingProfileRepository;
import com.imuliar.decima.entity.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

/**
 * <p>Initial state in session for processing updates received from slot owner</p>
 *
 * @author imuliar
 * @since 0.0.1
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class SlotOwnerInitialState extends AbstractState {

    private static final Pattern LEAVE_RESERVED_MATCHING_PATTERN = Pattern.compile("");
    private static final Pattern SET_FREE_MATCHING_PATTERN = Pattern.compile(""); //TODO provide pattern
    private static final Pattern BOOK_MATCHING_PATTERN = Pattern.compile(""); //TODO provide pattern

    private static final String RELEASE_MESSAGE_PATTERN = "Slot # %s has been released by user %s.";

    @Autowired
    private PollingProfileRepository pollingProfileRepository;

    @Override
    public void processUpdate(Long chatId, ParkingUser parkingUser, Update update) {
        if (update.hasCallbackQuery()) {
            String callbackString = update.getCallbackQuery().getData();
            if (SET_FREE_MATCHING_PATTERN.matcher(callbackString).matches()) {
                VacantPeriod vacantPeriod = parseSetFreeCallbackData(callbackString);
                getVacantPeriodRepository().save(vacantPeriod);

                PollingProfile pollingProfile = parkingUser.getPollingProfile();
                pollingProfile.setLastAnswerReceived(LocalDate.now());
                pollingProfileRepository.save(pollingProfile); // update
                //TODO updating with the save() need to be tested carefully


                Reservation currentUserReservation = getReservationRepository().findByTelegramUserId(parkingUser.getTelegramUserId())
                        .orElseThrow(() -> new IllegalStateException("Reservation should exist for user who has set his slot to be free."));
                Slot releasedSlot = currentUserReservation.getSlot();
                List<Reservation> allReservationsForSingleSlot = getReservationRepository().findBySlot(releasedSlot);
                allReservationsForSingleSlot.sort(Comparator.comparing(Reservation::getPriority));

                if(currentUserReservation.getPriority() == (allReservationsForSingleSlot.size() - 1)){
                    publishReleaseMessage(releasedSlot, parkingUser);
                    return;
                }

                for(Reservation res : allReservationsForSingleSlot.subList(currentUserReservation.getPriority() + 1, allReservationsForSingleSlot.size())){
                    ParkingUser parkingProposalCandidate = res.getUser();
                    boolean isCandidateAbsent = getVacantPeriodRepository().isUserAbsent(parkingProposalCandidate.getId(), LocalDate.now());
                    if(!isCandidateAbsent){
                        //notify candidate
                        publishReleaseMessage(releasedSlot, parkingProposalCandidate);
                        return;
                    }
                }
                publishReleaseMessage(releasedSlot, parkingUser);
            }
            if(BOOK_MATCHING_PATTERN.matcher(callbackString).matches()){
                List<String> splitStringData = Arrays.asList(callbackString.split("#"));
                Slot slot = getSlotRepository().findByNumber(splitStringData.get(0))
                        .orElseThrow(() -> new IllegalStateException("Booked slot should exist"));
                LocalDate bookingDate = LocalDate.parse(splitStringData.get(2), DateTimeFormatter.ISO_DATE);
                Booking booking = new Booking(parkingUser, slot, bookingDate);
                getBookingRepository().save(booking);
                //TODO notify about successful booking;
            }
        }
    }

    private void publishReleaseMessage(Slot releasedSlot, ParkingUser parkingUser) {
        String bookItCallbackData = String.format("book#%s#%s", releasedSlot.getNumber(), LocalDate.now().format(DateTimeFormatter.ISO_DATE));
        InlineKeyboardMarkup inlineMarkup = new InlineKeyboardMarkup();
        inlineMarkup.setKeyboard(Collections.singletonList(Collections.singletonList(new InlineKeyboardButton().setText("BOOK IT FOR ME!").setCallbackData(bookItCallbackData))));
        getMessagePublisher().sendMessageWithKeyboardToGroup(String.format(RELEASE_MESSAGE_PATTERN, releasedSlot.getNumber(), parkingUser.getTelegramUsername()) , inlineMarkup);
    }

    private VacantPeriod parseSetFreeCallbackData(String callbackString) {
        List<String> splitStringData = Arrays.asList(callbackString.split("#"));
        Integer telegramUserId = Integer.valueOf(splitStringData.get(1));
        LocalDate vacantDate = LocalDate.parse(splitStringData.get(2), DateTimeFormatter.ISO_DATE);
        ParkingUser user = getUserRepository().findByTelegramUserId(telegramUserId)
                .orElseThrow(() -> new IllegalStateException("Can't find user with telegram id: " + telegramUserId));
        return new VacantPeriod(vacantDate, vacantDate, user);
    }
}
