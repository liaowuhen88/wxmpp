package com.baodanyun.websocket.service.impl;

import com.baodanyun.websocket.service.CacheService;
import org.springframework.stereotype.Service;

@Service("nullMemCacheServiceImpl")
public class NullMemCacheServiceImpl implements CacheService {

    @Override
    public boolean set(String key, Object value, int second) {
        return false;
    }

    @Override
    public boolean setThreeSecond(String key, Object value) {
        return false;
    }

    @Override
    public boolean setTenSecond(String key, Object value) {
        return false;
    }

    @Override
    public boolean setOneMin(String key, Object value) {
        return false;
    }

    @Override
    public boolean setThreeMin(String key, Object value) {
        return false;
    }

    @Override
    public boolean setFiveMin(String key, Object value) {
        return false;
    }

    @Override
    public boolean setHalfHour(String key, Object value) {
        return false;
    }

    @Override
    public boolean setOneDay(String key, Object value) {
        return false;
    }

    @Override
    public boolean setOneWeek(String key, Object value) {
        return false;
    }

    @Override
    public boolean setOneMonth(String key, Object value) {
        return false;
    }

    @Override
    public Object get(String key) {
        return null;
    }

    @Override
    public boolean remove(String key) {
        return false;
    }

    @Override
    public boolean clear() {
        return false;
    }

    @Override
    public boolean isRun() {
        return false;
    }
}
