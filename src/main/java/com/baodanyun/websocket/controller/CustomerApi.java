package com.baodanyun.websocket.controller;

import com.baodanyun.websocket.bean.Response;
import com.baodanyun.websocket.bean.UserSetPW;
import com.baodanyun.websocket.bean.user.AbstractUser;
import com.baodanyun.websocket.bean.user.Visitor;
import com.baodanyun.websocket.bean.userInterface.PersonalDetail;
import com.baodanyun.websocket.bean.userInterface.user.VcardUserRes;
import com.baodanyun.websocket.bean.userInterface.user.WeiXinListUser;
import com.baodanyun.websocket.bean.userInterface.user.WeiXinUser;
import com.baodanyun.websocket.core.common.Common;
import com.baodanyun.websocket.exception.BusinessException;
import com.baodanyun.websocket.model.PageModel;
import com.baodanyun.websocket.model.Transferlog;
import com.baodanyun.websocket.model.UserModel;
import com.baodanyun.websocket.node.xmpp.VisitorChatNode;
import com.baodanyun.websocket.node.xmpp.XmppNodeManager;
import com.baodanyun.websocket.service.*;
import com.baodanyun.websocket.util.JSONUtil;
import com.baodanyun.websocket.util.PhoneUtils;
import com.baodanyun.websocket.util.Render;
import com.baodanyun.websocket.util.XMPPUtil;
import com.google.gson.Gson;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * Created by yutao on 2016/10/10.
 */
@RestController
public class CustomerApi extends BaseController {

    protected static Logger logger = LoggerFactory.getLogger(CustomerApi.class);


    @Autowired
    private TransferServer transferServer;

    @Autowired
    private VcardService vcardService;

    @Autowired
    private UserCacheServer userCacheServer;

    @Autowired
    private MessageSendToWeixin messageSendToWeixin;

    @Autowired
    private PersonalService personalService;
    @Autowired
    private UserServer userServer;

    @Autowired
    private XmppServer xmppServer;

    @Autowired
    private WebSocketService webSocketService;

    @Autowired
    private CustomerDispatcherService customerDispatcherService;

    /**
     * 获取客服的信息
     *
     * @param cjid
     * @param httpServletResponse
     */

    @RequestMapping(value = "customer/{cjid}", method = RequestMethod.GET)
    public void customer(@PathVariable("cjid") String cjid, HttpServletRequest request, HttpServletResponse httpServletResponse) {
        Response response = new Response();
        try {
            AbstractUser user = (AbstractUser) request.getSession().getAttribute(Common.USER_KEY);
            if (null != user) {
                response.setData(user);
                response.setSuccess(true);
            } else {
                response.setSuccess(false);
                response.setMsg("客服不存在");
            }
        } catch (Exception e) {
            logger.error("error", "获取客服错误", e);
            response.setSuccess(false);
        }
        Render.r(httpServletResponse, JSONUtil.toJson(response));
    }

    /**
     * 修改昵称等信息
     *
     * @param
     * @param httpServletResponse
     */

    @RequestMapping(value = "upCustomerInfo", method = RequestMethod.POST)
    public void upCustomerInfo(UserModel user, HttpServletRequest request, HttpServletResponse httpServletResponse) {
        Response response = new Response();
        boolean flag = false;
        AbstractUser au = (AbstractUser) request.getSession().getAttribute(Common.USER_KEY);
        try {
            if (StringUtils.isEmpty(user.getCjid())) {
                response.setSuccess(false);
                response.setMsg("用户id不能为空");
            } else {
                AbstractUser vcard = vcardService.getVCardUser(au.getId(), au.getId(), AbstractUser.class);

                if (!StringUtils.isEmpty(user.getDesc())) {
                    flag = true;
                    vcard.setDesc(user.getDesc());
                }

                if (!StringUtils.isEmpty(user.getNickName())) {
                    flag = true;
                    vcard.setNickName(user.getNickName());
                }
                if (!StringUtils.isEmpty(user.getIcon())) {
                    flag = true;
                    vcard.setIcon(user.getIcon());
                }

                if (!StringUtils.isEmpty(user.getUserName())) {
                    flag = true;
                    vcard.setUserName(user.getUserName());
                }

                if (flag) {
                    vcardService.updateBaseVCard(au.getId(), Common.userVcard, vcard);
                }

                VcardUserRes vu = new VcardUserRes();
                vu.setUser(au);
                vu.setVcard(vcard);
                response.setData(vu);
                response.setSuccess(true);
            }


        } catch (Exception e) {
            logger.error("error", e);
            response.setSuccess(false);
        }
        Render.r(httpServletResponse, JSONUtil.toJson(response));
    }

    /**
     * 修改客服密码
     *
     * @param
     * @param httpServletResponse
     */

