package com.baodanyun.websocket.node.dispatcher;

import com.baodanyun.websocket.bean.user.AbstractUser;

/**
 * Created by liaowuhen on 2017/5/24.
 */
public interface CustomerDispather {
    boolean joinQueue(AbstractUser abstractUser) throws InterruptedException;

    boolean uninstall(AbstractUser abstractUser) throws InterruptedException;

}
