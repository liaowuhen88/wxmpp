package com.baodanyun.websocket.controller;

import com.baodanyun.websocket.bean.Response;
import com.baodanyun.websocket.bean.user.AbstractUser;
import com.baodanyun.websocket.bean.user.ComparatorFriend;
import com.baodanyun.websocket.bean.user.Friend;
import com.baodanyun.websocket.bean.user.Visitor;
import com.baodanyun.websocket.core.common.Common;
import com.baodanyun.websocket.enums.MsgStatus;
import com.baodanyun.websocket.model.ConversationCustomer;
import com.baodanyun.websocket.service.ConversationCustomerService;
import com.baodanyun.websocket.service.XmppServer;
import com.baodanyun.websocket.util.JSONUtil;
import com.baodanyun.websocket.util.Render;
import com.baodanyun.websocket.util.XMPPUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * Created by yutao on 2016/10/4.
 */
@RestController
public class QueueApi extends BaseController {
    protected static Log logger = LogFactory.getLog(QueueApi.class);

    @Autowired
    private ConversationCustomerService conversationCustomerService;

    @Autowired
    private XmppServer xmppServer;


    @RequestMapping(value = "queue/{q}")
    public void backupQueue(@PathVariable("q") String q, HttpServletRequest request, HttpServletResponse response) {
        Response msgResponse = new Response();
        try {
            AbstractUser customer = (AbstractUser) request.getSession().getAttribute(Common.USER_KEY);
            if (null != customer) {

                Set<Friend> friendList = new HashSet<>();

                if (QueueName.online.getValue().equals(QueueName.getQueueNameByKey(q))) {

                    ConversationCustomer record = new ConversationCustomer();
                    record.setCjid(customer.getId());

                    List<ConversationCustomer> onlineQueue = conversationCustomerService.select(record);

                    if (!CollectionUtils.isEmpty(onlineQueue)) {
                        for (ConversationCustomer cc : onlineQueue) {

                            Friend friend = getFriend(cc, customer);
                            if (null != friend) {
                                friendList.add(friend);
                            }
                        }
                    }
                }

                List<Friend> li = new ArrayList<>(friendList);
                logger.info(li.size());
                ComparatorFriend comparator = new ComparatorFriend();
                Collections.sort(li, comparator);
                msgResponse.setData(li);
                msgResponse.setSuccess(true);
            } else {
                msgResponse.setMsg("非法访问");
                msgResponse.setSuccess(false);
            }
        } catch (Exception e) {
            logger.error("error", e);
            msgResponse.setSuccess(false);
        }
        Render.r(response, XMPPUtil.buildJson(msgResponse));
    }

    private Friend getFriend(ConversationCustomer cc, AbstractUser customer) {

        Friend friend = new Friend();
        try {
            Visitor visitor = JSONUtil.toObject(Visitor.class, cc.getVisitor());

            friend.setId(visitor.getId());

            friend.setName(visitor.getUserName());

            if (!xmppServer.isAuthenticated(visitor.getId())) {
                friend.setOnlineStatus(MsgStatus.history);
            } else {
                friend.setOnlineStatus(MsgStatus.online);
            }

            friend.setNickname(visitor.getNickName() == null ? visitor.getUserName() == null ? "未知" : visitor.getUserName() : visitor.getNickName());
            friend.setIcon(visitor.getIcon());
            friend.setNickname(visitor.getNickName());
            friend.setLoginUsername(visitor.getLoginUsername());
            friend.setOpenId(visitor.getOpenId());
            friend.setLoginTime(visitor.getLoginTime());


        } catch (Exception e) {
            logger.error("error", e);
        }


        return friend;

    }

    public enum QueueName {
        online("1", "online"), wait("2", "wait");

        private String key;
        private String value;

        QueueName(String key, String value) {
            this.key = key;
            this.value = value;
        }

        public static String getQueueNameByKey(String key) {
            if (StringUtils.isBlank(key)) {
                return null;
            }
            for (QueueName queueName : QueueName.values()) {
                if (key.equals(queueName.getKey())) {
                    return queueName.name();
                }
            }
            return null;
        }

        public String getKey() {
            return key;
        }

        public String getValue() {
            return value;
        }
    }

}
