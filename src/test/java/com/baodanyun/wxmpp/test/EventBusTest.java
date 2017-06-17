package com.baodanyun.wxmpp.test;

import com.baodanyun.websocket.event.SynchronizationMsgEvent;
import com.baodanyun.websocket.event.VisitorJoinEvent;
import com.baodanyun.websocket.event.VisitorLoginEvent;
import com.baodanyun.websocket.util.EventBusUtils;
import org.junit.Test;

/**
 * Created by liaowuhen on 2017/5/17.
 */
public class EventBusTest extends BaseTest {

    @Test
    public void testLogin() {
        VisitorLoginEvent le = new VisitorLoginEvent(null, null, null);

        VisitorJoinEvent vj = new VisitorJoinEvent(null, null, null);

        SynchronizationMsgEvent sm = new SynchronizationMsgEvent(null,null);

        EventBusUtils.post(le);
        EventBusUtils.post(vj);
        EventBusUtils.post(sm);
    }
}
