package com.imuliar.decima.service.scheduling;

import com.imuliar.decima.entity.ParkingUser;
import org.springframework.stereotype.Service;

import java.time.LocalTime;

/**
 * //TODO add description <p></p>
 *
 * @author imuliar
 * @since //TODO specify version
 */
@Service
public interface PollingService {

    /**
     * <p>Drop all existent tasks and schedule new ones</p>
     */
    void runPoll();
}
