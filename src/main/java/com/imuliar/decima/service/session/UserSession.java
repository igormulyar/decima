package com.imuliar.decima.service.session;

import com.imuliar.decima.service.state.SessionState;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * <p>Session of the bot user</p>
 *
 * @author imuliar
 * @since 0.0.1
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class UserSession {

    private LocalDateTime updateTime = LocalDateTime.now();

    private SessionState currentState;

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
}
