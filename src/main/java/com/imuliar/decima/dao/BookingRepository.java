package com.imuliar.decima.dao;

import com.imuliar.decima.entity.Booking;
import com.imuliar.decima.entity.ParkingUser;
import java.time.LocalDate;
import java.util.Optional;

import com.imuliar.decima.entity.Slot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

/**
 * <p>{@link Booking} repository access.</p>
 *
 * @author imuliar
 * @since 0.0.1
 */
@Repository
@Transactional
public interface BookingRepository extends JpaRepository<Booking, Long> {

    Optional<Booking> findByUserAndDate(ParkingUser parkingUser, LocalDate date);

    Optional<Booking> findBySlotAndDate(Slot slot, LocalDate date);

    @Modifying
    @Query("DELETE FROM Booking b WHERE b.date = :date " +
            "AND b.user = (SELECT u FROM ParkingUser u WHERE u.telegramUserId = :telegramUserId) ")
    void deleteBooking(@Param("telegramUserId") Integer telegramUserId, @Param("date") LocalDate date);
}
