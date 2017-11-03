package com.baodanyun.cc.service;

import com.baodanyun.websocket.model.CCProperties;

/**
 * Created by liaowuhen on 2017/11/1.
 */
public interface CCPropertiesService {
    int insert(CCProperties record);

    CCProperties selectByName(String name);

    int updateByPrimaryKeySelective(CCProperties record);
}
