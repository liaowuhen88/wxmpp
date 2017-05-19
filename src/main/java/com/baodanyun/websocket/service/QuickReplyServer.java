package com.baodanyun.websocket.service;

import com.baodanyun.websocket.bean.QuickReply;
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
public class QuickReplyServer {
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    @Qualifier("jdbcTemplate")
    public JdbcTemplate jdbcTemplate;

    public static String getQuickReply = "select * from quickreply  where addid = ? ";
    public static String addQuickReply = "insert quickreply(addid,message) VALUES (?,?);";
    public static String deleteQuickReply = "delete from quickreply where id = ? ";

    public Integer addQuickReply(String jid, String message) {
        int count = jdbcTemplate.update(addQuickReply, new Object[]{jid, message});
        return count;
    }

    public List<QuickReply> getQuickReply(String cjid) {
        return jdbcTemplate.query(getQuickReply, new Object[]{cjid}, new BeanPropertyRowMapper<QuickReply>(QuickReply.class));
    }

    public int deleteQuickReply(Integer id) {
        return jdbcTemplate.update(deleteQuickReply, new Object[]{id});
    }

}
