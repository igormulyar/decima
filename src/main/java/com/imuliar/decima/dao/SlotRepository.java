package com.imuliar.decima.dao;

import com.imuliar.decima.entity.Slot;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;

/**
 * <p>{@link Slot} repository access</p>
 *
 * @author imuliar
 * @since 0.0.1
 */
@Repository
@Transactional
public interface SlotRepository extends JpaRepository<Slot, Long> {

    @Query("SELECT slot FROM Slot slot " +
            "WHERE slot NOT IN " +
            "(SELECT bookedSlot FROM Booking b " +
            "JOIN b.slot bookedSlot " +
            "WHERE b.date = :date) " +
            "AND slot NOT IN " +
            "(SELECT reservedSlot FROM Reservation reservation " +
            "JOIN reservation.slot reservedSlot " +
            "WHERE reservation.userId NOT IN " +
            "(SELECT vacantPeriod.userId FROM VacantPeriod vacantPeriod " +
            "WHERE vacantPeriod.periodStart <= :date AND :date <= vacantPeriod.periodEnd))")
    List<Slot> findFreeSlots(@Param("date") LocalDate date);

    Optional<Slot> findByNumber(String number);
 }
