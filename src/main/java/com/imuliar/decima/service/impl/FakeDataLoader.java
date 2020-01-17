package com.imuliar.decima.service.impl;

import com.imuliar.decima.dao.ReservationRepository;
import com.imuliar.decima.dao.SlotRepository;
import com.imuliar.decima.dao.VacantPeriodRepository;
import com.imuliar.decima.entity.Reservation;
import com.imuliar.decima.entity.Slot;
import java.util.stream.Stream;
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

    @Value("${fake.user.id.alina}")
    private Integer aUserId;

    @Value("${fake.user.id.igor}")
    private Integer iUserId;

    @Value("${fake.user.id.nastya}")
    private Integer nastyaId;

    @Value("${fake.user.id.diana}")
    private Integer dianaId;

    @Value("${fake.user.id.alexandr}")
    private Integer alexandrId;

    @Value("${fake.user.id.luda}")
    private Integer ludaId;

    @Autowired
    private SlotRepository slotRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    VacantPeriodRepository vacantPeriodRepository;

    public void loadData() {
        if (reservationRepository.findAll().isEmpty()) {

            Slot alexandrSlot = new Slot("1f");
            Slot dianaSlot = new Slot("2f");
            Slot freeSlot = new Slot("3f");
            Slot igorSlot = new Slot("4f");
            Slot ludaSlot = new Slot("5f");
            Stream.of(alexandrSlot, dianaSlot, ludaSlot).forEach(s -> slotRepository.save(s));

            Reservation alexReservation = new Reservation(alexandrId, alexandrSlot);
            Reservation dianaSlotReservation = new Reservation(dianaId, dianaSlot);
            Reservation ludaReservation = new Reservation(ludaId, ludaSlot);
            Reservation igorReservation = new Reservation(iUserId, igorSlot);
            Stream.of(alexReservation, dianaSlotReservation, ludaReservation).forEach(r -> reservationRepository.save(r));

        }
    }
}
