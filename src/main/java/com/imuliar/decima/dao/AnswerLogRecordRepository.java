package com.imuliar.decima.dao;

import com.imuliar.decima.entity.AnswerLogRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * <p>Repository for {@link AnswerLogRecord}</p>
 *
 * @author imuliar
 * @since 0.0.1
 */
@Repository
public interface AnswerLogRecordRepository extends JpaRepository<AnswerLogRecord, Long> {
}
