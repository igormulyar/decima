package com.imuliar.decima.entity;

import org.springframework.util.Assert;

import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * <p>Represents the period of time, when parking user won't use his parking place</p>
 *
 * @author imuliar
 * @since 0.0.1
 */
@Entity
public class VacantPeriod extends EntityFrame {

    @Column(nullable = false)
    private Integer userId;

    @Column(nullable = false)
    private LocalDate periodStart;

    @Column(nullable = false)
    private LocalDate periodEnd;

    public VacantPeriod() {
    }

    public VacantPeriod(Integer userId, LocalDate periodStart, LocalDate periodEnd) {
        this.userId = userId;
        this.periodStart = periodStart;
        this.periodEnd = periodEnd;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public LocalDate getPeriodStart() {
        return periodStart;
    }

    public void setPeriodStart(LocalDate periodStart) {
        this.periodStart = periodStart;
    }

    public LocalDate getPeriodEnd() {
        return periodEnd;
    }

    public void setPeriodEnd(LocalDate periodEnd) {
        this.periodEnd = periodEnd;
    }
}
