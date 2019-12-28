package com.imuliar.decima.dao;

import com.imuliar.decima.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.Optional;

/**
 * <p>{@link Booking} repository access.</p>
 *
 * @author imuliar
 * @since 0.0.1
 */
@Repository
@Transactional
public interface BookingRepository extends JpaRepository<Booking, Long> {

    Optional<Booking> findByUserIdAndDate(Integer userId, LocalDate date);

    @Query("SELECT booking FROM Booking booking " +
            "JOIN booking.slot slot " +
            "WHERE booking.date = :date AND slot.number = :slotNumber")
    Optional<Booking> findBySlotNumberAndDate(@Param("slotNumber") String slotNumber, @Param("date") LocalDate date);

    void removeByUserIdAndDate(Integer userId, LocalDate date);
}
