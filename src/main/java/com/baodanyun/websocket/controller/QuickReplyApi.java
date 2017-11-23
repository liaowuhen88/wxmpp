package com.baodanyun.websocket.controller;

import com.baodanyun.websocket.bean.QuickReply;
import com.baodanyun.websocket.bean.Response;
import com.baodanyun.websocket.service.QuickReplyServer;
import com.baodanyun.websocket.util.JSONUtil;
import com.baodanyun.websocket.util.Render;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by liaowuhen on 2016/11/11.
 */
@RestController
public class QuickReplyApi extends BaseController {
    private static final String GREET = "问候";
    private static final String SETTLEMENTOFCLAIMS = "理赔";
    private static final String HEATH = "健康";
    private static final String MALL = "商城";
    private static final String PHY = "体检";
    private static final String WARRANTY = "保单";
    protected static Logger logger = LoggerFactory.getLogger(CustomerApi.class);
    @Autowired
    private QuickReplyServer quickReplyServer;

    public static void main(String[] args) {
        String cid = "zhassngqike@126xmpp";
        // 显示条数
        int display = 20;
        //logger.info(cid.hashCode()+"");
        QuickReplyApi qa = new QuickReplyApi();
        List<QuickReply> li = qa.getListCode(cid, display, 1);


    }

    @RequestMapping(value = "updateQuickReply")
    public void updateQuickReply(String cjid, String message, HttpServletResponse httpServletResponse) {
        Response response = new Response();
        try {
            if (StringUtils.isEmpty(cjid)) {
                response.setMsg("cjid参数异常");
                response.setSuccess(false);

            } else {
                List<QuickReply> li = null;
                if (!StringUtils.isEmpty(message)) {
                    Integer co = quickReplyServer.addQuickReply(cjid, message);
                }

                if (cjid.equals("yutao@126xmpp") || cjid.equals("zhangqike@126xmpp") || cjid.equals("zhangchi@126xmpp")) {
                    li = getListCode(cjid, 20, 1);
                } else {
                    li = quickReplyServer.getQuickReply(cjid);
                }

                response.setData(li);
                response.setSuccess(true);


            }

        } catch (Exception e) {
            logger.error("error", e);
            response.setMsg("update error");
            response.setSuccess(false);
        }
        Render.r(httpServletResponse, JSONUtil.toJson(response));
    }

    @RequestMapping(value = "changeQuickReply")
    public void changeQuickReply(String cjid, HttpServletRequest request, HttpServletResponse httpServletResponse) {
        Response response = new Response();
        try {
            Integer count = 1;
            Object obj = request.getSession().getAttribute(cjid);
            if (null != obj) {
                count = (Integer) obj;
            }
            count++;
            request.getSession().setAttribute(cjid, count);
            if (StringUtils.isEmpty(cjid)) {
                response.setMsg("cjid参数异常");
                response.setSuccess(false);

            } else {
                List<QuickReply> li = getListCode(cjid, 10, count);

                response.setData(li);
                response.setSuccess(true);


            }

        } catch (Exception e) {
            logger.error("error", e);
            response.setMsg("update error");
            response.setSuccess(false);
        }
        Render.r(httpServletResponse, JSONUtil.toJson(response));
    }

    @RequestMapping(value = "deleteQuickReply")
    public void deleteQuickReply(Integer id, HttpServletResponse httpServletResponse) {
        Response response = new Response();
        try {
            if (null != id) {
                int count = quickReplyServer.deleteQuickReply(id);
                response.setData(count);
                response.setSuccess(true);
            } else {
                response.setMsg("参数为空");
                response.setSuccess(false);
            }

        } catch (Exception e) {
            logger.error("error", e);
            response.setMsg(e.getMessage());
            response.setSuccess(false);
        }
        Render.r(httpServletResponse, JSONUtil.toJson(response));
    }

    public List<QuickReply> getListCode(String cid, int display, int count) {

        List<QuickReply> li = getList(cid);
        //logger.info(JSONUtil.toJson(li.size()));
        List<QuickReply> subList = new ArrayList<>();
        //int code = getCode(cid,li.size(),display,count);
        for (int i = 0; i < display; i++) {
            Random random = new Random();
            int s = random.nextInt(li.size()) % (li.size() + 1);
            subList.add(li.get(s));
        }

        //List<QuickReply> subList = li.subList(code, code+display);

        logger.info(JSONUtil.toJson(subList));

        return subList;
    }

