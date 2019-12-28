package com.imuliar.decima.itest.dao;

import com.imuliar.decima.dao.ReservationRepository;
import com.imuliar.decima.entity.Reservation;
import com.imuliar.decima.entity.Slot;
import com.imuliar.decima.entity.VacantPeriod;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

/**
 * <p>Integration test for {@link ReservationRepository}</p>
 *
 * @author imuliar
 * @since 0.0.1
 */
@RunWith(SpringRunner.class)
@DataJpaTest
public class ReservationRepositoryTest extends AbstractRepositoryTest {

    private static final LocalDate TODAY = LocalDate.now();

    private static final LocalDate YESTERDAY = TODAY.minusDays(1);

    @Autowired
    private ReservationRepository reservationRepository;

    @Test
    public void findEngagingOwner(){
        Slot slot1 = new Slot("1");
        Reservation reservation1 = new Reservation(1, slot1);
        VacantPeriod vacantPeriod1 = new VacantPeriod(1, YESTERDAY, TOMORROW);

        Slot slot2 = new Slot("2");
        Reservation reservation2 = new Reservation(2, slot2);

        Stream.of(slot1, slot2, reservation1, reservation2, vacantPeriod1)
                .forEach(e -> sharedEntityManager.persist(e));

        Integer result = reservationRepository.findEngagingOwner(slot2.getNumber(), TODAY)
                .orElseThrow(() -> new AssertionError("result should not be null"));

        Assert.assertEquals(Integer.valueOf(2), result);
    }

    @Test
    public void saveOrUpdate(){
        Slot slot1 = new Slot("1");
        Slot slot2 = new Slot("2");
        sharedEntityManager.persist(slot1);
        sharedEntityManager.persist(slot2);

        Reservation reservation1 = new Reservation(1, slot1);
        reservation1.setLastPollTimestamp(YESTERDAY);
        reservationRepository.save(reservation1);

        Reservation reservation2 = new Reservation(2, slot2);
        reservation2.setLastPollTimestamp(TODAY);
        reservationRepository.save(reservation2);

        sharedEntityManager.flush();
        sharedEntityManager.clear();

        Reservation reservation1FromDb = reservationRepository.findByUserId(1)
                .orElseThrow(() -> new AssertionError("reservation 1 should exist in DB"));

        reservation1FromDb.setLastPollTimestamp(TODAY);
        reservationRepository.save(reservation1FromDb);
        sharedEntityManager.flush();
        sharedEntityManager.clear();

        List<Reservation> all = reservationRepository.findAll();
        all.sort(Comparator.comparing(Reservation::getUserId));

        Assert.assertFalse(CollectionUtils.isEmpty(all));
        Assert.assertEquals(Integer.valueOf(1), all.get(0).getUserId());
        Assert.assertEquals(TODAY, all.get(0).getLastPollTimestamp());

    }
}
