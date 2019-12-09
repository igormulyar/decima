package com.imuliar.decima.entity;

import java.time.LocalDate;
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
    private Integer startPollingHour = 8;

    private LocalDate lastAnswerReceived;

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

    public LocalDate getLastAnswerReceived() {
        return lastAnswerReceived;
    }

    public void setLastAnswerReceived(LocalDate lastAnswerReceived) {
        this.lastAnswerReceived = lastAnswerReceived;
    }
}
