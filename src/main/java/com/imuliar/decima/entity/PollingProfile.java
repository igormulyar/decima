package com.imuliar.decima.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * //TODO add description <p></p>
 *
 * @author imuliar
 * @since //TODO specify version
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PollingProfile extends EntityFrame {

    @Column(nullable = false)
    private Integer startPollingHour;
}
