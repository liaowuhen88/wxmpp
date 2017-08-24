package com.baodanyun.websocket.controller;

import com.baodanyun.websocket.bean.Response;
import com.baodanyun.websocket.bean.user.AbstractUser;
import com.baodanyun.websocket.exception.BusinessException;
import com.baodanyun.websocket.node.ChatNodeManager;
import com.baodanyun.websocket.service.CustomerDispatcherTactics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by liaowuhen on 2017/8/24.
 */
@RestController
@RequestMapping(value = "closeNodeControl")
public class CloseNodeControl extends BaseController {
    protected static Logger logger = LoggerFactory.getLogger(CloseNodeControl.class);
    @Autowired
    private CustomerDispatcherTactics customerDispatcherTactics;

    @RequestMapping(value = "closeCustomer")
    public Response closeCustomer(String jid, HttpServletRequest request, HttpServletResponse httpServletResponse) {
        Response mv = new Response();

        try {
            AbstractUser customer = customerDispatcherTactics.getCustomerAcceptByJidOnline(jid);
            if (null != customer) {
                ChatNodeManager.getCustomerXmppNode(customer).logout();
            }
            customer = customerDispatcherTactics.getCustomerRefusedJidOnline(jid);
            if (null != customer) {
                ChatNodeManager.getCustomerXmppNode(customer).logout();
            }

        } catch (BusinessException e) {
            logger.error("error", e);

        } catch (Exception e) {
            logger.error("error", e);
        }

        return mv;
    }
}
