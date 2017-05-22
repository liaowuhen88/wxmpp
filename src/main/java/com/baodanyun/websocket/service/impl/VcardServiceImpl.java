package com.baodanyun.websocket.service.impl;

import com.baodanyun.websocket.bean.user.AbstractUser;
import com.baodanyun.websocket.core.common.Common;
import com.baodanyun.websocket.exception.BusinessException;
import com.baodanyun.websocket.service.VcardService;
import com.baodanyun.websocket.service.XmppServer;
import com.baodanyun.websocket.util.JSONUtil;
import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smackx.iqregister.AccountManager;
import org.jivesoftware.smackx.vcardtemp.VCardManager;
import org.jivesoftware.smackx.vcardtemp.packet.VCard;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by liaowuhen on 2016/11/25.
 */
@Service("vcardService")
public class VcardServiceImpl implements VcardService {
    private static final Map<String, AbstractUser> vcards = new ConcurrentHashMap<>();
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private XmppServer xmppServer;

    /**
     * 获取客服信息
     *
     * @param
     * @return
     */

    private <T> T getBaseVCard(String key, String jid, String xmppId, Class<T> c) throws Exception {
        VCard vcard = loadVCard(xmppId, jid);
        return getBaseVCard(key, vcard, c);

    }

    private <T> T getBaseVCard(String jid, String xmppId, Class<T> c) throws Exception {
        return getBaseVCard(Common.userVcard, jid, xmppId, c);

    }

    /**
     * 获取客服信息
     *
     * @param
     * @return
     */

    private <T> T getBaseVCard(String key, VCard vcard, Class<T> c) throws Exception {
        if (vcard == null) {
            return null;
        } else {
            String js = vcard.getField(key);
            logger.info(js);
            return JSONUtil.toObject(c, js);
        }
    }

    @Override
    public <T> T getVCardUser(String jid, String xmppId, Class<T> c) throws Exception {
        AbstractUser vu = vcards.get(jid);
        if (null == vu) {
            T t = getBaseVCard(jid, xmppId, c);
            if (null != t) {
                vcards.put(jid, (AbstractUser) t);
                return t;
            }

        }
        return (T) new AbstractUser();
    }

    /**
     * 更新VCard
     *
     * @param jid
     * @param key
     * @param cu
     * * @return
     */

    @Override
    public boolean updateBaseVCard(String jid, String key, AbstractUser cu) {
        try {
            VCard vcard = new VCard();
            vcard.setField(key, JSONUtil.toJson(cu));

            saveVCard(jid, vcard);

            vcards.put(jid, cu);

            return true;
        } catch (Exception e) {
            logger.error("update vcard error", e);
        }
        return false;
    }

    @Override
    public boolean updateBaseVCard(String jid, AbstractUser cu) {

        return updateBaseVCard(jid, Common.userVcard, cu);
    }

    private VCard loadVCard(String xmppId, String jid) throws BusinessException {
        try {
            if (xmppServer.isAuthenticated(xmppId)) {
                VCardManager vm = VCardManager.getInstanceFor(xmppServer.getXMPPConnectionAuthenticated(xmppId));
                if (vm != null) {
                    return vm.loadVCard(jid);
                }
            }
        } catch (Exception e) {
            logger.error("", e);
            throw new BusinessException("loadVCard error:" + xmppId + "------" + jid);
        }
        return null;
    }

    private void saveVCard(String jid, VCard vcard) throws SmackException.NotConnectedException, XMPPException.XMPPErrorException, SmackException.NoResponseException, BusinessException {
        if (xmppServer.isAuthenticated(jid)) {
            VCardManager vm = VCardManager.getInstanceFor(xmppServer.getXMPPConnectionAuthenticated(jid));
            if (vm != null) {
                vm.saveVCard(vcard);
            }
        }
    }


    /**
     * 修改密码
     *
     * @return
     */

    @Override
    public boolean changePassword(String jid, String pwd) throws SmackException.NotConnectedException, XMPPException.XMPPErrorException, SmackException.NoResponseException, BusinessException {
        AbstractXMPPConnection xmppConnection = xmppServer.getXMPPConnectionAuthenticated(jid);
        if (xmppConnection != null) {

            AccountManager.getInstance(xmppConnection).changePassword(pwd);
            return true;
        } else {
            throw new BusinessException("xmppConnection is not Authenticated");
        }
    }
}
