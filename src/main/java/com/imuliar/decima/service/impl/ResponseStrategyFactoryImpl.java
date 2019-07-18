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

    private ResponseStrategy ordinaryResponseStrategy;

    private ResponseStrategy slotOwnerResponseStrategy;

    private ResponseStrategy groupChatResponseStrategy;

    private ReserveRepository reserveRepository;

    @Autowired
    public ResponseStrategyFactoryImpl(@Qualifier("ordinaryResponseStrategy") ResponseStrategy ordinaryResponseStrategy,
                                       @Qualifier("slotOwnerResponseStrategy") ResponseStrategy slotOwnerResponseStrategy,
                                       @Qualifier("groupChatResponseStrategy") ResponseStrategy groupChatResponseStrategy,
                                       ReserveRepository reserveRepository) {
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
