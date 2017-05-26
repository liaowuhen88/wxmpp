/*
package com.baodanyun.websocket.core.handle;

import com.baodanyun.websocket.bean.user.Visitor;
import com.baodanyun.websocket.util.MD5Utils;
import org.apache.log4j.Logger;
import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smackx.iqregister.AccountManager;

import java.util.HashMap;
import java.util.Map;

*/
/**
 * Created by yutao on 2016/9/27.
 * 统一登录受检
 *//*

public class LoginCheckHandel {

    protected static Logger logger = Logger.getLogger(LoginCheckHandel.class);

    //System.out.println(MD5Utils.getMD5Code("17doubao.com"));
    private static final String VISITOR_DEFAULT_PWD = "00818863ff056f1d66c8427836f94a87";

    private LoginCheckHandel() {
    }

    private static final LoginCheckHandel LOGIN_CHECK_HANDEL = new LoginCheckHandel();

    public static LoginCheckHandel getInstance() {
        return LOGIN_CHECK_HANDEL;
    }

    private XmppConnectionManager xmppConnectionManager = XmppConnectionManager.getInstance();

    */
/**
     * TODO 这里用了一个链接 来检查 用户名和密码是否正确 由于SMACK 没有单独的检查用户铭的api 所有暂时用login
     *
     * @return
     *//*

    public boolean preValidHandel(String username, String pwd, String type) {
        AbstractXMPPConnection conn = xmppConnectionManager.getConnection();
        try {
            conn.connect();
            if ("customer".equals(type)) {
                conn.login(username, pwd);
            } else if ("visitor".equals(type)) {
                try {
                    //客服登录失败后 尝试创建用户
                    conn.login(username, pwd);
                } catch (Exception e1) {
                    e1.printStackTrace();
                    AccountManager accountManager = AccountManager.getInstance(conn);
                    accountManager.createAccount(username,pwd);
                }
            }
            return true;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            if (conn != null && conn.isConnected()) {
                try {
                    conn.disconnect();
                } catch (Exception e1) {
                    //
                }
                conn = null;
            }
        }
        return false;
    }


}
*/
