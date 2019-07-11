package com.imuliar.decima.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

/**
 * <p>Represents the record about booking a slot by user for specified date</p>
 *
 * @author imuliar
 * @since 0.0.1
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Booking extends EntityFrame {

    @OneToOne(optional = false)
    @JoinColumn(name = "PARKING_USER_ID",
            foreignKey = @ForeignKey(name = "FK_BOOKING_PARKING_USER"))
    private ParkingUser user;

    @ManyToOne(optional = false)
    @JoinColumn(name = "SLOT_ID", foreignKey = @ForeignKey(name = "FK_BOOKING_SLOT"))
    private Slot slot;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false)
    private Integer priority;
}
