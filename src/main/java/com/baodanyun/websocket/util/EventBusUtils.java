package com.baodanyun.websocket.util;

import com.google.common.eventbus.EventBus;

/**
 * Created by liaowuhen on 2017/5/17.
 */
public class EventBusUtils {

    public static final EventBus evnetBus = new EventBus();

    public static void post(Object event) {
        evnetBus.post(event);
    }
}
