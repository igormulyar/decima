package com.imuliar.decima.service.util;

import java.lang.reflect.Method;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.User;

/**
 * <p>Custom Cache kay generator for userAccess cache</p>
 *
 * @author imuliar
 * @since 0.0.1
 */
@Service
public class UserAccessCacheKeyGenerator implements KeyGenerator {

    @Override
    public Object generate(Object target, Method method, Object... params) {
        User userParam = (User) params[0];
        return userParam.getId();
    }
}
