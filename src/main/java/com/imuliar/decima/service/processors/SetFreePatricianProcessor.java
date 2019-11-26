package com.imuliar.decima.service.processors;

import com.imuliar.decima.dao.PollingProfileRepository;
import com.imuliar.decima.entity.ParkingUser;
import com.imuliar.decima.entity.PollingProfile;
import com.imuliar.decima.entity.Reservation;
import com.imuliar.decima.entity.Slot;
import com.imuliar.decima.entity.VacantPeriod;
import com.imuliar.decima.service.state.AbstractState;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import static com.imuliar.decima.service.util.RegexPatterns.SET_FREE_MATCHING_PATTERN;

/**
 * <p>Process "set slot free" order from slot owner</p>
 *
 * @author imuliar
 * @since 0.0.1
 */
@Service
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class SetFreePatricianProcessor extends AbstractUpdateProcessor {

    private static final String RELEASE_MESSAGE_PATTERN = "Slot # %s has been released by user %s.";

    @Autowired
    private PollingProfileRepository pollingProfileRepository;

    @Override
    public boolean isMatch(Update update) {
        return regexpEvaluating.apply(update, SET_FREE_MATCHING_PATTERN);
    }

    @Override
    Optional<AbstractState> doProcess(Update update, ParkingUser parkingUser, Long chatId) {
        String callbackString = update.getCallbackQuery().getData();
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
            return Optional.empty();
        }

        for(Reservation res : allReservationsForSingleSlot.subList(currentUserReservation.getPriority() + 1, allReservationsForSingleSlot.size())){
            ParkingUser parkingProposalCandidate = res.getUser();
            boolean isCandidateAbsent = getVacantPeriodRepository().isUserAbsent(parkingProposalCandidate.getId(), LocalDate.now());
            if(!isCandidateAbsent){
                //notify candidate
                publishReleaseMessage(releasedSlot, parkingProposalCandidate);
                return Optional.empty();
            }
        }
        publishReleaseMessage(releasedSlot, parkingUser);
        return Optional.empty();
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
