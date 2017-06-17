package com.baodanyun.websocket.service;

import com.baodanyun.websocket.bean.XmppAdapter;
import com.baodanyun.websocket.bean.msg.Msg;
import com.baodanyun.websocket.bean.user.AbstractUser;
import com.baodanyun.websocket.core.listener.InitChatManagerListener;
import com.baodanyun.websocket.core.listener.InitConnectListener;
import com.baodanyun.websocket.dao.OfuserMapper;
import com.baodanyun.websocket.exception.BusinessException;
import com.baodanyun.websocket.model.Ofuser;
import com.baodanyun.websocket.node.ChatNode;
import com.baodanyun.websocket.util.Config;
import com.baodanyun.websocket.util.JSONUtil;
import com.baodanyun.websocket.util.XMPPUtil;
import org.jivesoftware.smack.*;
import org.jivesoftware.smack.chat.ChatManager;
import org.jivesoftware.smack.chat.ChatManagerListener;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smack.roster.RosterEntry;
import org.jivesoftware.smack.roster.RosterGroup;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jivesoftware.smackx.iqregister.AccountManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by liaowuhen on 2016/11/11.
 */
@Service
public class XmppServer {

    public final static String CUSTOMER = "customer";
    public final static String CUSTOMER_WEIXIN = "customer_weixin";
    public final static String VISITOR = "visitor";
    /**
     * key 为用户id
     */
    private static final Map<String, XmppAdapter> XMPP_MAP = new ConcurrentHashMap<>();
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    protected MsgSendControl msgSendConrol;
    @Autowired
    protected OfuserMapper ofuserMapper;
    @Autowired
    protected MsgSendControl msgSendControl;
    @Autowired
    private WebSocketService webSocketService;

    @Autowired
    private VcardService vcardService;


    public RosterGroup getGroup(String jid, String groupName) throws XMPPException, IOException, SmackException, BusinessException {
        Roster roster = Roster.getInstanceFor(this.getXMPPConnectionAuthenticated(jid));

        RosterGroup group = roster.getGroup(groupName);

        return group;
    }

    public List<RosterEntry> getRosterEntrys(String jid, String groupName) throws XMPPException, IOException, SmackException, BusinessException {
        RosterGroup group = getGroup(jid, groupName);

        return group.getEntries();
    }

    public boolean isAuthenticated(String jid) {
        AbstractXMPPConnection xMPPConnection = getXMPPConnection(jid);
        if (null != xMPPConnection) {
            if (xMPPConnection.isConnected() && xMPPConnection.isAuthenticated()) {
                return true;
            } else {
                logger.info("jid:[" + jid + "] xMPPConnection is closed ");
                return false;
            }
        } else {
            logger.info("jid:[" + jid + "]  xMPPConnection is null");
            return false;
        }
    }

    /**
     * 是否在线 包括手机端在线和容器tomcat在线
     *
     * @param jid
     * @return
     */


    public boolean isConnected(String jid) {
        AbstractXMPPConnection xMPPConnection = getXMPPConnection(jid);
        if (null != xMPPConnection && xMPPConnection.isConnected()) {
            return true;
        } else {
            logger.info("jid:[" + jid + "] xMPPConnection is closed or xMPPConnection is null");
            return false;
        }
    }


    /**
     * 保存登陆成功的XMppConnection
     *
     * @param jid
     * @param xMPPConnection
     */
    public void saveXMPPConnection(String jid, AbstractXMPPConnection xMPPConnection) {
        XmppAdapter xa = new XmppAdapter();
        xa.setXmpp(xMPPConnection);
        xa.setTime(System.currentTimeMillis());
        XMPP_MAP.put(jid, xa);
    }

    /**
     * 获取登录成功的XMPPConnection
     *
     * @param jid
     * @return
     */

    public AbstractXMPPConnection getXMPPConnectionAuthenticated(String jid) throws BusinessException {
        AbstractXMPPConnection xmppConnection = getXMPPConnection(jid);
        if (null != xmppConnection && xmppConnection.isAuthenticated()) {
            return xmppConnection;
        } else {
            throw new BusinessException("jid:[ " + jid + "----xMPPConnection is not Authenticated");
        }
    }

