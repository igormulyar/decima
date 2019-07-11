package com.imuliar.decima.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
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

    @OneToOne
    private ParkingUser user;

    @ManyToOne
    private Slot slot;

    private LocalDate date;
}
