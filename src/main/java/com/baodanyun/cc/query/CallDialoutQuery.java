package com.baodanyun.cc.query;

/**
 * Created by liaowuhen on 2017/10/31.
 */
public class CallDialoutQuery {
    // 参数名	是否必填	值	参数释义
    private String FromExten;//	是	坐席工号	例：假设8001@gs是坐席登陆账号，8001才是坐席工号
    private String Exten;//	是	被叫号码	被叫号码；对于手机号，系统会自动判断是否本地号码，从而自动在号码前加0，使用者不需要处理，只需传手机号即可，如138********。对于固话，如果是外地号码使用者需要将区号带上，如010********
    private String ExtenType;//	否	Local/sip/gateway	外呼时强制坐席使用该接听方式。可选参数。正常情况下调用外呼接口不需传此字段，座席默认使用登陆呼叫中心的接听方式外呼。如有特殊需要，例如：座席不登陆系统发起外呼，需要传此字段。Local为“手机”sip为“软电话”gateway为“语音网关”
    private String ActionID;//	否	唯一字符串（可随机）	随机码，用户用来标识请求的操作，服务器返回的Response中会带有对应Action的ActionID；在通话事件中会带有该字段；该字段最大长度是40个字节

    public String getFromExten() {
        return FromExten;
    }

    public void setFromExten(String fromExten) {
        FromExten = fromExten;
    }

    public String getExten() {
        return Exten;
    }

    public void setExten(String exten) {
        Exten = exten;
    }

    public String getExtenType() {
        return ExtenType;
    }

    public void setExtenType(String extenType) {
        ExtenType = extenType;
    }

    public String getActionID() {
        return ActionID;
    }

    public void setActionID(String actionID) {
        ActionID = actionID;
    }
}
