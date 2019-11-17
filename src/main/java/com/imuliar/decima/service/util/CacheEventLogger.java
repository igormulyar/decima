package com.imuliar.decima.service.util;

import org.ehcache.event.CacheEvent;
import org.ehcache.event.CacheEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>Cache event logger</p>
 *
 * @author imuliar
 * @since 0.0.1
 */
public class CacheEventLogger implements CacheEventListener<Object, Object> {

    private static final Logger LOG = LoggerFactory.getLogger(CacheEventLogger.class);

    @Override
    public void onEvent(CacheEvent cacheEvent) {
        LOG.info("Cache event = {}, Key = {},  Old value = {}, New value = {}", cacheEvent.getType(),
                cacheEvent.getKey(), cacheEvent.getOldValue(), cacheEvent.getNewValue());
    }
}

