package com.imuliar.decima.dao;

import com.imuliar.decima.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
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
}
