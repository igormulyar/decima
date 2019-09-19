package com.imuliar.decima.dao;

import com.imuliar.decima.entity.VacantPeriod;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * <p>Access to {@link VacantPeriod} records</p>
 *
 * @author imuliar
 * @since 0.0.1
 */
@Repository
public interface VacantPeriodRepository extends JpaRepository<VacantPeriod, Long> {

    @Query("SELECT vp FROM VacantPeriod vp " +
            "WHERE vp.user.id = :userId " +
            "AND :date >= vp.periodStart AND :date <= periodEnd")
    List<VacantPeriod> findByUserAndDate(@Param("userId") Long userId, @Param("date") LocalDate date);


    /**
     * Check if user is supposed to park their car at specified day
     *
     * @param userId user entity id
     * @param date   date as search criterion
     * @return true if VacantPeriod record for the user exists and specified date is within the period's dates range, otherwise return false
     */
    @Query("SELECT COUNT(vp) > 0 FROM VacantPeriod vp " +
            "WHERE vp.user.id = :userId " +
            "AND :date >= vp.periodStart AND :date <= periodEnd")
    Boolean isUserAbsent(@Param("userId") Long userId, @Param("date") LocalDate date);
}
