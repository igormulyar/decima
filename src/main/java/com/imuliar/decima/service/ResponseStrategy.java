package com.imuliar.decima.service;

import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * <p>Service that encapsulates the batch of operations for generating and sending the proper response for input update</p>
 *
 * @author imuliar
 * @since 0.0.1
 */
public interface ResponseStrategy {

    void response(Update update);

}