    /**
     * 获取登录成功的XMPPConnection
     *
     * @param jid
     * @return
     */
    public AbstractXMPPConnection getXMPPConnection(String jid) {
        XmppAdapter xmppAdapter = XMPP_MAP.get(jid);
        if (null != xmppAdapter) {
            return xmppAdapter.getXmpp();
        }
        return null;
    }

    public boolean closed(String jid) throws IOException {
        AbstractXMPPConnection xMPPConnection = getXMPPConnection(jid);
        if (null != xMPPConnection) {
            if (xMPPConnection.isConnected()) {
                xMPPConnection.disconnect();
            }
            XMPP_MAP.remove(jid);
            return true;
        } else {
            logger.info("jid:[" + jid + "] xMPPConnection is closed or xMPPConnection is null");
            return false;
        }

    }

    public void sendPresence(String jid, Presence.Type type) throws SmackException.NotConnectedException {
        Presence presence = new Presence(type);
        AbstractXMPPConnection xMPPConnection = getXMPPConnection(jid);
        xMPPConnection.sendStanza(presence);
    }

    public void sendMessage(Msg msg) throws SmackException.NotConnectedException, BusinessException {
        Message xmppMsg = new Message();
        xmppMsg.setFrom(msg.getFrom());
        xmppMsg.setTo(msg.getTo());
        xmppMsg.setType(Message.Type.chat);
        xmppMsg.setBody(msg.getContent().toString());
        xmppMsg.setSubject(msg.getContentType());

        sendMessage(xmppMsg);

    }

    public void sendMessage(Message xmppMsg) throws BusinessException, SmackException.NotConnectedException {
        logger.info("xmpp send message:" + JSONUtil.toJson(xmppMsg));
        XMPPConnection connection = this.getXMPPConnectionAuthenticated(xmppMsg.getFrom());
        connection.sendStanza(xmppMsg);
    }


    public AbstractXMPPConnection getXMPPConnectionNew(ChatNode chatNode) throws IOException, XMPPException, SmackException {
        XMPPTCPConnectionConfiguration.Builder builder = XMPPTCPConnectionConfiguration.builder();
        builder.setSecurityMode(ConnectionConfiguration.SecurityMode.disabled);
        builder.setSendPresence(false);


        XMPPTCPConnectionConfiguration config = builder.setServiceName(Config.xmppdomain).setHost(Config.xmppurl).setPort(Integer.parseInt(Config.xmppport)).build();
        AbstractXMPPConnection connection = new XMPPTCPConnection(config);


        ReconnectionManager reconnectionManager = ReconnectionManager.getInstanceFor(connection);
        reconnectionManager.setFixedDelay(10);
        reconnectionManager.setReconnectionPolicy(ReconnectionManager.ReconnectionPolicy.FIXED_DELAY);
        reconnectionManager.enableAutomaticReconnection();


        Roster.getInstanceFor(connection).setRosterLoadedAtLogin(false);

        // 增加消息监听
        ChatManager chatmanager = ChatManager.getInstanceFor(connection);
        chatmanager.addChatListener(chatNode);

        //初始化的连接监听器
        connection.addConnectionListener(chatNode);

        connection.connect();

        return connection;
    }

