package com.imuliar.decima.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * //TODO add description <p></p>
 *
 * @author imuliar
 * @since //TODO specify version
 */
@Entity
public class PollingProfile extends EntityFrame {

    @Column(nullable = false)
    private Integer startPollingHour;

    public PollingProfile() {
    }

    public PollingProfile(Integer startPollingHour) {
        this.startPollingHour = startPollingHour;
    }

    public Integer getStartPollingHour() {
        return startPollingHour;
    }

    public void setStartPollingHour(Integer startPollingHour) {
        this.startPollingHour = startPollingHour;
    }
}
