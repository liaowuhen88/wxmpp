package com.baodanyun.websocket.util;

import com.baodanyun.websocket.core.common.Common;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by yutao on 2016/7/12.
 */
public class XMPPUtil {
    public static Logger logger = LoggerFactory.getLogger(XMPPUtil.class);

    public static String nameToJid(String name) {
        if (StringUtils.isBlank(name))
            return null;

        return name + Common.COMMON_XMPP_DOMAIN;
    }

    public static String jidToName(String jid) {
        if (StringUtils.isNotEmpty(jid)) {
            if (jid.contains(Common.COMMON_XMPP_DOMAIN_CHAR)) {
        return jid.substring(0, jid.indexOf(Common.COMMON_XMPP_DOMAIN_CHAR));
            }
        }
        return jid;
    }

    public static String removeSource(String jid) {
        if (StringUtils.isNotBlank(jid) && jid.contains("/")) {
            return jid.substring(0, jid.lastIndexOf("/"));
        }
        return jid;
    }

    public static String buildJson(Object o) {
       return JSONUtil.toJson(o);
    }

}
