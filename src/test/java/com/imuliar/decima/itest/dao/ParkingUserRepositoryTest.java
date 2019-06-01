package com.imuliar.decima.itest.dao;

import com.imuliar.decima.dao.ParkingUserRepository;
import com.imuliar.decima.entity.ParkingUser;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Optional;

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

    @Before
    public void setup() {
        loadDataFull();
    }

    @Test
    public void findNeighbours(){
        List<ParkingUser> resultNeighbours = parkingUserRepository.findNeighbours(max.getId(), TODAY);
        Assert.assertFalse(CollectionUtils.isEmpty(resultNeighbours));
        Assert.assertEquals(1, resultNeighbours.size());
    }

    @Test
    public void findByTelegramUserId() {
        Optional<ParkingUser> result = parkingUserRepository.findByTelegramUserId(markus.getTelegramUserId());
        Assert.assertTrue(result.isPresent());
        Assert.assertEquals(markus.getId(), result.get().getId());
    }
}
