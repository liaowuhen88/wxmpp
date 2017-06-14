package com.baodanyun.websocket.bean.user;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.Serializable;
import java.util.Comparator;

/**
 * Created by liaowuhen on 2017/3/8.
 */
public class ComparatorFriend implements Comparator<Friend>, Serializable {
    protected static Log logger = LogFactory.getLog(ComparatorFriend.class);
    @Override
    public int compare(Friend o1, Friend o2) {

        /***
         * 倒序排列算法
         */
        try {
            if (null == o1 || null == o2) {
                return -1;
            }
            int flag = 0;
            if (null != o2.getOnlineStatus() && null != o1.getOnlineStatus()) {
                flag = o2.getOnlineStatus().compareTo(o1.getOnlineStatus());
            }
            if (flag == 0) {
                if (null != o2.getLoginTime() && null != o1.getLoginTime()) {
                    return o2.getLoginTime().compareTo(o1.getLoginTime());
                } else {
                    return flag;
                }
            } else {
                return flag;
            }
        } catch (Exception e) {
            return -1;
        }

    }
}
