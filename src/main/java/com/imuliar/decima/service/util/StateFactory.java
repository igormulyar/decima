package com.imuliar.decima.service.util;

import com.imuliar.decima.service.state.SessionState;
import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.stereotype.Service;

/**
 * <p>Provides state prototypes</p>
 *
 * @author imuliar
 * @since 0.0.1
 */
@Service
public abstract class StateFactory {

    @Lookup("ordinaryInitialState")
    public abstract SessionState getOrdinaryInitialState();

    @Lookup("slotOwnerInitialState")
    public abstract SessionState getSlotOwnerInitialState();

    @Lookup("engagingUserSearchState")
    public abstract SessionState getEngagingUserSearchState();

    @Lookup("groupChatUpdateProcessingState")
    public abstract SessionState getGroupChatUpdateProcessingState();
}
