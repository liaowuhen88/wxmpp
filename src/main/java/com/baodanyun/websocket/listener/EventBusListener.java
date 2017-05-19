package com.baodanyun.websocket.listener;

/**
 * Created by liaowuhen on 2017/5/12.
 */

public interface EventBusListener<T> {

    void init();

    boolean processExpiringEvent(T t);
}
