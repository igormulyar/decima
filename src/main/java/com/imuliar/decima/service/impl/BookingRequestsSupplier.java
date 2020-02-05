package com.imuliar.decima.service.impl;

import java.util.Collections;
import java.util.LinkedList;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.User;

/**
 * <p>Supply info about existent unsatisfied booking requests</p>
 *
 * @author imuliar
 * @since 0.0.1
 */
@Service
public class BookingRequestsSupplier {

    @Autowired
    private Map<Integer, User> slotRequestBuffer;

    public synchronized Optional<User> pullOutRandomRequester(){
        LinkedList<User> users = new LinkedList<>(slotRequestBuffer.values());
        Collections.shuffle(users);
        Optional<User> randomRequestingUser = Optional.ofNullable(users.poll());
        randomRequestingUser.ifPresent(u -> slotRequestBuffer.remove(u.getId()));
        return randomRequestingUser;
    }
}
