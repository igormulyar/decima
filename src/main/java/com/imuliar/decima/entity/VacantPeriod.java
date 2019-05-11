package com.imuliar.decima.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import java.time.LocalDate;

/**
 * <p>Represents the period of time, when parking user with role PATRICIAN won't use his parking place</p>
 *
 * @author imuliar
 * @since 0.0.1
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VacantPeriod extends EntityFrame {

    private LocalDate periodStart;

    private LocalDate periodEnd;

    @ManyToOne
    private Slot slot;
}
