package com.baodanyun.websocket.service;

import com.baodanyun.websocket.bean.request.AppKeyVisitorLoginBean;
import com.baodanyun.websocket.bean.user.AppCustomer;
import com.baodanyun.websocket.bean.user.Visitor;
import com.baodanyun.websocket.exception.BusinessException;

/**
 * Created by liaowuhen on 2017/6/30.
 */
public interface AppKeyService {
    /**
     * 根据appkey获取绑定用户信息
     *
     * @param appkey
     * @return
     */
    AppCustomer getCustomerByAppKey(String appkey) throws BusinessException;

    Visitor getVisitor(AppKeyVisitorLoginBean re) throws BusinessException;
}