    @RequestMapping(value = "upCustomerPwd", method = RequestMethod.POST)
    public void upCustomerPwd(UserSetPW pw, HttpServletRequest request, HttpServletResponse httpServletResponse) {
        Response response = new Response();
        AbstractUser au = (AbstractUser) request.getSession().getAttribute(Common.USER_KEY);

        try {
            if (!StringUtils.isEmpty(pw.getNewPWD())) {
                if (pw.getNewPWD().trim().equals(pw.getConfirmPWD())) {
                    if (au.getPassWord().equals(pw.getOldPWD())) {
                        boolean flag = vcardService.changePassword(XMPPUtil.jidToName(pw.getCjid()), pw.getNewPWD().trim());
                        if (flag) {
                            response.setMsg("密码修改成功");
                            response.setSuccess(flag);
                        } else {
                            response.setMsg("密码修改失败");
                            response.setSuccess(flag);
                        }

                    } else {
                        response.setSuccess(false);
                        response.setMsg("密码错误");
                    }
                } else {
                    response.setSuccess(false);
                    response.setMsg("您两次输入的密码不一致");
                }
            } else {
                response.setSuccess(false);
                response.setMsg("新密码不能为空");
            }

        } catch (Exception e) {
            logger.error("error", e);
            response.setSuccess(false);
            response.setMsg("系统异常");
        }

        Render.r(httpServletResponse, JSONUtil.toJson(response));
    }


    /**
     * 当前在线可接入用户客服
     *
     * @param httpServletRequest
     * @param httpServletResponse
     */
    @RequestMapping(value = "customerListAccept")
    public void customerListAccept(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        Response response = new Response();
        Gson gson = new Gson();
        try {
            Collection collection = customerDispatcherService.getCustomerAccept();
            response.setData(collection);
            response.setSuccess(true);
        } catch (Exception e) {
            logger.error("error", e);
            response.setSuccess(false);
        }
        Render.r(httpServletResponse, gson.toJson(response));
    }

    /**
     * 客服退出
     *
     * @param httpServletRequest
     * @param httpServletResponse
     */
    @RequestMapping(value = "customerLogout")
    public void customerLogout(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        Response response = new Response();
        Gson gson = new Gson();
        try {
            logger.info("customerLogout");
            // 清楚session缓存
            AbstractUser customer = (AbstractUser) httpServletRequest.getSession().getAttribute(Common.USER_KEY);
            response.setSuccess(true);

            // 关闭node
            XmppNodeManager.getCustomerXmppNode(customer).logout();
            httpServletRequest.getSession().invalidate();

        } catch (Exception e) {
            logger.error("error", e);
            response.setSuccess(false);
        }
        Render.r(httpServletResponse, gson.toJson(response));
    }

    /**
     * 获取其他线上队列为
     *
     * @param httpServletResponse
     */
    @RequestMapping(value = "freeCustomerList")
    public void freeCustomerList(HttpServletResponse httpServletResponse) {
        Response response = new Response();
        Gson gson = new Gson();
        try {
            Collection<AbstractUser> freeCustomerNodeList = userCacheServer.getCustomers().values();
            response.setData(gson.toJson(freeCustomerNodeList));
            response.setSuccess(true);
        } catch (Exception e) {
            logger.error("error", e);
            response.setSuccess(false);
        }
        Render.r(httpServletResponse, gson.toJson(response));
    }

    /**
     * 获取在线的客服节点列表
     *
     * @param httpServletResponse
     */

    @RequestMapping(value = "onlineCustomerList")
    public void onlineCustomerList(PageModel model, HttpServletResponse httpServletResponse) {
        Response response = new Response();
        try {
            Collection<AbstractUser> freeCustomerNodeList = userCacheServer.getCustomers().values();

            if (null != freeCustomerNodeList) {
                Iterator<AbstractUser> it = freeCustomerNodeList.iterator();
                while (it.hasNext()) {
                    AbstractUser au = it.next();
                    if (!xmppServer.isAuthenticated(au.getId())) {
                        it.remove();
                    }
                }
            }
            response.setData(freeCustomerNodeList);
            response.setSuccess(true);
        } catch (Exception e) {
            logger.error("error", e);
            response.setSuccess(false);
        }
        Render.r(httpServletResponse, JSONUtil.toJson(response));
    }

    /**
     * 转接客服
     *
     * @param
     */
    @RequestMapping(value = "changeVisitorTo")
    public void changeVisitorTo(String vjid, String fromJid, String toJid, HttpServletResponse httpServletResponse) {
        Response response = new Response();
        try {
            Transferlog tm = new Transferlog();
            logger.info("vjid[" + vjid + "]----fromJid[" + fromJid + "]----toJid[" + toJid + "]");
            tm.setTransferto(toJid);
            tm.setTransferfrom(fromJid);
            tm.setVisitorjid(vjid);
            tm.setCause("客服主动转接");

            String jid = XMPPUtil.jidToName(vjid);

            Visitor visitor = null;
            if (PhoneUtils.isMobile(jid)) {
                visitor = userServer.initByPhone(jid);
            } else {
                visitor = userServer.initUserByOpenId(jid);
            }


            boolean flag = transferServer.changeVisitorTo(tm, visitor);

            response.setSuccess(flag);
        } catch (Exception e) {
            logger.error("error", e);
            response.setMsg(e.getMessage());
            response.setSuccess(false);
        }
        Render.r(httpServletResponse, JSONUtil.toJson(response));
    }

