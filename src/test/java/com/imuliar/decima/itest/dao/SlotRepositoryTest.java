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

        //Under the reservation but released by the  slot owner
        Slot slot4 = new Slot("4");
        ParkingUser user4 = new ParkingUser(4, "user4", "", "", new PollingProfile());
        Reservation reservation4 = new Reservation(user4, slot4);
        VacantPeriod vacantPeriod4 = new VacantPeriod(YESTERDAY, TODAY, user4);

        //not reserved
        //free
        Slot slot7 = new Slot("7");

        Stream.of(slot1, slot1a, slot2, slot3, slot4, slot7,
                user1, user1a, user2, user3, user33, user4,
                reservation1, reservation1a, reservation2, reservation3, reservation4,
                vacantPeriod1a, vacantPeriod2, vacantPeriod3, vacantPeriod4, booking3)
                .forEach(e -> sharedEntityManager.persist(e));


        List<Slot> result = slotRepository.findFreeSlots(TODAY).stream()
                .sorted(Comparator.comparing(Slot::getNumber))
                .collect(Collectors.toList());

        Assert.assertEquals(4, result.size());
        Assert.assertEquals(slot1a.getId(), result.get(0).getId());
        Assert.assertEquals(slot2.getId(), result.get(1).getId());
        Assert.assertEquals(slot4.getId(), result.get(2).getId());
        Assert.assertEquals(slot7.getId(), result.get(3).getId());
    }
}
