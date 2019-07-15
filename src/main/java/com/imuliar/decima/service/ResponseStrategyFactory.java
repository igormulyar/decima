package com.imuliar.decima.service;

import com.imuliar.decima.entity.ParkingUser;

/**
 * //TODO add description <p></p>
 *
 * @author imuliar
 * @since //TODO specify version
 */
public interface ResponseStrategyFactory {

    ResponseStrategy getStrategy(ParkingUser parkingUser, Long chatId);
}
