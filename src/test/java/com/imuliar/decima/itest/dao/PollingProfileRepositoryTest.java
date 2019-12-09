package com.imuliar.decima.itest.dao;

import com.imuliar.decima.dao.PollingProfileRepository;
import com.imuliar.decima.entity.PollingProfile;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>Integration test for {@link PollingProfileRepository}</p>
 *
 * @author imuliar
 * @since 0.0.1
 */
@RunWith(SpringRunner.class)
@DataJpaTest
public class PollingProfileRepositoryTest extends  AbstractRepositoryTest{

    @Autowired
    private PollingProfileRepository pollingProfileRepository;

    @Test
    public void saveOrUpdate(){
        PollingProfile profile1 = new PollingProfile(1);
        profile1.setLastAnswerReceived(TODAY);
        pollingProfileRepository.save(profile1);
        PollingProfile profile2 = new PollingProfile(2);
        profile2.setLastAnswerReceived(TODAY);
        pollingProfileRepository.save(profile2);
        sharedEntityManager.flush();
        sharedEntityManager.clear();

        PollingProfile fromDb = pollingProfileRepository.findAll().get(0);
        fromDb.setLastAnswerReceived(YESTERDAY);
        pollingProfileRepository.save(fromDb);
        sharedEntityManager.flush();
        sharedEntityManager.clear();

        List<PollingProfile> result = pollingProfileRepository.findAll().stream()
                .sorted(Comparator.comparing(PollingProfile::getLastAnswerReceived))
                .collect(Collectors.toList());
        Assert.assertEquals(2, result.size());
        Assert.assertEquals(YESTERDAY, result.get(0).getLastAnswerReceived());
        Assert.assertEquals(TODAY, result.get(1).getLastAnswerReceived());
    }
}
