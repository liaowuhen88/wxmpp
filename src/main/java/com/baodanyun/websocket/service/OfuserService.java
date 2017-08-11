package com.baodanyun.websocket.service;

import com.baodanyun.websocket.exception.BusinessException;
import com.baodanyun.websocket.model.Ofuser;

/**
 * Created by liaowuhen on 2017/6/26.
 */
public interface OfuserService {
    boolean checkOfUser(String userName, String password) throws BusinessException;

    /**
     * 根据用户名获取用户
     *
     * @param userName 用户名
     * @return xmpp用户
     */
    Ofuser getUserByUsername(String userName);
}
