package com.imuliar.decima.entity;

import java.time.LocalDate;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    @ManyToOne
    @JoinColumn(name = "USER_ID", foreignKey = @ForeignKey(name = "FK_BOOKING_PARKING_USER"))
    private ParkingUser user;

    @ManyToOne
    @JoinColumn(name = "SLOT_ID", foreignKey = @ForeignKey(name = "FK_BOOKING_SLOT"))
    private Slot slot;

    private LocalDate date;
}
