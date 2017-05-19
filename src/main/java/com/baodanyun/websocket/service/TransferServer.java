package com.baodanyun.websocket.service;

import com.baodanyun.websocket.bean.user.Customer;
import com.baodanyun.websocket.bean.user.Visitor;
import com.baodanyun.websocket.exception.BusinessException;
import com.baodanyun.websocket.model.Transferlog;

/**
 * Created by liaowuhen on 2016/11/11.
 */
public interface TransferServer {

     boolean changeVisitorTo(Transferlog tm, Visitor visitor) throws BusinessException;

     boolean changeVisitorTo(Transferlog tm, Visitor visitor, Customer customer) throws BusinessException;

}
