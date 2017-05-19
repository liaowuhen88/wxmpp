package com.baodanyun.websocket.service;

import com.baodanyun.websocket.bean.Tags;
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
public class TagsServer {
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    @Qualifier("jdbcTemplate")
    public JdbcTemplate jdbcTemplate;

    public static String getTagsAll = "select * from tags ";
    public static String addTags = "insert tags(tagname,uid) VALUES (?,?);";
    public static String deleteTags = "delete from tags where id = ? ";

    public Integer addTags(String tagname, String uid) {
        int count = jdbcTemplate.update(addTags, new Object[]{tagname, uid});
        return count;
    }

    public List<Tags> getTagsAll() {
        return jdbcTemplate.query(getTagsAll, new BeanPropertyRowMapper<Tags>(Tags.class));
    }

    public int deleteTags(Integer id) {
        return jdbcTemplate.update(deleteTags, new Object[]{id});
    }

}
