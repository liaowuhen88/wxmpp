package com.baodanyun.websocket.service;

public interface CacheService {
    boolean set(String key, Object value, int second);
    boolean setThreeSecond(String key, Object value);
    boolean setTenSecond(String key, Object value);
    boolean setOneMin(String key, Object value);
    boolean setThreeMin(String key, Object value);
    boolean setFiveMin(String key, Object value);
    boolean setHalfHour(String key, Object value);
    boolean setOneDay(String key, Object value);
    boolean setOneWeek(String key, Object value);
    boolean setOneMonth(String key, Object value);
    Object get(String key);
    boolean remove(String key);
    boolean clear();
    boolean isRun();
}
