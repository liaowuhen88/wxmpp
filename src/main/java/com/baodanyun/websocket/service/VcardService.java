package com.baodanyun.websocket.service;

import com.baodanyun.websocket.bean.user.AbstractUser;
import com.baodanyun.websocket.exception.BusinessException;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.springframework.stereotype.Service;

/**
 * Created by liaowuhen on 2016/11/25.
 */
@Service
public interface VcardService {

    <T> T getVCardUser(String jid, String xmppId, Class<T> c) throws Exception;

    boolean updateBaseVCard(String jid, String key, AbstractUser cu);

    boolean updateBaseVCard(String jid, AbstractUser cu);

    boolean changePassword(String jid, String pwd) throws SmackException.NotConnectedException, XMPPException.XMPPErrorException, SmackException.NoResponseException, BusinessException;

}
