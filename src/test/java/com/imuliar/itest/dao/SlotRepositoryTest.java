package com.imuliar.itest.dao;

import com.imuliar.decima.dao.ParkingPlaceRepository;
import com.imuliar.decima.dao.SlotRepository;
import com.imuliar.decima.entity.EntityFrame;
import com.imuliar.decima.entity.Slot;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.CollectionUtils;

import java.util.Comparator;
import java.util.List;

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
    private ParkingPlaceRepository parkingPlaceRepository;

    //TEST OBJECT
    @Autowired
    private SlotRepository slotRepository;

    @Before
    public void setup() {
        loadDataFull();
    }

    @Test
    public void findFreeSlots() {
        List<Slot> resultSlots = slotRepository.findFreeSlots(TODAY);
        resultSlots.sort((Comparator.comparing(EntityFrame::getId)));

        Assert.assertFalse(CollectionUtils.isEmpty(resultSlots));
        Assert.assertEquals(5, resultSlots.size());
        Assert.assertEquals(singlePlaceSlotFree.getId(), resultSlots.get(0).getId());
        Assert.assertEquals(dualPlaceSlotFree.getId(), resultSlots.get(1).getId());
        Assert.assertEquals(dualPlaceSlotPartiallyBooked.getId(), resultSlots.get(2).getId());
        Assert.assertEquals(triplePlaceSlotPartiallyBooked.getId(), resultSlots.get(3).getId());
        Assert.assertEquals(reservedForFerdinand.getId(), resultSlots.get(4).getId());
    }
}
