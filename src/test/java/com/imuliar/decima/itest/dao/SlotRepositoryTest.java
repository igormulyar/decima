package com.imuliar.decima.itest.dao;

import com.imuliar.decima.dao.SlotRepository;
import com.imuliar.decima.entity.*;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * <p>Integration test for {@link SlotRepository}</p>
 *
 * @author imuliar
 * @since 0.0.1
 */
@RunWith(SpringRunner.class)
@DataJpaTest
public class SlotRepositoryTest extends AbstractRepositoryTest {

    @Autowired
    private SlotRepository slotRepository;

    @Test
    public void findFreeSlots() {
        //Just under the reservation
        Slot slot1 = new Slot("1");
        ParkingUser user1 = new ParkingUser(1, "user1", "", "", new PollingProfile());
        Reservation reservation1 = new Reservation(user1, slot1);

        //Under the reservation but free from today to tomorrow
        //free
        Slot slot1a = new Slot("1a");
        ParkingUser user1a = new ParkingUser(11, "user1a", null, null, new PollingProfile());
        Reservation reservation1a = new Reservation(user1a, slot1a);
        VacantPeriod vacantPeriod1a = new VacantPeriod(TODAY, TOMORROW, user1a);

        //Under the reservation but free from yesterday to today
        //free
        Slot slot2 = new Slot("2");
        ParkingUser user2 = new ParkingUser(2, "user2", "", "", new PollingProfile());
        Reservation reservation2 = new Reservation(user2, slot2);
        VacantPeriod vacantPeriod2 = new VacantPeriod(YESTERDAY, TODAY, user2);

        //Under the reservation but released and booked by ordinary user
        Slot slot3 = new Slot("3");
        ParkingUser user3 = new ParkingUser(3, "user3", "", "", new PollingProfile());
        Reservation reservation3 = new Reservation(user3, slot3);
        VacantPeriod vacantPeriod3 = new VacantPeriod(YESTERDAY, TODAY, user3);
        ParkingUser user33 = new ParkingUser(33, "user33", "", null, null);
        Booking booking3 = new Booking(user33, slot3, TODAY);

        //Under 2 reservations but released by the first slot owner
        Slot slot4 = new Slot("4");
        ParkingUser user4 = new ParkingUser(4, "user4", "", "", new PollingProfile());
        Reservation reservation4 = new Reservation(user4, slot4);
        VacantPeriod vacantPeriod4 = new VacantPeriod(YESTERDAY, TODAY, user4);
        ParkingUser user44 = new ParkingUser(44, "user44", "", "", new PollingProfile());
        Reservation reservation44 = new Reservation(user44, slot4);
        reservation44.setPriority(1);

        //Under 2 reservations and released by both owners
        //free
        Slot slot5 = new Slot("5");
        ParkingUser user5 = new ParkingUser(5, "user5", null, null, new PollingProfile());
        Reservation reservation5 = new Reservation(user5, slot5);
        VacantPeriod vacantPeriod5 = new VacantPeriod(YESTERDAY, TODAY, user5);
        ParkingUser user55 = new ParkingUser(55, "user55", null, null, new PollingProfile());
        Reservation reservation55 = new Reservation(user55, slot5);
        reservation55.setPriority(1);
        VacantPeriod vacantPeriod55 = new VacantPeriod(YESTERDAY, TODAY, user55);

        //Under 2 reservations and released by both owners but booked by ordinary user
        Slot slot6 = new Slot("6");
        ParkingUser user6 = new ParkingUser(6, "user6", null, null, new PollingProfile());
        Reservation reservation6 = new Reservation(user6, slot6);
        VacantPeriod vacantPeriod6 = new VacantPeriod(YESTERDAY, TODAY, user6);
        ParkingUser user66 = new ParkingUser(66, "user66", null, null, new PollingProfile());
        Reservation reservation66 = new Reservation(user66, slot6);
        reservation66.setPriority(1);
        VacantPeriod vacantPeriod66 = new VacantPeriod(YESTERDAY, TODAY, user66);
        ParkingUser usr67 = new ParkingUser(67, "user67", "", null, null);
        Booking booking6 = new Booking(usr67, slot6, TODAY);

        //not reserved
        //free
        Slot slot7 = new Slot("7");

        Stream.of(slot1, slot1a, slot2, slot3, slot4, slot5, slot6, slot7,
                user1, user1a, user2, user3, user33, user4, user44, user5, user55, user6, user66, usr67,
                reservation1, reservation1a, reservation2, reservation3, reservation4, reservation44, reservation5, reservation55, reservation6, reservation66,
                vacantPeriod1a, vacantPeriod2, vacantPeriod3, vacantPeriod4, vacantPeriod5, vacantPeriod6, vacantPeriod55, vacantPeriod66,
                booking3, booking6)
                .forEach(e -> sharedEntityManager.persist(e));


        List<Slot> result = slotRepository.findFreeSlots(TODAY).stream()
                .sorted(Comparator.comparing(Slot::getNumber))
                .collect(Collectors.toList());

        Assert.assertEquals(4, result.size());
        Assert.assertEquals(slot1a.getId(), result.get(0).getId());
        Assert.assertEquals(slot2.getId(), result.get(1).getId());
        Assert.assertEquals(slot5.getId(), result.get(2).getId());
        Assert.assertEquals(slot7.getId(), result.get(3).getId());
    }
}
