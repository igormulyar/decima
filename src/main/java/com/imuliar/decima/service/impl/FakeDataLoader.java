package com.imuliar.decima.service.impl;

import com.imuliar.decima.dao.ReservationRepository;
import com.imuliar.decima.dao.SlotRepository;
import com.imuliar.decima.dao.VacantPeriodRepository;
import com.imuliar.decima.entity.Reservation;
import com.imuliar.decima.entity.Slot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

/**
 * <p>Load fake data for development purposes</p>
 *
 * @author imuliar
 * @since 0.0.1
 */
@Service
public class FakeDataLoader {

    static final LocalDate TODAY = LocalDate.now();
    static final LocalDate YESTERDAY = TODAY.minusDays(1);

    @Value("${fake.user.id.a}")
    private Integer aUserId;

    @Value("${fake.user.id.i}")
    private Integer iUserId;

    @Autowired
    private SlotRepository slotRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    VacantPeriodRepository vacantPeriodRepository;

    public void loadData() {
        if (reservationRepository.findAll().isEmpty()) {
            Slot iSlot = new Slot("5f");
            Slot freeSlot = new Slot("6f");
            Slot freeSlot2 = new Slot("7f");
            slotRepository.save(iSlot);
            slotRepository.save(freeSlot);
            slotRepository.save(freeSlot2);

            Reservation iUserReservation = new Reservation(iUserId, iSlot);
            reservationRepository.save(iUserReservation);
        }
    }
}
