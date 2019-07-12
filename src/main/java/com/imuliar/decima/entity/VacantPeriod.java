package com.imuliar.decima.entity;

import java.time.LocalDate;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

/**
 * <p>Represents the period of time, when parking user with role PATRICIAN won't use his parking place</p>
 *
 * @author imuliar
 * @since 0.0.1
 */
@Entity
public class VacantPeriod extends EntityFrame {

    private LocalDate periodStart;

    private LocalDate periodEnd;

    @ManyToOne
    private Slot slot;

    public VacantPeriod() {
    }

    public VacantPeriod(LocalDate periodStart, LocalDate periodEnd, Slot slot) {
        this.periodStart = periodStart;
        this.periodEnd = periodEnd;
        this.slot = slot;
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

    public Slot getSlot() {
        return slot;
    }

    public void setSlot(Slot slot) {
        this.slot = slot;
    }
}
