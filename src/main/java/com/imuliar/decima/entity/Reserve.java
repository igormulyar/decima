package com.imuliar.decima.entity;

import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.OneToOne;

/**
 * <p>Represent the permanent slot reservation for particular user</p>
 *
 * @author imuliar
 * @since 0.0.1
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Reserve extends EntityFrame {

    @OneToOne
    @JoinColumn(name = "USER_ID", foreignKey = @ForeignKey(name = "FK_RESERVE_PARKING_USER"))
    private ParkingUser user;

    @OneToOne
    @JoinColumn(name = "SLOT_ID", foreignKey = @ForeignKey(name = "FK_RESERVE_SLOT"))
    private Slot slot;
}
