package com.imuliar.decima.service.impl;

import com.imuliar.decima.dao.ReserveRepository;
import com.imuliar.decima.entity.ParkingUser;
import com.imuliar.decima.service.ResponseStrategy;
import com.imuliar.decima.service.ResponseStrategyFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * //TODO add description <p></p>
 *
 * @author imuliar
 * @since //TODO specify version
 */
@Service
public class ResponseStrategyFactoryImpl implements ResponseStrategyFactory {

    @Qualifier("plebeianResponseStrategy")
    private ResponseStrategy plebeianResponseStrategy;

    @Qualifier("patricianResponseStrategy")
    private ResponseStrategy patricianResponseStrategy;

    private ReserveRepository reserveRepository;

    @Autowired
    public ResponseStrategyFactoryImpl(ResponseStrategy plebeianResponseStrategy, ResponseStrategy patricianResponseStrategy, ReserveRepository reserveRepository) {
        this.plebeianResponseStrategy = plebeianResponseStrategy;
        this.patricianResponseStrategy = patricianResponseStrategy;
        this.reserveRepository = reserveRepository;
    }

    @Override
    public ResponseStrategy getStrategy(ParkingUser parkingUser) {
        return reserveRepository.findByUser(parkingUser).isPresent()
                ? patricianResponseStrategy
                : plebeianResponseStrategy;
    }
}
