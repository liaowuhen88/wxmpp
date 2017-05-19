/*
package com.baodanyun.websocket.core.handle;

import com.baodanyun.websocket.bean.msg.Msg;
import com.baodanyun.websocket.bean.msg.msg.ImgMsg;
import com.baodanyun.websocket.bean.msg.msg.ReceiptMsg;
import com.baodanyun.websocket.bean.msg.msg.TextMsg;
import com.baodanyun.websocket.bean.msg.status.StatusMsg;
import com.google.gson.Gson;
import org.apache.log4j.Logger;
import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.ReconnectionManager;
import org.jivesoftware.smack.SASLAuthentication;
import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;

*/
/**
 * Created by yutao on 2016/9/7.
 *//*

public class XmppConnectionManager {

    XMPPTCPConnectionConfiguration config = null;

    private XmppConnectionManager() {
        XMPPTCPConnectionConfiguration.Builder builder = XMPPTCPConnectionConfiguration.builder();
        builder.setSecurityMode(ConnectionConfiguration.SecurityMode.disabled);
        builder.setSendPresence(false);
        config = builder.setServiceName(Config.xmppdomain).setHost(Config.xmppurl).setPort(Integer.valueOf(Config.xmppport)).build();
    }

    private static XmppConnectionManager CONN = new XmppConnectionManager();

    public static XmppConnectionManager getInstance() {
        return CONN;
    }

    private static Logger logger = Logger.getLogger(XmppConnectionManager.class);

    //获取一个xmpp连接
    //TODO 如果获取连接过快 会导致关闭异常 需要优化
    public AbstractXMPPConnection getConnection() {
        AbstractXMPPConnection connection = new XMPPTCPConnection(config);
        ReconnectionManager reconnectionManager = ReconnectionManager.getInstanceFor(connection);
        reconnectionManager.setFixedDelay(10);
        reconnectionManager.setReconnectionPolicy(ReconnectionManager.ReconnectionPolicy.FIXED_DELAY);
        reconnectionManager.enableAutomaticReconnection();
        Roster.getInstanceFor(connection).setRosterLoadedAtLogin(false);
        return connection;
    }

    //解析字符串数据成为Msg类型
    public static Msg handelMsg(String bodyMsg) {
        Msg msg = null;
        try {
            if (null != bodyMsg) {
                Gson gson = new Gson();
                Msg topMsg = gson.fromJson(bodyMsg, Msg.class);
                if (Msg.Type.msg.toString().equals(topMsg.getType())) {
                    Msg abstractMsg = gson.fromJson(bodyMsg, Msg.class);
                    if (Msg.MsgContentType.text.toString().equals(abstractMsg.getContentType())) {
                        return gson.fromJson(bodyMsg, TextMsg.class);
                    } else if (Msg.MsgContentType.audio.toString().equals(abstractMsg.getContentType())) {

                    } else if (Msg.MsgContentType.file.toString().equals(abstractMsg.getContentType())) {

                    } else if (Msg.MsgContentType.video.toString().equals(abstractMsg.getContentType())) {

                    } else if (Msg.MsgContentType.image.toString().equals(abstractMsg.getContentType())) {
                        return gson.fromJson(bodyMsg, ImgMsg.class);
                    } else if (Msg.MsgContentType.receiptMsg.toString().equals(abstractMsg.getContentType())) {
                        return gson.fromJson(bodyMsg, ReceiptMsg.class);
                    } else{
                        return null;
                    }
                } else if (Msg.Type.active.toString().equals(topMsg.getType())) {
                } else if (Msg.Type.status.toString().equals(topMsg.getType())) {
                    return gson.fromJson(bodyMsg, StatusMsg.class);
                } else {
                }
            }
        } catch (Exception e) {
            logger.error("parseMsg msg error" + e.getMessage());
        }
        return msg;
    }


}
*/
