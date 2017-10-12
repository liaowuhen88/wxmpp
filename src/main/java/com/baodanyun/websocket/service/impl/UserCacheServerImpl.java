package com.baodanyun.websocket.service.impl;

import com.baodanyun.websocket.exception.BusinessException;
import com.baodanyun.websocket.service.CacheService;
import com.baodanyun.websocket.service.UserCacheServer;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by liaowuhen on 2016/11/11.
 */
@Service
public class UserCacheServerImpl implements UserCacheServer {
    private static final Map<String, String> dest = new ConcurrentHashMap<>();

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private CacheService cacheService;

    /**
     * 维护发送消息目的地
     */
    @Override
    public boolean addVisitorCustomerOpenId(String openId, String to) throws BusinessException {

        if (StringUtils.isEmpty(openId)) {
            throw new BusinessException("openId is null");
        }

        if (StringUtils.isEmpty(to)) {
            throw new BusinessException("to is null");
        }

        dest.put(openId, to);

        Map map = (Map) cacheService.get(openId);
        if (null == map) {
            map = new HashMap();
        }

        map.put(openId, to);

        return cacheService.setOneWeek(openId, map);
    }

    /**
     * 获取发送地址
     */
    @Override
    public String getCustomerIdByVisitorOpenId(String openId) throws BusinessException {
        if (StringUtils.isEmpty(openId)) {
            throw new BusinessException("openId is null");
        }

        if (null != dest.get(openId)) {
            return dest.get(openId);
        } else {
            Map map = (Map) cacheService.get(openId);
            if (null != map) {
                return (String) map.get(openId);
            }
        }
        return null;
    }

}
