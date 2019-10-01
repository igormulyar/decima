package com.imuliar.decima.service.state;

import com.imuliar.decima.dao.PollingProfileRepository;
import com.imuliar.decima.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static com.imuliar.decima.service.util.RegexPatterns.BOOK_MATCHING_PATTERN;
import static com.imuliar.decima.service.util.RegexPatterns.SET_FREE_MATCHING_PATTERN;

/**
 * <p>Initial state in session for processing updates received from slot owner</p>
 *
 * @author imuliar
 * @since 0.0.1
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class SlotOwnerInitialState extends AbstractState {

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
                return;
            }
            if(BOOK_MATCHING_PATTERN.matcher(callbackString).matches()){
                List<String> splitStringData = Arrays.asList(callbackString.split("#"));
                Slot slot = getSlotRepository().findByNumber(splitStringData.get(0))
                        .orElseThrow(() -> new IllegalStateException("Booked slot should exist"));
                LocalDate bookingDate = LocalDate.parse(splitStringData.get(2), DateTimeFormatter.ISO_DATE);
                Booking booking = new Booking(parkingUser, slot, bookingDate);
                getBookingRepository().save(booking);
                getMessagePublisher().popUpNotify(update.getCallbackQuery().getId(), "Successfully booked!");
                return;
            }
            printInitialMessage(chatId, parkingUser, update);
        }
    }

    private void printInitialMessage(Long chatId, ParkingUser parkingUser, Update update) {
        String messageLable = "As a parking slot owner I want to...";
        String buttonLabel1 = "Share my parking slot for today";
        String buttonLabel2 = "Set slot availability for others for period";
        String buttonLabel3 = "Cancel slot sharing for today";
        String buttonLabel4 = "See slot list";
        String buttonLabel5 = "See parking plan";

        InlineKeyboardMarkup inlineMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        keyboard.add(Collections.singletonList(new InlineKeyboardButton().setText(buttonLabel1).setCallbackData("")));
        keyboard.add(Collections.singletonList(new InlineKeyboardButton().setText(buttonLabel2).setCallbackData("")));
        keyboard.add(Collections.singletonList(new InlineKeyboardButton().setText(buttonLabel3).setCallbackData("")));
        keyboard.add(Collections.singletonList(new InlineKeyboardButton().setText(buttonLabel4).setCallbackData("")));
        keyboard.add(Collections.singletonList(new InlineKeyboardButton().setText(buttonLabel5).setCallbackData("")));
        inlineMarkup.setKeyboard(keyboard);
        
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
