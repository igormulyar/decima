package com.imuliar.decima.service.session;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * <p>Session of the bot user</p>
 *
 * @author imuliar
 * @since 0.0.1
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class UserSession {

    private Map<String, Object> context;

    private LocalDateTime updateTime = LocalDateTime.now();

    private SessionState currentState;

    private String langCode;

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }

    public SessionState getCurrentState() {
        return currentState;
    }

    public void setCurrentState(SessionState currentState) {
        this.currentState = currentState;
    }

    public Map<String, Object> getContext() {
        return context;
    }

    public void setContext(Map<String, Object> context) {
        this.context = context;
    }

    public String getLangCode() {
        return langCode;
    }

    public void setLangCode(String langCode) {
        this.langCode = langCode;
    }
}
