package com.imuliar.decima.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * <p>Represents the batch of {@link ParkingPlace}s, which have single access gateway</p>
 *
 * @author imuliar
 * @since 0.0.1
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Slot extends EntityFrame {

    @Column(nullable = false)
    private Integer number;

    @Column(nullable = false)
    private Integer maxLoading;
}
