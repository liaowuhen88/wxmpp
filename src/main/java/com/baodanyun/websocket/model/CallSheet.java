package com.baodanyun.websocket.model;

/**
 * Created by liaowuhen on 2017/10/31.
 * <p>
 * 参考地址:https://developer.7moor.com/v2docs/queryCdr-2/
 */
public class CallSheet {

    private String _id;//本条通话记录的唯一ID
    private String CALL_SHEET_ID;//	本条通话记录的唯一ID（与_id值一致）
    private String CALL_NO;//	主叫号码
    private String CALLED_NO;//	被叫号码
    private String END_TIME;//	结束时间
    private String CONNECT_TYPE;//	呼叫类型，值为 normal（普通来电）、dialout（外呼去 电）、transfer（来电转接）、dialTransfer（外呼转接）
    private String STATUS;//	处理状态，值为dealing（已接听）、notDeal（振铃未接 听）、queueLeak（排队放弃）、voicemail（已留言）、 leak（IVR放弃） 、blackList（黑名单）
    private String DISPOSAL_AGENT;//	处理座席ID
    private String BEGIN_TIME;//	通话开始时间（只有已接听状态的才有值）
    private String OFFERING_TIME;//	呼叫发起时间
    private String RECORD_FILE_NAME;//录音文件名
    private String CUSTOMER_NAME;//	定位客户名称
    private String REF_CALL_SHEET_ID;//	转接类型通话，此字段记录之前一通通话记录的ID
    private String PBX;//通话产生所在PBX的ID
    private String QUEUE_NAME;  //技能组名称
    private String FILE_SERVER;//录音服务器地址
    private String PROVINCE;//省
    private String DISTRICT;//市
    private String DISTRICT_CODE;//	城市区号
    private String KEY_TAG;//	是否标记
    private String CALL_TIME_LENGTH;//	通话时长（未接通为0）


    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getCALL_SHEET_ID() {
        return CALL_SHEET_ID;
    }

    public void setCALL_SHEET_ID(String CALL_SHEET_ID) {
        this.CALL_SHEET_ID = CALL_SHEET_ID;
    }

    public String getCALL_NO() {
        return CALL_NO;
    }

    public void setCALL_NO(String CALL_NO) {
        this.CALL_NO = CALL_NO;
    }

    public String getCALLED_NO() {
        return CALLED_NO;
    }

    public void setCALLED_NO(String CALLED_NO) {
        this.CALLED_NO = CALLED_NO;
    }

    public String getEND_TIME() {
        return END_TIME;
    }

    public void setEND_TIME(String END_TIME) {
        this.END_TIME = END_TIME;
    }

    public String getCONNECT_TYPE() {
        return CONNECT_TYPE;
    }

    public void setCONNECT_TYPE(String CONNECT_TYPE) {
        this.CONNECT_TYPE = CONNECT_TYPE;
    }

    public String getSTATUS() {
        return STATUS;
    }

    public void setSTATUS(String STATUS) {
        this.STATUS = STATUS;
    }

    public String getDISPOSAL_AGENT() {
        return DISPOSAL_AGENT;
    }

    public void setDISPOSAL_AGENT(String DISPOSAL_AGENT) {
        this.DISPOSAL_AGENT = DISPOSAL_AGENT;
    }

    public String getBEGIN_TIME() {
        return BEGIN_TIME;
    }

    public void setBEGIN_TIME(String BEGIN_TIME) {
        this.BEGIN_TIME = BEGIN_TIME;
    }

    public String getOFFERING_TIME() {
        return OFFERING_TIME;
    }

    public void setOFFERING_TIME(String OFFERING_TIME) {
        this.OFFERING_TIME = OFFERING_TIME;
    }

    public String getRECORD_FILE_NAME() {
        return RECORD_FILE_NAME;
    }

    public void setRECORD_FILE_NAME(String RECORD_FILE_NAME) {
        this.RECORD_FILE_NAME = RECORD_FILE_NAME;
    }

    public String getCUSTOMER_NAME() {
        return CUSTOMER_NAME;
    }

    public void setCUSTOMER_NAME(String CUSTOMER_NAME) {
        this.CUSTOMER_NAME = CUSTOMER_NAME;
    }

    public String getREF_CALL_SHEET_ID() {
        return REF_CALL_SHEET_ID;
    }

    public void setREF_CALL_SHEET_ID(String REF_CALL_SHEET_ID) {
        this.REF_CALL_SHEET_ID = REF_CALL_SHEET_ID;
    }

    public String getPBX() {
        return PBX;
    }

    public void setPBX(String PBX) {
        this.PBX = PBX;
    }

    public String getQUEUE_NAME() {
        return QUEUE_NAME;
    }

    public void setQUEUE_NAME(String QUEUE_NAME) {
        this.QUEUE_NAME = QUEUE_NAME;
    }

    public String getFILE_SERVER() {
        return FILE_SERVER;
    }

    public void setFILE_SERVER(String FILE_SERVER) {
        this.FILE_SERVER = FILE_SERVER;
    }

    public String getPROVINCE() {
        return PROVINCE;
    }

    public void setPROVINCE(String PROVINCE) {
        this.PROVINCE = PROVINCE;
    }

    public String getDISTRICT() {
        return DISTRICT;
    }

    public void setDISTRICT(String DISTRICT) {
        this.DISTRICT = DISTRICT;
    }

    public String getDISTRICT_CODE() {
        return DISTRICT_CODE;
    }

    public void setDISTRICT_CODE(String DISTRICT_CODE) {
        this.DISTRICT_CODE = DISTRICT_CODE;
    }

    public String getKEY_TAG() {
        return KEY_TAG;
    }

    public void setKEY_TAG(String KEY_TAG) {
        this.KEY_TAG = KEY_TAG;
    }

    public String getCALL_TIME_LENGTH() {
        return CALL_TIME_LENGTH;
    }

    public void setCALL_TIME_LENGTH(String CALL_TIME_LENGTH) {
        this.CALL_TIME_LENGTH = CALL_TIME_LENGTH;
    }
}
