package com.imuliar.decima.entity;

import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

/**
 * <p>Represents the record about booking a slot by user for specified date</p>
 *
 * @author imuliar
 * @since 0.0.1
 */
@Entity
public class Booking extends EntityFrame {

    @OneToOne(optional = false)
    @JoinColumn(name = "PARKING_USER_ID",
            foreignKey = @ForeignKey(name = "FK_BOOKING_PARKING_USER"))
    private ParkingUser user;

    @ManyToOne(optional = false)
    @JoinColumn(name = "SLOT_ID", foreignKey = @ForeignKey(name = "FK_BOOKING_SLOT"))
    private Slot slot;

    @Column(nullable = false)
    private LocalDate date;

    public Booking() {
    }

    public Booking(ParkingUser user, Slot slot, LocalDate date) {
        this.user = user;
        this.slot = slot;
        this.date = date;
    }

    public ParkingUser getUser() {
        return user;
    }

    public void setUser(ParkingUser user) {
        this.user = user;
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
