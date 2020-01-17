package com.imuliar.decima.service.session;

import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

/**
 * <p>Default implementation of {@link SessionProvider}</p>
 *
 * @author imuliar
 * @since 0.0.1
 */
@Service
public abstract class SessionProviderImpl implements SessionProvider {

    /**
     * Time of session being alive
     */
    private static final Duration SESSION_TTL = Duration.ofSeconds(45);

    /**
     * All sessions in-memory storage
     */
    private Map<Long, UserSession> userSessionPool = new ConcurrentHashMap<>();

    @Override
    public UserSession provideSession(Update update) {
        clearExpiredSessions();

        Long chatId = resolveChatId(update);
        UserSession session = userSessionPool.get(chatId);
        if (session != null) {
            session.setUpdateTime(LocalDateTime.now());
        } else {
            User user = resolveTelegramUser(update);
            session = getUserSession();
            session.setLangCode(user.getLanguageCode());
            session.setContext(new HashMap<>());
            userSessionPool.put(chatId, session);
        }
        return session;
    }

    private void clearExpiredSessions() {
        Predicate<Map.Entry<Long, UserSession>> notExpired = entry -> Duration.between(entry.getValue().getUpdateTime(),
                LocalDateTime.now()).compareTo(SESSION_TTL) < 0;
        userSessionPool = userSessionPool.entrySet().stream()
                .filter(notExpired)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (v1, v2) -> v1, ConcurrentHashMap::new));
    }

    private Long resolveChatId(Update update) {
        return update.hasMessage()
                ? update.getMessage().getChatId()
                : update.getCallbackQuery().getMessage().getChatId();
    }

    private User resolveTelegramUser(Update update) {
        return update.getMessage() != null
                ? update.getMessage().getFrom()
                : update.getCallbackQuery().getFrom();
    }

    @Lookup
    public abstract UserSession getUserSession();
}
