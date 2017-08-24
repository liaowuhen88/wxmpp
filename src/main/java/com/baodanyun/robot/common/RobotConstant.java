package com.baodanyun.robot.common;

/**
 * 常量
 */
public class RobotConstant {

    /*我要报案缓存key前缀*/
    public static final String ROBOT_KEYP_REFIX = "kf_robot_";

    /*微信文本类型标识*/
    public static final String WECHAT_TEXT = "text";

    /*15分钟  毫秒*/
    public static final long RULE_TIME = 15 * 60 * 1000;

    public static final String REPORT_CASE = "我要报案";
    public static final String FINISH = "Y";
    public static final String CLOSE = "关闭";

    public static final String NOT_REGIST_TIP = "欢迎进入豆包自助报案服务，抱歉，此次自助服务关闭，请注册登录豆包管家，如有问题请联系客服核实。";

    public static final String HAS_REGIST_TIP = "欢迎进入豆包自助报案服务，请上传以下图片，全部上传完请输入'Y'结束。\n(1)出险人身份证正反面(2)银行卡正方面(3)医疗费用原始收据(4)医疗费用明细清单(5)门急诊病历或住院小结(6)处方。\n注意：请在15分钟内完成整个操作，否则需要重新申请，也可回复'关闭'，终止此次自助服务。";

    public static final String SUCCESS_TIP = "您的理赔申请已经提交成功，此次自助服务关闭。";

    public static final String FORBIDDEN_TEXT = "您输入有误，全部上传完请输入'Y'结束，终止此次自助服务请输入'关闭'。";

    public static final String EXPIRE_TIP = "此次自助服务已超过15分钟,服务关闭。";
}
