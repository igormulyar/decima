package com.imuliar.decima.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import java.time.LocalDate;

/**
 * <p>Represent the permanent slot reservation for particular user</p>
 *
 * @author imuliar
 * @since 0.0.1
 */
@Entity
public class Reservation extends EntityFrame {

    @Column(nullable = false)
    private Integer userId;

    @OneToOne(optional = false)
    private Slot slot;

    private LocalDate lastPollTimestamp;

    public Reservation() {
    }

    public Reservation(Integer userId, Slot slot) {
        this.userId = userId;
        this.slot = slot;
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

    public LocalDate getLastPollTimestamp() {
        return lastPollTimestamp;
    }

    public void setLastPollTimestamp(LocalDate lastPollTimestamp) {
        this.lastPollTimestamp = lastPollTimestamp;
    }
}
