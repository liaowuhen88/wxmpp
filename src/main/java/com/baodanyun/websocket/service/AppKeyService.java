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
     * 根据AppKeyVisitorLoginBean获取绑定用户信息
     *
     * @param re
     * @return
     */
    AppCustomer getCustomerByAppKey(AppKeyVisitorLoginBean re, String url) throws BusinessException;

    Visitor getVisitor(AppKeyVisitorLoginBean re, String token) throws BusinessException;

    Visitor getVisitorByToken(String token) throws BusinessException;
}
