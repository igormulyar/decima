package com.imuliar.decima.service.impl;

import com.imuliar.decima.dao.ParkingUserRepository;
import com.imuliar.decima.entity.ParkingUser;
import com.imuliar.decima.entity.PollingProfile;
import com.imuliar.decima.service.ParkingUserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.telegram.telegrambots.meta.api.objects.User;

import javax.annotation.Nonnull;
import java.util.Optional;

/**
 * <p>Service for managing {@link ParkingUser} data</p>
 *
 * @author imuliar
 * @since 0.0.1
 */
@Service
@AllArgsConstructor
public class ParkingUserServiceImp implements ParkingUserService {

    @Autowired
    private ParkingUserRepository userRepository;

    @Cacheable(cacheNames = "parkingUser", key = "#p0")
    @Override
    @Nonnull
    public ParkingUser findOrCreateParkingUser(@Nonnull User user) {
        Assert.notNull(user, "user is null");
        return userRepository.findByTelegramUserId(user.getId())
                .orElseGet(() -> userRepository.save(new ParkingUser(user.getId(), user.getUserName(), user.getFirstName(), user.getLastName(), new PollingProfile(8))));
    }
}
