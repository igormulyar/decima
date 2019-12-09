package com.imuliar.decima.itest.dao;

import com.imuliar.decima.entity.*;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.util.stream.Stream;

/**
 * <p>Encapsulates common properties and behavior for repository tests</p>
 *
 * @author imuliar
 * @since 0.0.1
 */
public class AbstractRepositoryTest {

    static final LocalDate TODAY = LocalDate.now();
    static final LocalDate YESTERDAY = TODAY.minusDays(1);
    static final LocalDate TOMORROW = TODAY.plusDays(1);

    @Autowired
    EntityManager sharedEntityManager;

}
