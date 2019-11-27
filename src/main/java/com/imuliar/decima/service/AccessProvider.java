package com.imuliar.decima.service;

import org.telegram.telegrambots.meta.api.objects.User;

/**
 * <p>Service for solving access providing work</p>
 *
 * @author imuliar
 * @since 0.0.1
 */
public interface AccessProvider {

    boolean isPermitted(User telegramUser);
}
