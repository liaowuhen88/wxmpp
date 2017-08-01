package com.baodanyun.websocket.util;

import com.baodanyun.websocket.alarm.LRUCache;

/**
 * 消息来源
 *
 * @author hubo
 * @since 2017-07-13 11:15
 **/
public class MsgSourceUtil {
    private static final LRUCache<String, Integer> cache = new LRUCache<>(1000);

    public static void put(String key, Integer val) {
        cache.put(key, val);
    }

    public static Integer get(String key) {
        return cache.get(key);
    }
}
