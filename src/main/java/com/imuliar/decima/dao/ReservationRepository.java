package com.imuliar.decima.dao;

import com.imuliar.decima.entity.Reservation;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * <p>Access to {@link Reservation} records</p>
 *
 * @author imuliar
 * @since 0.0.1
 */
@Repository
@Transactional
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    Optional<Reservation> findByUserId(Integer userId);

    @Query("SELECT reservation.userId FROM Reservation reservation " +
            "JOIN reservation.slot slot " +
            "WHERE slot.number = :slotNumber AND reservation.userId NOT IN (" +
            "SELECT vacantPeriod.userId FROM VacantPeriod vacantPeriod " +
            "WHERE vacantPeriod.periodStart <= :date AND :date <= vacantPeriod.periodEnd)")
    Optional<Integer> findEngagingOwner(@Param("slotNumber") String slotNumber, @Param("date") LocalDate date);

    @Query("SELECT reservation FROM Reservation reservation " +
            "WHERE reservation.lastPollTimestamp IS NULL OR reservation.lastPollTimestamp <> :date " +
            "AND reservation.userId NOT IN (" +
            "SELECT vacantPeriod.userId FROM VacantPeriod vacantPeriod " +
            "WHERE :date >= vacantPeriod.periodStart AND :date <= vacantPeriod.periodEnd)")
    List<Reservation> findUnpolled(@Param("date") LocalDate date);
}
