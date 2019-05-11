package com.imuliar.decima.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;

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

    private String secondName;

    @Enumerated(STRING)
    private Role role;
}
