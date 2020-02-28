package com.imuliar.decima.entity;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * <p>Additional info about patrician</p>
 *
 * @author imuliar
 * @since 0.0.1
 */
@Entity
public class UserDetails extends EntityFrame {

    @Column(nullable = false)
    private Integer telegramId;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    private String phoneNumber;

    public UserDetails() {
    }

    public Integer getTelegramId() {
        return telegramId;
    }

    public void setTelegramId(Integer telegramId) {
        this.telegramId = telegramId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
