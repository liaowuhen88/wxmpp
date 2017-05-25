package com.baodanyun.websocket.node.dispatcher;

/**
 * Created by liaowuhen on 2017/5/24.
 */
public interface VisitorDispather {

    boolean joinQueue() throws InterruptedException;

    boolean uninstall() throws InterruptedException;

}
