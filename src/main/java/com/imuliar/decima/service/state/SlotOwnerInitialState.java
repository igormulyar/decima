package com.imuliar.decima.service.state;

import com.imuliar.decima.entity.ParkingUser;
import com.imuliar.decima.entity.Reservation;
import com.imuliar.decima.entity.VacantPeriod;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

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

    @Override
    public void processUpdate(Long chatId, ParkingUser parkingUser, Update update) {
        if (update.hasCallbackQuery()) {
            String callbackString = update.getCallbackQuery().getData();
            if (SET_FREE_MATCHING_PATTERN.matcher(callbackString).matches()) {
                VacantPeriod vacantPeriod = parseSetFreeCallbackData(callbackString);
            }
        }

    }

    private VacantPeriod parseSetFreeCallbackData(String callbackString) {
        List<String> splitStringData = Arrays.asList(callbackString.split("#"));
        Integer userTelegramId = Integer.valueOf(splitStringData.get(1));
        LocalDate vacantDate = LocalDate.parse(splitStringData.get(2), DateTimeFormatter.ISO_DATE);
        Reservation reservation = getReserveRepository().findByTelegramUserId(userTelegramId)
                .orElseThrow(() -> new IllegalStateException("Reservation should exist for user who has set slot free."));
        return new VacantPeriod(vacantDate, vacantDate, reservation.getSlot());
    }
}
