package com.imuliar.decima.entity;

import javax.persistence.*;
import java.time.LocalDate;

/**
 * <p>Represents the record about booking a slot by user for specified date</p>
 *
 * @author imuliar
 * @since 0.0.1
 */
@Entity
public class Booking extends EntityFrame {

    @Column(nullable = false)
    private Integer userId;

    @ManyToOne(optional = false)
    @JoinColumn(name = "SLOT_ID", foreignKey = @ForeignKey(name = "FK_BOOKING_SLOT"))
    private Slot slot;

    @Column(nullable = false)
    private LocalDate date;

    public Booking() {
    }

    public Booking(Integer userId, Slot slot, LocalDate date) {
        this.userId = userId;
        this.slot = slot;
        this.date = date;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Slot getSlot() {
        return slot;
    }

    public void setSlot(Slot slot) {
        this.slot = slot;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
