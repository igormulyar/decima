package com.imuliar.decima.service.impl;

import com.imuliar.decima.dao.ReserveRepository;
import com.imuliar.decima.entity.ParkingUser;
import com.imuliar.decima.service.ResponseStrategy;
import com.imuliar.decima.service.ResponseStrategyFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * <p>Resolves appropriate strategy for processing update</p>
 *
 * @author imuliar
 * @since 0.0.1
 */
@Service
public class ResponseStrategyFactoryImpl implements ResponseStrategyFactory {

    @Value("${decima.groupChatId}")
    private Long groupChatId;

    @Qualifier("ordinaryResponseStrategy")
    private ResponseStrategy ordinaryResponseStrategy;

    @Qualifier("slotOwnerResponseStrategy")
    private ResponseStrategy slotOwnerResponseStrategy;

    @Qualifier("groupChatResponseStrategy")
    private ResponseStrategy groupChatResponseStrategy;

    private ReserveRepository reserveRepository;

    @Autowired
    public ResponseStrategyFactoryImpl(ResponseStrategy ordinaryResponseStrategy, ResponseStrategy slotOwnerResponseStrategy,
                                       ResponseStrategy groupChatResponseStrategy, ReserveRepository reserveRepository) {
        this.ordinaryResponseStrategy = ordinaryResponseStrategy;
        this.slotOwnerResponseStrategy = slotOwnerResponseStrategy;
        this.groupChatResponseStrategy = groupChatResponseStrategy;
        this.reserveRepository = reserveRepository;
    }

    @Override
    public ResponseStrategy getStrategy(ParkingUser parkingUser, Long chatId) {
        if(chatId.longValue() == groupChatId.longValue()){
            return groupChatResponseStrategy;
        }
        return reserveRepository.findByUser(parkingUser).isPresent()
                ? slotOwnerResponseStrategy
                : ordinaryResponseStrategy;
    }
}
