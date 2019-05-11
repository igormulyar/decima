package com.imuliar.decima.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;

/**
 * <p>Entity represents single parking slot for one car</p>
 *
 * @author imuliar
 * @since 0.0.1
 */
@Entity
@Data
@NoArgsConstructor
public class ParkingPlace extends EntityFrame {

    private Integer parkingLabel;

    private Integer internalLabel;

}
