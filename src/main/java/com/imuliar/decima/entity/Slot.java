package com.imuliar.decima.entity;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * <p>Represents the place for parking the car</p>
 *
 * @author imuliar
 * @since 0.0.1
 */
@Entity
public class Slot extends EntityFrame {

    @Column(nullable = false)
    private String number;

    public Slot() {
    }

    public Slot(String number) {
        this.number = number;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
