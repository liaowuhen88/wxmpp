/*
package com.baodanyun.websocket.service.impl.lifecycle;

import com.baodanyun.websocket.service.MsgSendService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

*/
/**
 * Created by liaowuhen on 2017/3/6.
 *//*


*/
/**
 * uec 接入客服的处理类
 *//*

@Service("accessUserLifeCycleService")
public class AccessVisitorUserLifeCycleServiceImpl extends VisitorUserLifeCycleServiceImpl {
    private static final Logger logger = Logger.getLogger(AccessVisitorUserLifeCycleServiceImpl.class);

    @Autowired
    @Qualifier("accessMsgSendService")
    protected MsgSendService msgSendService;

    @Override
    public MsgSendService getMsgSendService() {
        return msgSendService;
    }
}
*/
