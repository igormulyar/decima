package com.imuliar.decima.service.scheduling;

import com.imuliar.decima.entity.ParkingUser;
import org.springframework.stereotype.Service;

import java.time.LocalTime;

/**
 * <p></p>
 *
 * @author imuliar
 * @since 0.0.1
 */
@Service
public interface PollingService {

    /**
     * <p>Drop all existent tasks and schedule new ones</p>
     */
    void runPoll();

    /**
     * Ask single user
     * @param parkingUser user to be polled
     */
    void poll(ParkingUser parkingUser);
}
