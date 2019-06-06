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

    @Query("SELECT neighbour FROM Booking booking " +
            "JOIN booking.user neighbour " +
            "WHERE booking.slot IN " +
            "(SELECT slot FROM Booking b " +
            "JOIN b.slot slot " +
            "JOIN b.user user " +
            "WHERE user.id = :id " +
            "AND b.date = :date) " +
            "AND neighbour <> :id " +
            "AND booking.date = :date")
    List<ParkingUser> findNeighbours(@Param("id") Long id, @Param("date") LocalDate date);
}
