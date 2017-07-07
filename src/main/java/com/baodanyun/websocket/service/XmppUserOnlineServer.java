package com.baodanyun.websocket.service;

import com.baodanyun.websocket.exception.BusinessException;
import com.baodanyun.websocket.util.HttpUtils;
import com.baodanyun.websocket.util.PropertiesUtil;
import com.baodanyun.websocket.util.XmllUtil;
import org.apache.commons.lang.StringUtils;
import org.jdom.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by liaowuhen on 2016/11/11.
 */
@Service
public class XmppUserOnlineServer {
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    private String ip;
    private String domain;

    public static void main(String args[]) {
        System.out.println(123);
        XmppUserOnlineServer xs = new XmppUserOnlineServer();

        String main = "<presence id=\"6kro2sgu58\" from=\"maqiumeng@126xmpp/AstraChat-iOS-69111447\"><show>chat</show><priority>-1</priority><nick xmlns=\"http://jabber.org/protocol/nick\">豆包萌萌</nick></presence>";


        System.out.println(xs.isOnlineXml(main));

    }

    @PostConstruct
    private void init() {
        Map<String, String> map = PropertiesUtil.get(XmppUserOnlineServer.class.getClassLoader(), "config.properties");
        ip = map.get("xmpp.ip");
        domain = map.get("xmpp.domain");
    }

    public boolean isOnline(String uid){
        String jid = uid + "@" + domain;
        try {
            return isOnlineByJid(jid);
        } catch (Exception e) {
            logger.error("解析异常");
        }
        return false;
    }

    public boolean isOnlineByJid(String jid) throws IOException, BusinessException {
        if (StringUtils.isEmpty(jid)) {
            throw new BusinessException("jid 参数为空");
        }
        String url = "http://" + ip + ":" + 9090 + "/plugins/presence/status?jid=" + jid + "&type=xml";
        logger.info("isOnline" + url);
        String str = HttpUtils.get(url);
        logger.info("result" + str);
        return isOnlineXml(str);
    }

    public boolean isOnlineXml(String str) {
        //<presence id="6kro2sgu58" from="maqiumeng@126xmpp/AstraChat-iOS-69111447">
        // <show>chat</show>
        // <priority>-1</priority>
        // <nick xmlns="http://jabber.org/protocol/nick">豆包萌萌</nick>
        // </presence>

        try {
            if (StringUtils.isEmpty(str)) {
                return false;
            }
            Element el = XmllUtil.xmlElementRoot(str);

            if (null != el && StringUtils.isEmpty(el.getName())) {
                return false;
            }

            logger.info(el.getName());
            if (el.getName().equals("presence")) {
                if (!StringUtils.isEmpty(el.getAttribute("from").toString())) {
                    List els = el.getChildren();
                    if (!CollectionUtils.isEmpty(els)) {
                        Iterator<Element> eit = els.iterator();
                        while (eit.hasNext()) {
                            Element e = eit.next();
                            if (e.getName().equals("show")) {
                                if (e.getText().equals("chat")) {
                                    return true;
                                }
                            }
                        }
                    }
                }
            }

        } catch (Exception e) {
            logger.error("error", "xml 解析异常");
        }

        return false;
    }

}
