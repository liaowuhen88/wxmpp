package com.baodanyun.websocket.util;

import com.baodanyun.websocket.alarm.LRUCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 消息来源
 *
 * @author hubo
 * @since 2017-07-13 11:15
 **/
public class MsgSourceUtil {
    private static final LRUCache<String, Integer> cache = new LRUCache<>(1000);
    protected static Logger logger = LoggerFactory.getLogger(MsgSourceUtil.class);

    public static void put(String key, Integer val) {
        cache.put(key, val);
    }

    public static void put(String from, String to, Integer val) {
        String key = XMPPUtil.jidToName(from) + "_" + XMPPUtil.jidToName(to);
        logger.info("kye:{}", key);
        put(key, val);
    }

    public static Integer get(String from, String to) {
        String key = XMPPUtil.jidToName(from) + "_" + XMPPUtil.jidToName(to);
        logger.info("kye:{}", key);
        return get(key);
    }

    public static Integer get(String key) {
        return cache.get(key);
    }

    public static void remove(String key) {
        cache.remove(key);
    }
}
