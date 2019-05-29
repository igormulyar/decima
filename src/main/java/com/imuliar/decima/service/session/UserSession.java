package com.imuliar.decima.service.session;

import com.imuliar.decima.service.state.AbstractState;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
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
@Getter
@Setter
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class UserSession {

    private LocalDateTime updateTime = LocalDateTime.now();

    private AbstractState currentState;
}
