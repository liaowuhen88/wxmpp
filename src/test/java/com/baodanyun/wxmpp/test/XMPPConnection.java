package com.baodanyun.wxmpp.test;

import com.baodanyun.websocket.util.Config;
import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.ConnectionConfiguration.SecurityMode;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class XMPPConnection {
    protected static final Logger logger = LoggerFactory.getLogger(XMPPConnection.class);


    //获取一个新的连接
    public static AbstractXMPPConnection connect() throws IOException, XMPPException, SmackException {
        XMPPTCPConnectionConfiguration config = XMPPTCPConnectionConfiguration.builder()
                .setServiceName(Config.xmppdomain).setHost(Config.xmppurl)
                .setPort(Integer.valueOf(Config.xmppport)).setCompressionEnabled(true).setSecurityMode(SecurityMode.disabled).build();

        AbstractXMPPConnection conn = new XMPPTCPConnection(config);

        ConnectionListenerTest test = new ConnectionListenerTest();

        conn.addConnectionListener(test);

        conn.connect();

        return conn;
    }


}