package com.baodanyun.websocket.service;

import com.baodanyun.websocket.bean.Message;
import com.baodanyun.websocket.model.MessageModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by liaowuhen on 2016/11/11.
 */
@Service
public class MessageServer {
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    @Qualifier("jdbcTemplate")
    public JdbcTemplate jdbcTemplate;

    public static String getMessageByCIdStatus = "select * from dbmessage where cid = ? and status = ? order by id desc  LIMIT   ?,?";
    public static String getMessageByCId = "select * from dbmessage where cid = ? order by id desc    LIMIT   ?,?";
    public static String getMessageById = "select * from dbmessage where id = ? order by id desc    LIMIT   ?,?";

    public static String getMessageCountByCIdStatus = "select count(id) from dbmessage where cid = ? and status = ?";
    public static String getMessageCountByCId = "select count(id) from dbmessage where cid = ?  ";
    public static String getMessageCountById = "select count(id) from dbmessage where id = ? ";


    public static String addMessage = "insert dbmessage(openId,username,phone,content,status,cid) VALUES (?,?,?,?,?,?);";
    public static String updateMessage = "update dbmessage set dealResult = ? ,status = ?  where id = ? ";
    public static String deleteMessage = "delete from dbmessage where id = ? ";

    public Integer addMessage(MessageModel message) {
        int count = jdbcTemplate.update(addMessage, new Object[]{message.getOpenId(), message.getUsername(), message.getPhone(), message.getContent(), Message.SUB, message.getCid()});
        return count;
    }

    public Integer updateMessage(Integer id, String text) {
        int count = jdbcTemplate.update(updateMessage, new Object[]{text, Message.DEAL, id});
        return count;
    }


    public List<Message> getMessageById(MessageModel model) {
        return jdbcTemplate.query(getMessageById, new Object[]{model.getId(), model.getPage() * model.getCount(), model.getCount()}, new BeanPropertyRowMapper<Message>(Message.class));
    }

    public List<Message> getMessageByCId(MessageModel model) {
        return jdbcTemplate.query(getMessageByCId, new Object[]{model.getCid(), model.getPage() * model.getCount(), model.getCount()}, new BeanPropertyRowMapper<Message>(Message.class));
    }

    public List<Message> getMessageByCIdStatus(MessageModel model) {
        return jdbcTemplate.query(getMessageByCIdStatus, new Object[]{model.getCid(), model.getStatus(), model.getPage() * model.getCount(), model.getCount()}, new BeanPropertyRowMapper<Message>(Message.class));
    }

    public Integer getMessageCountById(MessageModel model) {
        return jdbcTemplate.queryForObject(getMessageCountById, Integer.class, new Object[]{model.getId()});
    }

    public Integer getMessageCountByCId(MessageModel model) {
        return jdbcTemplate.queryForObject(getMessageCountByCId, Integer.class, new Object[]{model.getCid()});
    }

    public Integer getMessageCountByCIdStatus(MessageModel model) {
        return jdbcTemplate.queryForObject(getMessageCountByCIdStatus, Integer.class, new Object[]{model.getCid(), model.getStatus()});
    }


    public int deleteMessage(Integer id) {
        return jdbcTemplate.update(deleteMessage, new Object[]{id});
    }

}
