package com.imuliar.decima.entity;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * <p>Some settings for slot owner polling </p>
 *
 * @author imuliar
 * @since 0.0.1
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
