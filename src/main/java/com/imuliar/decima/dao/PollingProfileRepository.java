package com.imuliar.decima.dao;

import com.imuliar.decima.entity.PollingProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * <p>DAO for {@link PollingProfile} entity</p>
 *
 * @author imuliar
 * @since 0.0.1
 */
@Repository
public interface PollingProfileRepository extends JpaRepository<PollingProfile, Long> {
}