    /**
     * @param httpServletResponse
     */
    @RequestMapping(value = "weiXinVisitorList")
    public void weiXinVisitorList(String name, String phone, String nickName, String uid, HttpServletRequest request, HttpServletResponse httpServletResponse) {
        Response response = new Response();

        try {
            List<WeiXinListUser> visitors = new ArrayList<>();
            List<WeiXinUser> infos = personalService.getWeiXinUser(uid, name, phone, nickName);

            if (null != infos && infos.size() > 0) {
                for (WeiXinUser info : infos) {
                    WeiXinListUser wu = new WeiXinListUser();
                    //Visitor visitor = userServer.initVisitor(info.getOpenId());
                    AbstractUser customer = userCacheServer.getCustomerByVisitorOpenId(info.getOpenId());
                    wu.setInfo(info);
                    //wu.setUser(info);
                    wu.setCustomer(customer);
                    visitors.add(wu);
                }
            }
            response.setData(visitors);
            response.setSuccess(true);

        } catch (Exception e) {
            logger.error("error", e);
            response.setSuccess(false);
        }

        Render.r(httpServletResponse, JSONUtil.toJson(response));
    }


    /**
     * 修改用户指定的客服
     */
    @RequestMapping(value = "bindCustomer")
    public void bindCustomer(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, String from) throws BusinessException, InterruptedException {
        // 获取当前客服
        // from 为openId
        AbstractUser au = (AbstractUser) httpServletRequest.getSession().getAttribute(Common.USER_KEY);
        Visitor visitor = userServer.initUserByOpenId(from);
        AbstractUser customerFrom = userCacheServer.getCustomerByVisitorOpenId(visitor.getOpenId());
        transferServer.bindVisitor(customerFrom, au, visitor);
        Response response = new Response();
        response.setData(au);
        response.setSuccess(true);

        Render.r(httpServletResponse, JSONUtil.toJson(response));
    }

    /**
     * 获取当前访客详情
     *
     * @param httpServletResponse
     */
    @RequestMapping(value = "visitorDetail")
    public void visitorDetail(String openid, String id, HttpServletRequest request, HttpServletResponse httpServletResponse) {
        Response response = new Response();
        AbstractUser customer = (AbstractUser) request.getSession().getAttribute(Common.USER_KEY);
        try {
            if (!StringUtils.isEmpty(id)) {
                AbstractUser vCard = vcardService.getVCardUser(id, customer.getId(), AbstractUser.class);
                Map<String, Object> map = new HashMap<>();
                map.put("vCard", vCard);

                if (!StringUtils.isEmpty(openid)) {
                    try {
                        PersonalDetail personalDetail = personalService.getPersonalDetail(openid);
                        map.put("basic", personalDetail);
                    } catch (Exception e) {
                        logger.error("第三方接口获取数据出出错", e);
                        response.setMsg("第三方接口获取数据出出错");
                    }
                } else {
                    response.setMsg("openid不能为空");
                }

                response.setData(map);
                response.setSuccess(true);
            } else {
                response.setSuccess(false);
                response.setMsg("用户id不能为空");
            }
        } catch (Exception e) {
            logger.error("error", e);
            response.setSuccess(false);
        }
        Render.r(httpServletResponse, JSONUtil.toJson(response));
    }


    /**
     *
     */
    @RequestMapping(value = "customer_chat")
    public ModelAndView chat(HttpServletRequest request, HttpServletResponse httpServletResponse, String from) throws BusinessException, InterruptedException {
        ModelAndView mv = new ModelAndView();
        AbstractUser au = (AbstractUser) request.getSession().getAttribute(Common.USER_KEY);
        AbstractUser vcard = null;
        try {
            vcard = vcardService.getVCardUser(au.getId(), au.getId(), AbstractUser.class);
        } catch (Exception e) {
            logger.error("error", e);
        }
        VcardUserRes wu = new VcardUserRes();
        wu.setUser(au);
        wu.setVcard(vcard);
        mv.setViewName("/customer/chat");
        mv.addObject("wu", JSONUtil.toJson(wu));

        return mv;

    }


    /**
     * @param vjid
     * @param httpServletResponse
     */
    @RequestMapping(value = "visitorOff/{vjid}")
    public void visitorOff(@PathVariable("vjid") String vjid, HttpServletRequest request, HttpServletResponse httpServletResponse) {
        Response response = new Response();
        try {
            // au 为登录客服
            AbstractUser au = (AbstractUser) request.getSession().getAttribute(Common.USER_KEY);

            Visitor user = new Visitor();
            user.setId(vjid);
            user.setCustomer(au);

            VisitorChatNode vn = XmppNodeManager.getVisitorXmppNode(user);
            vn.uninstall();

            vn.logout();

            response.setSuccess(true);

        } catch (Exception e) {
            logger.error("error", e);
            response.setSuccess(false);
        }
        Render.r(httpServletResponse, JSONUtil.toJson(response));
    }

}
