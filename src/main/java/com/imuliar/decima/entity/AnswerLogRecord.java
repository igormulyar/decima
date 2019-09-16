package com.imuliar.decima.entity;

import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * <p>Store info about sending answer for a poll question</p>
 * <p>If the record in DB exist that means that specified user has sent the answer to the bot's poll question at specified date</p>
 *
 * @author imuliar
 * @since /0.0.1
 */
@Entity
public class AnswerLogRecord extends EntityFrame{

    @ManyToOne(optional = false)
    @JoinColumn(name = "USER_ID")
    private ParkingUser user;

    @Column(nullable = false)
    private LocalDate date;

    public ParkingUser getUser() {
        return user;
    }

    public void setUser(ParkingUser user) {
        this.user = user;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
