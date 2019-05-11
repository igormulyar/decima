package com.imuliar.decima.dao;

import com.imuliar.decima.entity.ParkingPlace;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * <p>{@link ParkingPlace} repository acess</p>
 *
 * @author imuliar
 * @since 0.0.1
 */
public interface ParkingPlaceRepository extends JpaRepository<ParkingPlace, Long> {
}
