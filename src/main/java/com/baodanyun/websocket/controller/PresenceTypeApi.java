package com.baodanyun.websocket.controller;

import com.baodanyun.websocket.bean.Response;
import com.baodanyun.websocket.service.XmppServer;
import com.baodanyun.websocket.util.JSONUtil;
import com.baodanyun.websocket.util.Render;
import org.apache.log4j.Logger;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.roster.Roster;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

/**
 * Created by yutao on 2016/10/4.
 */
@RestController
public class PresenceTypeApi extends BaseController {
    protected static Logger logger = Logger.getLogger(PresenceTypeApi.class);

    @Autowired
    private XmppServer xmppServer;

    @RequestMapping(value = "GetPresenceType")
    public void GetPresenceType(String jid, String to, HttpServletResponse httpServletResponse) {
        Response response = new Response();
        try {
            XMPPConnection conn = xmppServer.getXMPPConnection(jid);
            Roster roster = Roster.getInstanceFor(conn);
            Presence presence = roster.getPresence(jid);

            logger.info(JSONUtil.toJson(presence));
            if (presence.getType() == Presence.Type.available) {
                logger.info("User is online");
            }

            response.setSuccess(true);


        } catch (Exception e) {
            logger.error(e);
            response.setMsg("update error");
            response.setSuccess(false);
        }
        Render.r(httpServletResponse, JSONUtil.toJson(response));
    }


}
