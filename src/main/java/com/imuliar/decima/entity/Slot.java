package com.imuliar.decima.entity;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * <p>Represents the batch of {@link ParkingPlace}s, which have single access gateway</p>
 *
 * @author imuliar
 * @since 0.0.1
 */
@Entity
public class Slot extends EntityFrame {

    @Column(nullable = false)
    private String number;

    @Column(nullable = false)
    private Integer maxLoading;

    public Slot() {
    }

    public Slot(String number, Integer maxLoading) {
        this.number = number;
        this.maxLoading = maxLoading;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Integer getMaxLoading() {
        return maxLoading;
    }

    public void setMaxLoading(Integer maxLoading) {
        this.maxLoading = maxLoading;
    }
}
