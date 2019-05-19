package com.imuliar.decima.service;

import com.imuliar.decima.entity.ParkingUser;
import org.telegram.telegrambots.meta.api.objects.User;

/**
 * <p>Service for managing {@link ParkingUser} data</p>
 *
 * @author imuliar
 * @since 0.0.1
 */
public interface ParkingUserService {

    ParkingUser findOrCreateParkingUser(User telegramUser);
}
