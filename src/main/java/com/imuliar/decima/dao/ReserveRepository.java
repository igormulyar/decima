package com.imuliar.decima.dao;

import com.imuliar.decima.entity.ParkingUser;
import com.imuliar.decima.entity.Reserve;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * //TODO add description <p></p>
 *
 * @author imuliar
 * @since //TODO specify version
 */
public interface ReserveRepository extends JpaRepository<Reserve, Long> {

    Optional<Reserve> findByUser(ParkingUser parkingUser);
}
