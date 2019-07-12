package com.imuliar.decima.service.impl;

import com.imuliar.decima.dao.*;
import com.imuliar.decima.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.stream.Stream;

/**
 * //TODO add description <p></p>
 *
 * @author imuliar
 * @since //TODO specify version
 */
@Service
public class FakeDataLoader {

    static final LocalDate TODAY = LocalDate.now();
    static final LocalDate YESTERDAY = TODAY.minusDays(1);

    @Autowired
    SlotRepository slotRepository;

    @Autowired
    ParkingUserRepository parkingUserRepository;

    @Autowired
    BookingRepository bookingRepository;

    @Autowired
    ReserveRepository reserveRepository;

    @Autowired
    VacantPeriodRepository vacantPeriodRepository;


    /*Slot singlePlaceSlotFree = new Slot(1, 1);
    Slot dualPlaceSlotFree = new Slot(2, 2);
    Slot dualPlaceSlotPartiallyBooked = new Slot(3, 2);
    Slot triplePlaceSlotPartiallyBooked = new Slot(4, 3);
    Slot singlePlaceSlotFullBooked = new Slot(5, 1);
    Slot dualPlaceSlotFullBooked = new Slot(6, 2);
    Slot reservedForAurelius = new Slot(7,1);
    Slot reservedForFerdinand = new Slot(8, 1);
    Slot reservedForPlutarch = new Slot(9, 1);

    ParkingUser john = new ParkingUser(1, "johnDoe", "John", "Doe");
    ParkingUser gavrilo = new ParkingUser(2, "gprincip", "Gavrilo", "Princip");
    ParkingUser franz = new ParkingUser(3, "fferdinand", "Franz", "Ferdinand");
    ParkingUser mary = new ParkingUser(4, "mberry", "Mary", "Berry");
    ParkingUser max = new ParkingUser(5, "mmad", "Max", "Mad");
    ParkingUser ivan = new ParkingUser(6, "iivanov", "Ivan", "Ivanov");
    ParkingUser markus = new ParkingUser(7, "mavrely", "Markus", "Aurelius");
    ParkingUser plutarch = new ParkingUser(8, "plutarch", null, null);*/

    /*public void loadDataFull() {
        loadSlotsAnsUsers();

        Reserve aureliusReserve = new Reserve(markus, reservedForAurelius);
        Reserve ferdinandReserve = new Reserve(franz, reservedForFerdinand);
        Reserve plutarchReserve = new Reserve(plutarch, reservedForPlutarch);

        Booking johnsBooking = new Booking(john, dualPlaceSlotPartiallyBooked, TODAY);
        Booking gavrilosBooking = new Booking(gavrilo, triplePlaceSlotPartiallyBooked, TODAY);
        Booking marysBooking = new Booking(mary, singlePlaceSlotFullBooked, TODAY);
        Booking maxsBooking = new Booking(max, dualPlaceSlotFullBooked, TODAY);
        Booking ivansBooking = new Booking(ivan, dualPlaceSlotFullBooked, TODAY);

        Booking yesterdayMaxsBooking = new Booking(max, dualPlaceSlotPartiallyBooked, YESTERDAY);
        Booking yesterdayJohnsBooking = new Booking(john, dualPlaceSlotFullBooked, YESTERDAY);
        Booking yesterDayIvansBooking = new Booking(ivan, dualPlaceSlotFullBooked, YESTERDAY);

        VacantPeriod aureliusVacation = new VacantPeriod(TODAY.plusDays(10), TODAY.plusDays(20), reservedForAurelius);
        VacantPeriod ferdinandVacation = new VacantPeriod(TODAY.minusDays(10), TODAY, reservedForFerdinand);
        VacantPeriod plutarchVacation = new VacantPeriod(TODAY, TODAY.plusDays(3), reservedForPlutarch);
        Booking markusBooking = new Booking(markus, reservedForPlutarch, TODAY);

        Stream.of(aureliusReserve, ferdinandReserve, plutarchReserve).forEach(reserve -> reserveRepository.save(reserve));
        Stream.of(johnsBooking, gavrilosBooking, marysBooking, maxsBooking, ivansBooking, yesterdayJohnsBooking, yesterdayMaxsBooking, yesterDayIvansBooking, markusBooking)
                .forEach(booking -> bookingRepository.save(booking));
        Stream.of(aureliusVacation, ferdinandVacation, plutarchVacation).forEach(period -> vacantPeriodRepository.save(period));
    }

    private void loadSlotsAnsUsers() {
        Stream.of(singlePlaceSlotFree, dualPlaceSlotFree, dualPlaceSlotPartiallyBooked, triplePlaceSlotPartiallyBooked, singlePlaceSlotFullBooked, dualPlaceSlotFullBooked,
                reservedForAurelius, reservedForFerdinand, reservedForPlutarch).forEach(slot -> slotRepository.save(slot));
        Stream.of(john, gavrilo, franz, mary, max, ivan, markus, plutarch).forEach(user -> parkingUserRepository.save(user));
    }*/
}
