package com.imuliar.decima.entity;

import javax.persistence.Entity;
import javax.persistence.OneToOne;

/**
 * <p>Represent the permanent slot reservation for particular user</p>
 *
 * @author imuliar
 * @since 0.0.1
 */
@Entity
public class Reserve extends EntityFrame {

    @OneToOne
    private ParkingUser user;

    @OneToOne
    private Slot slot;

    /**
     * <p>Represents the priority level of reservation</p>
     * <p> 0 is the highest priority.</p>
     */
    private Integer priority = 0;

    public Reserve() {
    }

    public Reserve(ParkingUser user, Slot slot) {
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

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }
}
