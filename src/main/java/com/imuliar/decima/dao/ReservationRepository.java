package com.imuliar.decima.dao;

import com.imuliar.decima.entity.ParkingUser;
import com.imuliar.decima.entity.Reservation;
import com.imuliar.decima.entity.Slot;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
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
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    Optional<Reservation> findByUser(ParkingUser parkingUser);

    @Query("SELECT r FROM Reservation r " +
            "JOIN r.user u " +
            "WHERE u.telegramUserId = :telegramUserId")
    Optional<Reservation> findByTelegramUserId(@Param("telegramUserId") Integer telegramUserId);

    List<Reservation> findBySlot(Slot slot);
}
