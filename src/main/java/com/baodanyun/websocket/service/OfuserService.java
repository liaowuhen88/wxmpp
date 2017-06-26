package com.baodanyun.websocket.service;

import com.baodanyun.websocket.exception.BusinessException;

/**
 * Created by liaowuhen on 2017/6/26.
 */
public interface OfuserService {
    boolean checkOfUser(String userName, String password) throws BusinessException;
}
