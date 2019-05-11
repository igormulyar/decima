package com.imuliar.decima.dao;

import com.imuliar.decima.entity.Slot;
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
            "GROUP BY bookedSlot.id, b.date " +
            "HAVING COUNT(b) = bookedSlot.maxLoading " +
            "AND b.date = :date) " +
            "AND slot NOT IN " +
            "(SELECT reservedSlot FROM Reserve reserve " +
            "JOIN reserve.slot reservedSlot " +
            "WHERE reservedSlot NOT IN " +
            "(SELECT vacantSlot FROM VacantPeriod vacantPeriod " +
            "JOIN vacantPeriod.slot vacantSlot " +
            "WHERE vacantPeriod.periodStart <= :date AND :date <= vacantPeriod.periodEnd))")
    List<Slot> findFreeSlots(@Param("date") LocalDate date);
}
