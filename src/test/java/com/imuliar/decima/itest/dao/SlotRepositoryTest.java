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
        Reservation reservation1 = new Reservation(1, slot1);

        //Under the reservation but free from today to tomorrow
        //free
        Slot slot1a = new Slot("1a");
        Reservation reservation1a = new Reservation(11, slot1a);
        VacantPeriod vacantPeriod1a = new VacantPeriod(11, TODAY, TOMORROW);

        //Under the reservation but free from yesterday to today
        //free
        Slot slot2 = new Slot("2");
        Reservation reservation2 = new Reservation(2, slot2);
        VacantPeriod vacantPeriod2 = new VacantPeriod(2, YESTERDAY, TODAY);

        //Under the reservation but released and booked by ordinary user
        Slot slot3 = new Slot("3");
        Reservation reservation3 = new Reservation(3, slot3);
        VacantPeriod vacantPeriod3 = new VacantPeriod(3, YESTERDAY, TODAY);
        Booking booking3 = new Booking(33, slot3, TODAY);

        //Under the reservation but released by the  slot owner
        Slot slot4 = new Slot("4");
        Reservation reservation4 = new Reservation(4, slot4);
        VacantPeriod vacantPeriod4 = new VacantPeriod(4, YESTERDAY, TODAY);

        //not reserved
        //free
        Slot slot7 = new Slot("7");

        Stream.of(slot1, slot1a, slot2, slot3, slot4, slot7,
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
