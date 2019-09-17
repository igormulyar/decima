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
            "WHERE :now >= profile.startPollingHour AND :now <= (profile.startPollingHour + 2) " +
            "AND profile.lastAnswerReceived != :today" )
    List<ParkingUser> findByStartPollingHour(@Param("now") int currentHour, @Param("today") LocalDate today);
}
