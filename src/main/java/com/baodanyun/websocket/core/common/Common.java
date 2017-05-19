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
