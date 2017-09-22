package com.baodanyun.websocket.core.common;

/**
 * Created by yutao on 2016/7/12.
 */
public class Common {
    public static final String USER_KEY = "user_key";
    public static final String VISITOR_USER_HEADER = "visitor_user_header";
    public static final String VISITOR_USER_HTTP_SESSION = "visitor_user_http_session";

    public static final String CUSTOMER_USER_HEADER = "customer_user_header";
    public static final String CUSTOMER_USER_HTTP_SESSION = "customer_user_http_session";

    public static final String COMMON_XMPP_DOMAIN = "@126xmpp";
    public static final String COMMON_XMPP_DOMAIN_CHAR = "@";
    public static final String MSG_ID_TPL = "%s-%s";
    public static final String FROM_SYS_ID = "sys";//系统发送的消息

    public static final String userVcard = "key";
    public static final String quickReplyVcard = "key";

    /*客服负责人登陆名*/
    public static final String CUSTOMER_LEADER = "wangjing;yutao";

    /*微信超时返回结果*/
    public static final String[] WORDS = {"45015__回复时间超过限制", "45047__未知错误代码:45047"
            , "【收到不支持的消息类型，暂无法显示】", "系统消息,微信发送超时，消息发送失败",
            "您好，我是豆包网的专属客服,不知道您有什么疑问可以帮到您^_^",
            "40003__不合法的OpenID，请开发者确认OpenID（该用户）是否已关注公众号，或是否是其他公众号的OpenID"};

    public enum ErrorCode {

        PARAM_ERROR("loginParamError",1),
        LOGIN_ERROR("nameOrPwdError",2);

        private String codeName;
        private int code;

        ErrorCode(String codeName,int code){
            this.codeName = codeName;
            this.code = code;
        }

        public String getCodeName() {
            return codeName;
        }

        public int getCode(){
            return code;
        }
    }
}
