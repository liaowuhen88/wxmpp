package com.baodanyun.websocket.service;

import com.baodanyun.websocket.bean.user.AbstractUser;
import com.baodanyun.websocket.bean.user.Customer;
import com.baodanyun.websocket.bean.user.Visitor;
import com.baodanyun.websocket.exception.BusinessException;
import com.baodanyun.websocket.model.Transferlog;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;

import java.io.IOException;

/**
 * Created by liaowuhen on 2016/11/11.
 */
public interface TransferServer {

     boolean changeVisitorTo(Transferlog tm, Visitor visitor) throws BusinessException, XMPPException, IOException, SmackException;

     boolean bindVisitor(AbstractUser customerFrom, AbstractUser customer, Visitor visitor);

     boolean changeVisitorTo(Transferlog tm, Visitor visitor, Customer customerFrom, Customer customer) throws BusinessException, XMPPException, IOException, SmackException;

}
