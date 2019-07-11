package com.imuliar.decima.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.EnumType.STRING;

/**
 * <p>Represents the bot user who is allowed periodEnd use parking</p>
 *
 * @author imuliar
 * @since 0.0.1
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ParkingUser extends EntityFrame {

    @Column(unique = true)
    private Integer telegramUserId;

    private String telegramUsername;

    private String firstName;

    private String lastName;

    @JoinColumn(name = "POLLING_PROFILE_ID",
            foreignKey = @ForeignKey(name = "FK_PARKING_USER_POLLING_PROFILE"))
    private PollingProfile pollingProfile;
}
