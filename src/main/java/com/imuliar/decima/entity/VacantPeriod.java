package com.imuliar.decima.entity;

import java.time.LocalDate;
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

    private LocalDate periodStart;

    private LocalDate periodEnd;

    @ManyToOne
    @JoinColumn(name="USER_ID", nullable = false)
    private ParkingUser user;

    public VacantPeriod() {
    }

    public VacantPeriod(LocalDate periodStart, LocalDate periodEnd, ParkingUser user) {
        this.periodStart = periodStart;
        this.periodEnd = periodEnd;
        this.user = user;
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

    public ParkingUser getUser() {
        return user;
    }

    public void setUser(ParkingUser user) {
        this.user = user;
    }
}
