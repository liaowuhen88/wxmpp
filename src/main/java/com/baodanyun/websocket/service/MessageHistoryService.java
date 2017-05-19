package com.baodanyun.websocket.service;

import com.baodanyun.websocket.bean.ChatHistoryUser;
import com.baodanyun.websocket.bean.MessageUser;
import com.baodanyun.websocket.bean.msg.Msg;
import com.baodanyun.websocket.bean.user.Visitor;
import com.baodanyun.websocket.exception.BusinessException;
import com.baodanyun.websocket.model.HistoryMessageUserModel;
import com.baodanyun.websocket.util.JSONUtil;
import com.baodanyun.websocket.util.XmllUtil;
import org.apache.commons.lang.StringUtils;
import org.jdom.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by liaowuhen on 2016/10/18.
 */
@Service
public class MessageHistoryService {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    public static String getUserHistoryList = " SELECT time,id,username,vcard FROM ( SELECT from_unixtime(left(startTime, 10), '%Y-%m-%d ') time1,max(from_unixtime(left(startTime, 10), '%Y-%m-%d %H:%m:%s')) time, withJid id FROM archiveConversations WHERE ownerJid = ?  GROUP BY withJid,time1 ORDER BY time desc LIMIT ?,? ) AS USER LEFT JOIN ofVCard v ON CONCAT(v.username, '@126xmpp') = id ORDER BY time desc  ";

    public static String getUserHistoryCount = "SELECT COUNT(*) count FROM ( SELECT MAX(startTime) time, withJid id FROM archiveConversations WHERE  ownerJid = ? GROUP BY withJid ORDER BY max(startTime)  ) AS USER LEFT JOIN ofVCard v on CONCAT(v.username,'@126xmpp') = id  ";

    public static String getByThreadId = "SELECT am.messageId FROM archiveMessages am LEFT JOIN archiveConversations ac ON ac.conversationId = am.conversationId WHERE ac.thread = ? and direction = 'to'";

    public static String getMessageById = "SELECT am.`subject` as 'contentType' ,am.messageId,am.time ct ,am.body content,am.type,ac.withJid as 'to', ac.ownerJid as 'from' FROM archiveMessages am LEFT JOIN archiveConversations ac ON ac.conversationId = am.conversationId WHERE am.messageId = ?  ORDER BY ac.startTime DESC LIMIT   ?,?";

    @Autowired
    @Qualifier("jdbcTemplate")
    public JdbcTemplate jdbcTemplate;

    /*
      根据消息id获取一条信息
     */
    public Integer getByThreadId(String ThreadId) {
        //FOR  UPDATE
        Integer msg = null;
        try {
            msg = jdbcTemplate.queryForObject(getByThreadId, Integer.class, new Object[]{ThreadId});
        } catch (Exception e) {
            logger.info("查询数据库出错", e);

        }

        return msg;
    }

    /*
     根据消息id获取消息body
    */
    public Msg getBodyById(String id) {
        //FOR  UPDATE
        try {
            return jdbcTemplate.queryForObject(getMessageById, Msg.class, new Object[]{id});
        } catch (Exception e) {
            logger.info("查询数据库出错", e);

        }
        return null;
    }


    /**
     * 从历史聊天记录获取用户列表
     *
     * @param
     * @return
     */

    public List<MessageUser> getUserHistoryList(HistoryMessageUserModel model) throws BusinessException {

        try {
            int limit = model.getPage() * model.getCount();
            return jdbcTemplate.query(getUserHistoryList, new Object[]{model.getId(), limit, model.getCount()}, new BeanPropertyRowMapper<MessageUser>(MessageUser.class));

        } catch (Exception e) {
            logger.error("", e);
            throw new BusinessException("查询数据库异常");
        }
    }

    /**
     * 从历史聊天记录获取用户列表总数
     *
     * @param
     * @return
     */

    public Integer getUserHistoryCount(HistoryMessageUserModel model) {

        return jdbcTemplate.queryForObject(getUserHistoryCount + model.getSearch(), Integer.class, new Object[]{model.getId()});
    }

    public List<ChatHistoryUser> getUserHistoryList(List<MessageUser> li) {

        if (CollectionUtils.isEmpty(li)) {
            return null;
        } else {
            List<ChatHistoryUser> users = new ArrayList<>();
            Iterator<MessageUser> it = li.iterator();
            while (it.hasNext()) {
                MessageUser me = it.next();
                String vCard = me.getVcard();
                if(!StringUtils.isEmpty(vCard)){
                    try {
                        List<Element> els = XmllUtil.xmlElements(vCard);
                        if (!CollectionUtils.isEmpty(els)) {
                            Iterator<Element> eit = els.iterator();
                            while (eit.hasNext()) {
                                Element e = eit.next();
                                if (e.getName().equals("key")) {
                                    String json = e.getText();
                                    Visitor u = JSONUtil.toObject(Visitor.class, json);
                                    ChatHistoryUser cu = new ChatHistoryUser();
                                    cu.setId(me.getId());
                                    cu.setChattime(me.getTime());
                                    cu.setIcon(u.getIcon());
                                    cu.setNickName(u.getNickName());
                                    cu.setRemark(u.getRemark());
                                    cu.setLoginUsername(u.getLoginUsername());
                                    cu.setTags(u.getTags());
                                    users.add(cu);
                                }
                            }
                        }
                    } catch (Exception e) {
                        logger.error("vcard 解析异常", e);
                    }
                }else {
                    ChatHistoryUser cu = new ChatHistoryUser();
                    cu.setId(me.getId());
                    cu.setChattime(me.getTime());
                    cu.setNickName("游客");
                    cu.setLoginUsername(me.getId());
                    users.add(cu);
                }
            }
            return users;

        }
    }

}
