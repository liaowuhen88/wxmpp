package com.baodanyun.websocket.service.impl;

import com.baodanyun.websocket.core.common.SecondConstant;
import com.baodanyun.websocket.service.CacheService;
import com.whalin.MemCached.MemCachedClient;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Created by liaowuhen on 2017/3/13.
 */
@Service
public class MemCacheServiceImpl implements CacheService{

    private static final Logger logger = Logger.getLogger(MemCacheServiceImpl.class);

    @Autowired
    private MemCachedClient memCachedClient;

    @Override
    public boolean set(String key, Object value, int second) {
        boolean flag = false;
        if(null != value){
            Date exp = new Date(Long.valueOf(second )* 1000);
            //logger.info(DateUtils.format(exp));
            try {
                flag = memCachedClient.set(key, value, exp);
            } catch (Exception e) {
                logger.error(e.getMessage(),e);
            }
        }else {
            logger.info("value is null");
        }
        return flag;
    }

    @Override
    public boolean setThreeSecond(String key, Object value) {
        return set(key, value, SecondConstant.THREE_SECONDS);
    }
    @Override
    public boolean setTenSecond(String key, Object value) {
        return set(key, value, SecondConstant.TEN_SECONDS);
    }
    @Override
    public boolean setOneMin(String key, Object value) {
        return set(key, value, SecondConstant.ONE_MIN_SECONDS);
    }
    @Override
    public boolean setThreeMin(String key, Object value) {
        return set(key, value, SecondConstant.THREE_MIN_SECONDS);
    }
    @Override
    public boolean setFiveMin(String key, Object value) {
        return set(key, value, SecondConstant.FIVE_MIN_SECONDS);
    }
    @Override
    public boolean setHalfHour(String key, Object value) {
        return set(key, value, SecondConstant.HALF_HOUR_SECONDS);
    }
    @Override
    public boolean setOneDay(String key, Object value) {
        return set(key, value, SecondConstant.ONE_DAY_SECONDS);
    }
    @Override
    public boolean setOneWeek(String key, Object value) {
        return set(key, value, SecondConstant.ONE_WEEK_SECONDS);
    }
    @Override
    public boolean setOneMonth(String key, Object value) {
        return set(key, value, SecondConstant.ONE_MONTH_SECONDS);
    }
    @Override
    public Object get(String key) {
        return memCachedClient.get(key);
    }
    @Override
    public boolean remove(String key) {
        return memCachedClient.delete(key);
    }
    @Override
    public boolean clear() {
        return memCachedClient.flushAll();
    }
    @Override
    public boolean isRun() {
        return true;
    }

}