    public AbstractXMPPConnection getXMPPConnectionNew(AbstractUser user) throws IOException, XMPPException, SmackException {
        XMPPTCPConnectionConfiguration.Builder builder = XMPPTCPConnectionConfiguration.builder();
        builder.setSecurityMode(ConnectionConfiguration.SecurityMode.disabled);
        builder.setSendPresence(false);


        XMPPTCPConnectionConfiguration config = builder.setServiceName(Config.xmppdomain).setHost(Config.xmppurl).setPort(Integer.parseInt(Config.xmppport)).build();
        AbstractXMPPConnection connection = new XMPPTCPConnection(config);


        ReconnectionManager reconnectionManager = ReconnectionManager.getInstanceFor(connection);
        reconnectionManager.setFixedDelay(10);
        reconnectionManager.setReconnectionPolicy(ReconnectionManager.ReconnectionPolicy.FIXED_DELAY);
        reconnectionManager.enableAutomaticReconnection();


        Roster.getInstanceFor(connection).setRosterLoadedAtLogin(false);

        // 增加消息监听
        ChatManager chatmanager = ChatManager.getInstanceFor(connection);
        ChatManagerListener chatManagerListener = new InitChatManagerListener(user, msgSendControl);
        chatmanager.addChatListener(chatManagerListener);

        //初始化的连接监听器
        ConnectionListener connectionListener = new InitConnectListener(user);
        connection.addConnectionListener(connectionListener);

        connection.connect();

        return connection;
    }

    public synchronized boolean login(AbstractUser user) throws SmackException, XMPPException, IOException {
        boolean isLoginDone = false;
        boolean is = isAuthenticated(user.getId());
        if (!is) {
            Ofuser ofuser = ofuserMapper.selectByPrimaryKey(user.getLoginUsername());

            AbstractXMPPConnection xmppConnection = getXMPPConnectionNew(user);
            if (null == ofuser) {
                logger.info("创建用户:" + user.getLoginUsername());
                AccountManager accountManager = AccountManager.getInstance(xmppConnection);
                accountManager.createAccount(user.getLoginUsername(), user.getPassWord());

            }
            xmppConnection.login(user.getLoginUsername(), user.getPassWord());
            isLoginDone = true;

            if (isLoginDone) {
                logger.info("id:[" + user.getId() + "] login success");
                this.saveXMPPConnection(user.getId(), xmppConnection);
            }
        }

        return isLoginDone;
    }


    public synchronized boolean login(ChatNode chatNode) throws SmackException, XMPPException, IOException {
        boolean isLoginDone = false;
        boolean is = isAuthenticated(chatNode.getAbstractUser().getId());
        if (!is) {
            Ofuser ofuser = ofuserMapper.selectByPrimaryKey(chatNode.getAbstractUser().getLoginUsername());

            AbstractXMPPConnection xmppConnection = getXMPPConnectionNew(chatNode);
            if (null == ofuser) {
                logger.info("创建用户:" + chatNode.getAbstractUser().getLoginUsername());
                AccountManager accountManager = AccountManager.getInstance(xmppConnection);
                accountManager.createAccount(chatNode.getAbstractUser().getLoginUsername(), chatNode.getAbstractUser().getPassWord());

            }
            xmppConnection.login(chatNode.getAbstractUser().getLoginUsername(), chatNode.getAbstractUser().getPassWord());
            isLoginDone = true;

            if (isLoginDone) {
                logger.info("id:[" + chatNode.getAbstractUser().getId() + "] login success");

                this.saveXMPPConnection(chatNode.getAbstractUser().getId(), xmppConnection);
                chatNode.setXmppConnection(xmppConnection);
            }
        }

        return isLoginDone;
    }


    public void roster(String vjid, String cjid) throws BusinessException, SmackException.NotLoggedInException, XMPPException.XMPPErrorException, SmackException.NotConnectedException, SmackException.NoResponseException {
        AbstractXMPPConnection createAccountConn = this.getXMPPConnectionAuthenticated(vjid);

        Roster roster = Roster.getInstanceFor(createAccountConn);
        roster.setSubscriptionMode(Roster.SubscriptionMode.accept_all);

        roster.createEntry(cjid, XMPPUtil.jidToName(cjid), new String[]{"Friends"});
    }


    /**
     * 修改密码
     *
     * @return
     */


    public boolean changePassword(String jid, String pwd) throws SmackException.NotConnectedException, XMPPException.XMPPErrorException, SmackException.NoResponseException, BusinessException {
        if (isAuthenticated(jid)) {

            AccountManager.getInstance(getXMPPConnectionAuthenticated(jid)).changePassword(pwd);
            return true;
        }
        return false;
    }

}
