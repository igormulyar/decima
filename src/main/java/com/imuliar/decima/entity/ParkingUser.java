package com.imuliar.decima.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

/**
 * <p>Represents the bot user who is allowed periodEnd use parking</p>
 *
 * @author imuliar
 * @since 0.0.1
 */
@Entity
public class ParkingUser extends EntityFrame {

    @Column(unique = true)
    private Integer telegramUserId;

    private String telegramUsername;

    private String firstName;

    private String lastName;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "POLLING_PROFILE_ID",
            foreignKey = @ForeignKey(name = "FK_PARKING_USER_POLLING_PROFILE"))
    private PollingProfile pollingProfile;

    public ParkingUser() {
    }

    public ParkingUser(Integer telegramUserId, String telegramUsername, String firstName, String lastName, PollingProfile pollingProfile) {
        this.telegramUserId = telegramUserId;
        this.telegramUsername = telegramUsername;
        this.firstName = firstName;
        this.lastName = lastName;
        this.pollingProfile = pollingProfile;
    }

    public Integer getTelegramUserId() {
        return telegramUserId;
    }

    public void setTelegramUserId(Integer telegramUserId) {
        this.telegramUserId = telegramUserId;
    }

    public String getTelegramUsername() {
        return telegramUsername;
    }

    public void setTelegramUsername(String telegramUsername) {
        this.telegramUsername = telegramUsername;
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

    public PollingProfile getPollingProfile() {
        return pollingProfile;
    }

    public void setPollingProfile(PollingProfile pollingProfile) {
        this.pollingProfile = pollingProfile;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("user: ");
        if(telegramUsername != null){
            builder.append(telegramUsername).append(" ");
        }
        if(firstName != null){
            builder.append(firstName).append(" ");
        }
        if(lastName != null){
            builder.append(lastName).append(" ");
        }
        return builder.toString();
    }
}
