package com.baodanyun.websocket.service;

import com.baodanyun.websocket.exception.BusinessException;
import org.springframework.stereotype.Service;

/**
 * Created by liaowuhen on 2016/11/11.
 */
@Service
public interface UserCacheServer {

    /**
     * 维护发送消息目的地
     */
    boolean addVisitorCustomerOpenId(String openId, String to) throws BusinessException;

    String getCustomerIdByVisitorOpenId(String openId) throws BusinessException;

}
