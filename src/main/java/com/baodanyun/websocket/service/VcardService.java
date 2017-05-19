package com.baodanyun.websocket.service;

import com.baodanyun.websocket.bean.user.VCardUser;
import com.baodanyun.websocket.exception.BusinessException;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.springframework.stereotype.Service;

/**
 * Created by liaowuhen on 2016/11/25.
 */
@Service
public interface VcardService {

    VCardUser getVCardUser(String jid, String xmppId) throws Exception;

    //<T> T getBaseVCard(String key, String jid, String xmppId, Class<T> c) throws Exception;

    //<T> T getBaseVCard(String jid, String xmppId, Class<T> c) throws Exception;

    boolean updateBaseVCard(String jid, String key, VCardUser cu);

    boolean updateBaseVCard(String jid, VCardUser cu);

    boolean changePassword(String jid, String pwd) throws SmackException.NotConnectedException, XMPPException.XMPPErrorException, SmackException.NoResponseException, BusinessException;

}
