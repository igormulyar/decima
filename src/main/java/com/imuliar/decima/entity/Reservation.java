package com.imuliar.decima.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToOne;

/**
 * <p>Represent the permanent slot reservation for particular user</p>
 *
 * @author imuliar
 * @since 0.0.1
 */
@Entity
public class Reservation extends EntityFrame {

    @OneToOne
    private ParkingUser user;

    @OneToOne
    private Slot slot;

    public Reservation() {
    }

    public Reservation(ParkingUser user, Slot slot) {
        this.user = user;
        this.slot = slot;
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
}