    /**
     * 获取数组开始位置
     *
     * @return
     */
    public int getCode(String cid, int size, int display, int page) {
        int code = Math.abs(cid.hashCode()) % size + display;

        if (code > size) {
            code = code - (2) * display;
        }

        logger.info(code + "");

        return code;
    }

    public List<QuickReply> getList(String cid) {
        StringBuffer sb = new StringBuffer();
        sb.append("[");
        // 问候
        sb.append(getMessage(QuickReplyApi.GREET, "请问有什么可以帮助您的？", cid));
        sb.append(getMessage(QuickReplyApi.GREET, "有什么需要为您服务的？", cid));
        sb.append(getMessage(QuickReplyApi.GREET, "您可以留一个联系方式或者微信号  我们会有专属客服为您详细解答", cid));
        sb.append(getMessage(QuickReplyApi.GREET, "http://mp.weixin.qq.com/s/lIpTtShaBMbb2D54gJeHBQ激活流程，请参考一下", cid));
        sb.append(getMessage(QuickReplyApi.GREET, "您好，请将您咨询的问题直接发送到豆包管家，我们在豆包管家为您提供服务，您现在的接入方式，提示信息不完整，还请您直接通过豆包管家联系我们，谢谢O(∩_∩)O~", cid));
        sb.append(getMessage(QuickReplyApi.GREET, "您好，我是您的专属客服，您提交的理赔报案号为：提示是问题件，您需要根据提示 补充提供相应的就诊病例，您补充好 病例 通过豆包管家 理赔进度，找到问题件，点击问题件处理， 将病例补传就可以，有问题 请随时联系我们。", cid));
        sb.append(getMessage(QuickReplyApi.GREET, "烦请您点击“个人中心”——“查看更多功能”，您会看到右上角有个齿轮状图形，点开后完善资料。这样我能更准确有效的为您服务。", cid));
        sb.append(getMessage(QuickReplyApi.GREET, "我们这里有保险界的权威专家，您的问题，将由他给您进行解答，您的手机号就是您的微信号吧，我们的保险专家会加您。给您最好的搭配组合。", cid));
        sb.append(getMessage(QuickReplyApi.GREET, "进入豆包管家点击个人中心，-- 进入个人中心点击右上角查看更多功能，再次点击右上角点击小齿轮按钮—进入后完善个人信息（输入名字 绑定手机号身份证号码）----激活完成", cid));


        //理赔
        sb.append(getMessage(QuickReplyApi.SETTLEMENTOFCLAIMS, "您好  您可以撤销重新提交理赔", cid));
        sb.append(getMessage(QuickReplyApi.SETTLEMENTOFCLAIMS, "提交申请后三个工作日审核", cid));
        sb.append(getMessage(QuickReplyApi.SETTLEMENTOFCLAIMS, "理赔资料：病历，处方，有税务印章的收费发票及医保结算联，如果有检查还需要提供检查报告，首次提交需要提交银行卡和身份证的影像件", cid));
        sb.append(getMessage(QuickReplyApi.SETTLEMENTOFCLAIMS, "您点击微信公众号--豆包管家-个人中心-理赔申请，然后上传您的理赔申请的资料即可", cid));
        sb.append(getMessage(QuickReplyApi.SETTLEMENTOFCLAIMS, "您好，您成功提交理赔申请后的三个工作日咱们的理赔工作人员会帮您进行初审，通过豆包网审核后10-15个工作日保险公司会审核结案，结案后三个工作日保险公司会安排打款，这是正常的理赔时效。", cid));
        sb.append(getMessage(QuickReplyApi.SETTLEMENTOFCLAIMS, "是这样的，咱们企业医疗补充方案只是针对我们发票当中自付一医保范围内的部分可以报销，其余部分是不在保障范围内的。", cid));
        sb.append(getMessage(QuickReplyApi.SETTLEMENTOFCLAIMS, "在非社保地就诊，需就诊于二级及二级以上公立医院急诊，回到社保地将票据带回给人事进行社保分割，分割后将分割单和您的就诊资料再咱们这申请二次报销，提示您将就诊票据和资料交人事前留存影像或复印件。", cid));
        sb.append(getMessage(QuickReplyApi.SETTLEMENTOFCLAIMS, "http://mp.weixin.qq.com/s/P5_7MnLq2cKQKRvHw1opzw 理赔流程，请参一下", cid));
        sb.append(getMessage(QuickReplyApi.SETTLEMENTOFCLAIMS, "您的理赔已经成功提交，工作人员会为您进行审核，请您关注后续信息变化", cid));
        sb.append(getMessage(QuickReplyApi.SETTLEMENTOFCLAIMS, "您提供下发票抬头，纳税人识别号，收件人姓名手机号和邮寄地址，我先帮您登记一下", cid));
        sb.append(getMessage(QuickReplyApi.SETTLEMENTOFCLAIMS, "系统申请金额是自动带出的，不会影响您的正常报销，保险公司会以您的实际票据为准进行审核的，您可以放心。", cid));
        sb.append(getMessage(QuickReplyApi.SETTLEMENTOFCLAIMS, "您进入豆包管家-个人中心-理赔报案，点击查看更多，找到这笔问题件，点击问题件处理", cid));
        sb.append(getMessage(QuickReplyApi.SETTLEMENTOFCLAIMS, "结案后保险公司会在三个工作日安排打款，请您近两天多关注一下银行卡流水。", cid));
        sb.append(getMessage(QuickReplyApi.SETTLEMENTOFCLAIMS, "您好，您的心情我能理解，我们一定会加快处理的，希望您能理解，我们会第一时间和保险公司联系尽快给您回复。最长时间是2个工作日。很抱歉给您带来不便。", cid));
        sb.append(getMessage(QuickReplyApi.SETTLEMENTOFCLAIMS, "您好，感谢您的耐心等待，针对您反馈的问题我会第一时间提交到理赔部门，会有我们理赔部门的同事和保险公司工作人员确认， 2个工作日内给您回复。请问还有什么其他需要帮助的吗？", cid));
        sb.append(getMessage(QuickReplyApi.SETTLEMENTOFCLAIMS, "您好，您成功提交理赔申请后的三个工作日咱们的理赔工作人员会帮您进行初审，通过豆包网审核后10个工作日保险公司会审核结案，结案后三个工作日保险公司会安排打款", cid));
        sb.append(getMessage(QuickReplyApi.SETTLEMENTOFCLAIMS, "您好，免赔额是投保的合同规定的， 是一个保险年度内 符合报销要求的金额中有一百是不报的，  如果一个案件中可报销的金额不够， 保险公司系统会自动累计，在接下来的案件中 将剩余没有扣除的金额 扣除，  直到一百扣完为止， 再有新的理赔就会根据您的 赔付比例进行赔付，", cid));
        sb.append(getMessage(QuickReplyApi.SETTLEMENTOFCLAIMS, "您好 咱们的企补是基于社保范围内的  需要持社保卡就诊于19家A类免指医院或指定医院 或中医院 专科医院  社保结算后 发票上有自付一的金额可以申请企补二次报销", cid));
        sb.append(getMessage(QuickReplyApi.SETTLEMENTOFCLAIMS, "您好，您只要是持医保卡在您的定点医院，北京19家A类免指医院或中医院，专科医院就诊，医保结算后发票上自付一的金额都是可以在咱们这申请补充医疗报销。", cid));


        //健康
        sb.append(getMessage(QuickReplyApi.HEATH, "我们豆包网是一家优质的保障产品优选服务平台，我们为您提供保险及健康管理优选组合服务。我们会有专业的健康管理师在线服务，也有保障专家根据您的需求为您提供优选保险计划。想要健康保障，一定找我们豆包哦！", cid));

        //商城
        sb.append(getMessage(QuickReplyApi.MALL, "有时间您可以浏览一下咱们的豆包商城，体检，理赔，保险，健康等相关的信息都有的，短期的意外险，旅游险，和类似咱们企补的医疗险都是可以的，有需要您可以随时联系我们", cid));
        sb.append(getMessage(QuickReplyApi.MALL, "您如果有保险的需求，也是可以联系我们的，我们对您公司的方案还是比较了解的，我们知道怎样帮您做最好的搭配和组合的", cid));
        sb.append(getMessage(QuickReplyApi.MALL, "您如果有保险的需求呢，可以来找我们，我们对您公司的方案还是比较了解的，我们知道怎么样帮您做最好的搭配和组合。", cid));
        sb.append(getMessage(QuickReplyApi.MALL, "感谢您使用豆包网。如果您自付部分承担的费用比较高，是有商业保险能够帮您解决这方面的问题的，如果您有需求可以来联系我们。", cid));
        sb.append(getMessage(QuickReplyApi.MALL, "贵公司的体检是专属定制的，有限制每个账户只能购买一次，如果当时扫码未进行付款就不能再进行购买了，麻烦您提供下购买时登记的姓名，我帮您查询一下", cid));
        sb.append(getMessage(QuickReplyApi.MALL, "看您在豆包管家回答了问卷，您对商业险是比较关注的，咱们现在有补充医疗，多一重保障，咱们的家人是否也有多重保障呢", cid));
        sb.append(getMessage(QuickReplyApi.MALL, "看您浏览了豆包商城豆包华夏福重大疾病保险计划，不知道您有什么疑问，我可以帮到您", cid));
        sb.append(getMessage(QuickReplyApi.MALL, "我们可以为您提供一个可以报销自付二费用的计划，最高上限20万，我把介绍发给您，你可以看一下，有需要您可以随时和我联系。https://mp.weixin.qq.com/s/SuENdoEJvs8Lkp8r2zfC7w", cid));
        sb.append(getMessage(QuickReplyApi.MALL, "如果您还需要其他的保险健康组合，或者是体检方面的需求，都可以联系我们为您量身定制，进行方案搭配。", cid));

        //保单
        sb.append(getMessage(QuickReplyApi.MALL, "您在豆包管家-我的保单-可以看到问卷，完成问卷即可领取", cid));

        //体检
        sb.append(getMessage(QuickReplyApi.PHY, "请您体检前一天注意清淡饮食，勿饮酒，勿服用药物（降糖药，降压药除外），晚8点后，请您避免进食，可适量喝白开水，请注意休息，避免过度疲劳，剧烈运动，以免影响体检结果。[微笑]体检当天空腹携带本人身份证早上8-10点到检。", cid));
        sb.append(getMessage(QuickReplyApi.PHY, "在个人中心，体检预约，选择门店和体检时间，根据提示可自助提交申请即可", cid));
        sb.append(getMessage(QuickReplyApi.PHY, "预约流程 关注豆包微信公众平台—豆包管家 进入豆包管家点击个人中心，-- 进入个人中心点击右上角查看更多功能，再次点击右上角点击小齿轮按钮—进入后完善个人信息（输入名字 绑定手机号身份证号码）----激活完成 在个人中心，体检预约，选择门店和体检时间，根据提示可自助提交申请即可", cid));
        sb.append(getMessageEnd(QuickReplyApi.PHY, "因为涉及到 客户的隐私，我们是看不到的， 体检后一般 5-7个工作日会出电子版报告， 出来后，我们的健康老师会直接上传到 豆包管家里，可以通过豆包管家 健康报告查询", cid).toString());

        sb.append("]");

        //System.out.println(sb.toString());
        java.lang.reflect.Type InfoType = new TypeToken<List<QuickReply>>() {
        }.getType();
        List<QuickReply> infos = JSONUtil.fromJson(sb.toString(), InfoType);

        return infos;
    }

    public String getMessage(String tag, String content, String addid) {
        StringBuffer sb = getMessageEnd(tag, content, addid);
        sb.append(",");
        return sb.toString();
    }

    public StringBuffer getMessageEnd(String tag, String content, String addid) {
        StringBuffer sb = new StringBuffer();
        sb.append("{");
        sb.append("\"tag\": \"" + tag + "\"");
        sb.append(",");
        sb.append("\"message\": \"" + content + "\"");
        sb.append(",");
        sb.append("\"addid\": \"" + addid + "\"");
        sb.append("}");
        return sb;
    }
}
