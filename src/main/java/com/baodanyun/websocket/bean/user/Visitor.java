package com.baodanyun.websocket.bean.user;

import com.baodanyun.websocket.bean.Tags;

import java.util.List;

/**
 * Created by yutao on 2016/7/12.
 */
public class Visitor extends AbstractUser {

    // 当前客服标签
    public List<Tags> tags;
    // 表示用户从那个入口接入
    //  0 默认h5客服端   1 微信直接聊天入口
    private Integer type = 0;

    public Visitor() {
        setUserType(UserType.visitor);
    }

    public List<Tags> getTags() {
        return tags;
    }

    public void setTags(List<Tags> tags) {
        this.tags = tags;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        Visitor visitor = (Visitor) o;

        return getId().equals(visitor.getId());

    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    @Override
    public int hashCode() {
        return getId().hashCode();
    }

}
