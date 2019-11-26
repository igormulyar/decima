package com.imuliar.decima.service.state;

import com.imuliar.decima.entity.Booking;
import com.imuliar.decima.entity.ParkingUser;
import com.imuliar.decima.service.UpdateProcessor;
import java.util.List;
import java.util.regex.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * <p>Single state for processing updates from the group chat</p>
 *
 * @author imuliar
 * @since 0.0.1
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class GroupChatUpdateProcessingState extends AbstractState {

    @Autowired
    private List<UpdateProcessor> updateProcessors;

    @Override
    List<UpdateProcessor> getUpdateProcessors() {
        return updateProcessors;
    }
}
