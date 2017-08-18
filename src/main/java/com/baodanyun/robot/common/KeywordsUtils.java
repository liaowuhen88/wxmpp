package com.baodanyun.robot.common;

import com.baodanyun.websocket.bean.msg.Msg;
import org.apache.commons.lang.StringUtils;

public class KeywordsUtils {

    /**
     * 微信消息内容中是否存在关键字
     *
     * @param msg 内容
     * @return true则存在否则不存在
     */
    public static boolean checkKeywords(Msg msg) {
        if (msg == null)
            return false;

        return checkKeywords(msg.getContent());
    }

    /**
     * 内容中是否存在关键字
     *
     * @param content 内容
     * @return true则存在否则不存在
     */
    public static boolean checkKeywords(String content) {
        boolean exists = false;

        if (StringUtils.isNotBlank(content)) {
            String[] arr = RobotConstant.KEYWORDS.split(RobotConstant.SEPARATOR);
            if (arr != null && arr.length > 0) {
                for (String str : arr) {
                    return str.equals(content);
                }
            }
        }
        return exists;
    }

}
