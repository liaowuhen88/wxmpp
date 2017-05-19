/*
package com.baodanyun.websocket.core.handle;

import com.baodanyun.websocket.util.IPDataHandler;
import org.apache.log4j.Logger;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

*/
/**
 * Created by yutao on 2016/9/8.
 * 节点属性
 *//*

public class NodeProperties {
    protected static Logger logger = Logger.getLogger(NodeProperties.class);
    //属性过滤器
    public PropertiesFilter propertiesFilter;

    public static final String P_ID = "Id";
    public static final String P_OPEN_ID = "P_OPEN_ID";
    public static final String P_IP = "Ip";
    public static final String P_TYPE = "Type";
    public static final String P_ICON = "Icon";
    public static final String P_NICKNAME = "NickName";
    public static final String P_USERNAME = "UserName";
    public static final String P_LOGIN_TIME = "LoginTime";
    public static final String P_PROTOCOL = "Protocol";
    public static final String P_LOGOUT_TIME = "LogoutTime";
    public static final String P_UA = "UserAgent";
    public static final String P_DESC = "Desc";
    public static final String P_UPDATE_TIME = "UpdateTime";
    public static final String P_MAX_ONLINE_QUEUE_SIZE = "OnlineQueueSize";
    public static final String P_MAX_WAIT_QUEUE_SIZE = "WaitQueueSize";
    public static final String P_WAIT_START_TIME = "";//开始排队的时间

    //文字转换
    private static final Map<String, String> LANG_CH = new HashMap<>();

    static {
        LANG_CH.put(P_ID, "编号");
        LANG_CH.put(P_OPEN_ID, "openId");
        LANG_CH.put(P_IP, "访问地区");
        LANG_CH.put(P_TYPE, "用户类型");
        LANG_CH.put(P_PROTOCOL, "访问协议");
        LANG_CH.put(P_LOGOUT_TIME, "上次登出时间");
        LANG_CH.put(P_LOGIN_TIME, "登录时间");
        LANG_CH.put(P_UA, "UA");
        LANG_CH.put(P_DESC, "描述");
        LANG_CH.put(P_ICON, "头像");
        LANG_CH.put(P_NICKNAME, "昵称");
        LANG_CH.put(P_USERNAME, "用户名");
        LANG_CH.put(P_UPDATE_TIME, "更新时间");
    }

    private Map<String, String> properties = new ConcurrentHashMap<>();

    public static String checkKey(String key) {
        return LANG_CH.get(key);
    }

    public NodeProperties addParams(String key, String val) {
        properties.put(key, val);
        return this;
    }

    public NodeProperties addMaps(Map<String, String> newProperties) {
        properties.putAll(newProperties);
        return this;
    }


    public void addMap(Map<String, String> map) {
        properties.putAll(map);
    }

    public void removeParams(String key) {
        properties.remove(key);
    }

    public Map<String, String> getProperties() {
        return properties;
    }

    */
/**
     * map转换成list
     * 支持过滤
     *
     * @return
     *//*

    public List<Properties> getList() {
        Iterator<String> it = properties.keySet().iterator();
        List<Properties> list = new ArrayList<>();
        while (it.hasNext()) {
            String key = it.next();
            if (propertiesFilter != null && propertiesFilter.filter(key)) {
                String value = properties.get(key);
                Properties properties = new Properties();
                if (P_IP.equals(key)) {
                    String ip = "未知";
                    try {
                        ip = IPDataHandler.findGeography(value);
                    } catch (Exception e) {
                        logger.error(e);
                    }
                    properties.setValue(ip);
                } else {
                    properties.setValue(value);
                }
                properties.setKey(LANG_CH.get(key) == null ? key : LANG_CH.get(key));
                list.add(properties);
            }
        }
        return list;
    }

    public interface PropertiesFilter {
        boolean filter(String key);
    }

    public class Properties {
        private String key;
        private String value;

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }

}
*/
