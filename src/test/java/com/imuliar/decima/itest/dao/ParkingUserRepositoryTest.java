package com.imuliar.decima.itest.dao;

import com.imuliar.decima.dao.ParkingUserRepository;
import com.imuliar.decima.entity.*;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;
import java.util.stream.Stream;

/**
 * <p>Integration test for {@link ParkingUserRepository}</p>
 *
 * @author imuliar
 * @since 0.0.1
 */
@RunWith(SpringRunner.class)
@DataJpaTest
public class ParkingUserRepositoryTest extends AbstractRepositoryTest {

    @Autowired
    private ParkingUserRepository parkingUserRepository;

    @Test
    public void findEngagingOwner(){
        ParkingUser user1 = new ParkingUser(1, "user1", null, null, new PollingProfile(9));
        ParkingUser user2 = new ParkingUser(2, "user2", null, null, new PollingProfile(9));
        Slot slot1 = new Slot("1");
        Reservation reservation1 = new Reservation(user1, slot1);
        Reservation reservation2 = new Reservation(user2, slot1); //expected to be found
        reservation2.setPriority(1);
        VacantPeriod vacantPeriod1 = new VacantPeriod(YESTERDAY, TOMORROW, user1);

        ParkingUser user3 = new ParkingUser(3, "user3", null, null, new PollingProfile(9));
        Slot slot3 = new Slot("3");
        Reservation reservation3 = new Reservation(user3, slot3);

        Stream.of(user1, user2, user3, slot1, slot3, reservation1, reservation2, reservation3, vacantPeriod1)
                .forEach(e -> sharedEntityManager.persist(e));

        ParkingUser resultUser = parkingUserRepository.findEngagingOwner(slot1.getNumber(), TODAY)
                .orElseThrow(() -> new AssertionError("result should not be null"));

        Assert.assertEquals(user2.getId(), resultUser.getId());
    }
}
