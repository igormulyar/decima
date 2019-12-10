package com.imuliar.decima.dao;

import com.imuliar.decima.entity.ParkingUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * <p>{@link ParkingUser} repository access</p>
 *
 * @author imuliar
 * @since 0.0.1
 */
@Repository
@Transactional
public interface ParkingUserRepository extends JpaRepository<ParkingUser, Long> {

    Optional<ParkingUser> findByTelegramUserId(Integer telegramUserId);

    @Query("SELECT user FROM ParkingUser user " +
            "JOIN user.pollingProfile profile " +
            "WHERE (:now >= profile.startPollingHour AND :now <= (profile.startPollingHour + 2)) " +
            "AND (profile.lastAnswerReceived <> :today OR profile.lastAnswerReceived IS NULL)" )
    List<ParkingUser> findByStartPollingHour(@Param("now") int currentHour, @Param("today") LocalDate today);

    @Query("SELECT b.user FROM Booking b " +
            "JOIN b.slot slot " +
            "WHERE slot.number = :slotNumber AND b.date = :date")
    Optional<ParkingUser> findBooker(@Param("slotNumber") String slotNumber, @Param("date") LocalDate date);

    @Query("SELECT parkingUser FROM Reservation reservation " +
            "JOIN reservation.user parkingUser " +
            "JOIN reservation.slot slot " +
            "WHERE slot.number = :slotNumber AND (slot.id, reservation.priority) " +
            "IN " +
            "(SELECT s.id, MIN(r.priority) FROM Reservation r " +
            "JOIN r.slot s " +
            "JOIN r.user u " +
            "WHERE u.id NOT IN ( " +
            "SELECT pu.id FROM VacantPeriod vacantPeriod " +
            "JOIN vacantPeriod.user pu " +
            "WHERE vacantPeriod.periodStart <= :date AND :date <= vacantPeriod.periodEnd) " +
            "GROUP BY s)")
    Optional<ParkingUser> findEngagingOwner(@Param("slotNumber") String slotNumber, @Param("date") LocalDate date);
}
