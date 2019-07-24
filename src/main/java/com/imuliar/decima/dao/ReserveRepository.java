package com.imuliar.decima.dao;

import com.imuliar.decima.entity.ParkingUser;
import com.imuliar.decima.entity.Reserve;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * //TODO add description <p></p>
 *
 * @author imuliar
 * @since //TODO specify version
 */
public interface ReserveRepository extends JpaRepository<Reserve, Long> {

    Optional<Reserve> findByUser(ParkingUser parkingUser);

    @Query("SELECT r FROM Reserve r " +
            "JOIN r.user u " +
            "WHERE u.telegramUserId = :telegramUserId")
    Optional<Reserve> findByTelegramUserId(@Param("telegramUserId") Integer telegramUserId);
}
