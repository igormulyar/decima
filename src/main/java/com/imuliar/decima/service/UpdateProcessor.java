package com.imuliar.decima.service;

import com.imuliar.decima.entity.ParkingUser;
import com.imuliar.decima.service.state.SessionState;
import java.util.Optional;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * <p>Single update processing unit</p>
 *
 * @author imuliar
 * @since 0.0.1
 */
public interface UpdateProcessor {

    boolean isMatch(Update update);

    Optional<SessionState> process(Update update, ParkingUser parkingUser);
}
