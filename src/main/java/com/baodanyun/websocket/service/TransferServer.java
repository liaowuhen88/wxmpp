package com.baodanyun.websocket.service;

import com.baodanyun.websocket.bean.user.AbstractUser;
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
    /**
     * 转接
     *
     * @param tm
     * @param visitor
     * @return
     * @throws BusinessException
     * @throws XMPPException
     * @throws IOException
     * @throws SmackException
     */
     boolean changeVisitorTo(Transferlog tm, Visitor visitor) throws BusinessException, XMPPException, IOException, SmackException;

    boolean bindVisitor(AbstractUser customer, Visitor visitor) throws BusinessException, XMPPException, IOException, SmackException, InterruptedException;

     boolean changeVisitorTo(Transferlog tm, Visitor visitor, AbstractUser customer) throws BusinessException, XMPPException, IOException, SmackException;

}
