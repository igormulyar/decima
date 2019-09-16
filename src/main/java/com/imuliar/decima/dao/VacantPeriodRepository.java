package com.imuliar.decima.dao;

import com.imuliar.decima.entity.VacantPeriod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * <p>Access to {@link VacantPeriod} records</p>
 *
 * @author imuliar
 * @since 0.0.1
 */
@Repository
public interface VacantPeriodRepository extends JpaRepository<VacantPeriod, Long> {
}
