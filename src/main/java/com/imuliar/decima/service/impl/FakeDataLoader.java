package com.imuliar.decima.service.impl;

import com.imuliar.decima.dao.BookingRepository;
import com.imuliar.decima.dao.ParkingUserRepository;
import com.imuliar.decima.dao.ReservationRepository;
import com.imuliar.decima.dao.SlotRepository;
import com.imuliar.decima.dao.VacantPeriodRepository;
import com.imuliar.decima.entity.ParkingUser;
import com.imuliar.decima.entity.PollingProfile;
import com.imuliar.decima.entity.Reservation;
import com.imuliar.decima.entity.Slot;
import java.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

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
    SlotRepository slotRepository;

    @Autowired
    ParkingUserRepository parkingUserRepository;

    @Autowired
    BookingRepository bookingRepository;

    @Autowired
    ReservationRepository reservationRepository;

    @Autowired
    VacantPeriodRepository vacantPeriodRepository;

    public void loadData(){
        if(parkingUserRepository.findAll().isEmpty()){
            Slot iSlot = new Slot("5f");
            Slot freeSlot = new Slot("6f");
            Slot freeSlot2 = new Slot("7f");
            slotRepository.save(iSlot);
            slotRepository.save(freeSlot);
            slotRepository.save(freeSlot2);

            ParkingUser aUser = new ParkingUser(aUserId, null, "alina", null, null);
            //aUser.setPollingProfile(new PollingProfile(22));
            ParkingUser iUser = new ParkingUser(iUserId, null, "igor", null, new PollingProfile(22));
            parkingUserRepository.save(aUser);
            parkingUserRepository.save(iUser);

            Reservation iUserReservation = new Reservation(iUser, iSlot);
            reservationRepository.save(iUserReservation);
        }
    }
}
