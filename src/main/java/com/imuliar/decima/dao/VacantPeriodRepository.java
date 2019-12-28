package com.imuliar.decima.dao;

import com.imuliar.decima.entity.VacantPeriod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * <p>Access to {@link VacantPeriod} records</p>
 *
 * @author imuliar
 * @since 0.0.1
 */
@Repository
public interface VacantPeriodRepository extends JpaRepository<VacantPeriod, Long> {

    List<VacantPeriod> findByUserId(Integer userId);

    @Query("SELECT vacantPeriod FROM VacantPeriod vacantPeriod " +
            "WHERE vacantPeriod.userId = :userId " +
            "AND :date >= vacantPeriod.periodStart AND :date <= vacantPeriod.periodEnd")
    List<VacantPeriod> findByUserIdAndDate(@Param("userId") Integer userId, @Param("date") LocalDate date);

    /**
     * Checks if passed arguments has intersections with existing vacant period representation in db
     *
     * @param telegramUserId telegram user identifier
     * @param startDate      start period criterion
     * @param endDate        end period criterion
     * @return {@literal true} if has intersections, otherwise - {@literal false}
     */
    @Query("SELECT COUNT(vp) > 0 FROM VacantPeriod vp " +
            "WHERE vp.userId = :telegramUserId " +
            "AND (vp.periodEnd >= :startDate OR vp.periodStart <= :endDate) ")
    Boolean hasIntersections(@Param("telegramUserId") Integer telegramUserId, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
}
