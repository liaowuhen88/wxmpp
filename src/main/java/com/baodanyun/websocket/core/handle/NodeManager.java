/*
package com.baodanyun.websocket.core.handle;

import com.baodanyun.websocket.bean.conf.CustomerConf;
import com.baodanyun.websocket.bean.user.AbstractUser;
import com.baodanyun.websocket.bean.user.Customer;
import com.baodanyun.websocket.bean.user.Visitor;
import com.baodanyun.websocket.exception.BusinessException;
import com.baodanyun.websocket.model.Transferlog;
import com.baodanyun.websocket.service.CacheService;
import com.baodanyun.websocket.service.HistoryUserServer;
import com.baodanyun.websocket.util.JSONUtil;
import com.baodanyun.websocket.util.SpringContextUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

*/
/**
 * Created by yutao on 2016/9/8.
 *//*

public class NodeManager implements INodeManager {

    protected static Logger logger = LoggerFactory.getLogger(NodeManager.class);
    HistoryUserServer historyUserServer = SpringContextUtil.getBean("historyUserServer", HistoryUserServer.class);

    private static final NodeManager NODE_MANAGER = new NodeManager();

    private NodeManager() {
    }

    public static NodeManager getInstance() {
        return NODE_MANAGER;
    }


    //限制最大接入的客服量
    // key 为id
    private static final Map<String, CustomerNode> CUSTOMER_NODE_MAP = new ConcurrentHashMap<>();
    private static final Map<String, VisitorNode> VISITOR_NODE_MAP = new ConcurrentHashMap<>();


    */
/**
     * 获取闲时客服列表
     * 如果客服节点的在线队列不满 则为空闲节点
     *
     * @return
     *//*

    public List<CustomerNode> freeCustomerNode() {
        List<CustomerNode> freeNodes = new ArrayList<>();

        Collection<CustomerNode> customerNodes = CUSTOMER_NODE_MAP.values();
        if (!CollectionUtils.isEmpty(customerNodes)) {
            for (CustomerNode customerNode : customerNodes) {
                if (customerNode.isOnline()) {
                    CustomerConf customerConf = (CustomerConf) customerNode.getConf();
                    Set<AbstractUser> set =  historyUserServer.get(CacheService.USER_ONLINE,customerNode.getBindUser().getId());

                    if (set.size() < customerConf.getOnlineQueueDefaultSize()) {
                        freeNodes.add(customerNode);
                    }
                }
            }
        }
        return freeNodes;
    }


    */
/**
     * 优先根据用户名密码找客服节点，如果客服节点为空 则初始化一个客服节点
     *
     * @param customer
     * @return
     *//*

    public synchronized CustomerNode getMyCustomerNode(Customer customer) {
        CustomerNode retCustomerNode = CUSTOMER_NODE_MAP.get(customer.getId());
        if (retCustomerNode == null) {
            retCustomerNode = new CustomerNode(customer);
        }
        return retCustomerNode;
    }


    */
/**
     * 获取当前和访客绑定的客服
     *
     * @param visitorNode
     * @return
     *//*

    public synchronized CustomerNode getMyCustomerNode(VisitorNode visitorNode) {
        Visitor visitor = (Visitor) visitorNode.getBindUser();
        return CUSTOMER_NODE_MAP.get(visitor.getCustomerJid());
    }

    @Override
    public boolean updateNodeToBackUpQueue(String visitorNodeId) {
        return false;
    }

    */
/**
     * 根据客服jid获取到当前客服的节点
     *
     * @param customerNodeId
     * @return
     *//*

    public static CustomerNode getCustomerNodeByJid(String customerNodeId) {
        return CUSTOMER_NODE_MAP.get(customerNodeId);
    }

    */
/**
     * 跟进客服jid和访客jid查找到当前的访客节点
     *
     * @param
     * @param visitorNodeId
     * @return
     *//*

    public static VisitorNode getVisitorNodeByJid(String visitorNodeId) {
        return VISITOR_NODE_MAP.get(visitorNodeId);
    }

    */
/**
     * 更新节点到backUp队列
     *
     * @param visitorNodeId
     *//*
*/
/*
    public boolean updateNodeToBackUpQueue(String visitorNodeId) {
        VisitorNode visitorNode = getVisitorNodeByJid(visitorNodeId);
        if (visitorNode != null) {
            return visitorNode.updateNodeToBackUp();
        }
        return false;
    }*//*


    */
/**
     * 更新客服节点的队列大小
     *
     * @param
     * @param
     * @return
     *//*

    public boolean updateNodeQueue(String customerNodeId, int onlineSize) {
        CustomerNode customerNode = getCustomerNodeByJid(customerNodeId);
        if (customerNode != null) {
            CustomerConf customerConf = (CustomerConf) customerNode.getConf();
            if (customerConf != null) {
                customerConf.setOnlineQueueDefaultSize(onlineSize);
                return true;
            }
        }
        return false;
    }

    */
/**
     * 客服退出登录
     *
     * @param customerJid
     *//*

    public void customerLogout(String customerJid) throws InterruptedException {
        CustomerNode customerNode = getCustomerNodeByJid(customerJid);
        if (customerNode != null) {
            customerNode.logout();
        }
    }

    */
/**
     * 获取visitor vcard
     *
     * @param
     * @param
     *//*


    public AbstractUser getVisitor(String vjid) {
        VisitorNode visitorNode = getVisitorNodeByJid(vjid);
        if (visitorNode != null) {
            return visitorNode.getBindUser();
        }

        return null;
    }


    */
/**
     * 获取客服 vcard
     *
     * @param customerJid
     * @param
     *//*


    public AbstractUser getCustomer(String customerJid) {
        CustomerNode customerNode = getCustomerNodeByJid(customerJid);
        if (customerNode != null) {
            return customerNode.getBindUser();
        }

        return null;
    }


    */
/**
     * 访客注销
     *
     * @param visitorJid
     *//*

    public void visitorOff(String visitorJid) throws InterruptedException {
        VisitorNode visitorNode = VISITOR_NODE_MAP.get(visitorJid);
        if (visitorNode != null) {
            visitorNode.logout();
        }
    }

    */
/**
     * 更改访客的客服
     *
     * @param

     *//*

    public static synchronized boolean changeVisitorTo(Transferlog tm) throws BusinessException, InterruptedException {

        logger.info("Transferlog----->" + JSONUtil.toJson(tm));

        if (StringUtils.isEmpty(tm.getVisitorjid())) {
            throw new BusinessException("手机端用户id为空");
        }
        if (StringUtils.isEmpty(tm.getTransferto())) {
            throw new BusinessException("被转接客服账号id为空");
        }

        if (StringUtils.isEmpty(tm.getTransferfrom())) {
            throw new BusinessException("转出客服账号id为空");
        }

        VisitorNode visitorNode = getVisitorNodeByJid(tm.getVisitorjid());

        if (visitorNode == null) {
            throw new BusinessException("手机端用户不在线");
        } else {
            if (visitorNode.isOnline()) {
                CustomerNode fromNode = getCustomerNodeByJid(tm.getTransferfrom());
                CustomerNode toNode = getCustomerNodeByJid(tm.getTransferto());
                if (null != toNode) {
                    if (toNode.isOnline()) {
                        visitorNode.setBindCustomerNode(toNode);
                        if (visitorNode.joinQueue()) {
                            if (fromNode.uninstallVisitorNode(visitorNode)) {
                                return true;
                            } else {
                                throw new BusinessException("从当前客服卸载失败");
                            }
                        } else {
                            throw new BusinessException("加入到目标客服失败");
                        }
                    } else {
                        throw new BusinessException("转出客服已经下线");
                    }
                } else {
                    throw new BusinessException("转出客服为空");
                }


            } else {
                throw new BusinessException("当前访客已经下线");
            }
        }
    }


    */
/**
     * 获取在线的客服列表
     *
     * @return
     *//*


    public List<AbstractUser> onlineCustomerList() {
        List<AbstractUser> nodes = new ArrayList<>();
        Collection<CustomerNode> customerNodes = CUSTOMER_NODE_MAP.values();
        if (!CollectionUtils.isEmpty(customerNodes)) {
            for (CustomerNode customerNode : customerNodes) {
                if (customerNode.isOnline()) {
                    nodes.add(customerNode.getBindUser());
                }
            }
        }
        return nodes;
    }

    public static AbstractUser getUserById(String id) {
        if (null != CUSTOMER_NODE_MAP.get(id)) {
            return CUSTOMER_NODE_MAP.get(id).getBindUser();
        } else if (null != VISITOR_NODE_MAP.get(id)) {
            return VISITOR_NODE_MAP.get(id).getBindUser();
        }

        return null;
    }


    public static Map<String, CustomerNode> getCustomerNodeMap() {
        return CUSTOMER_NODE_MAP;
    }

    public static Map<String, VisitorNode> getVisitorNodeMap() {
        return VISITOR_NODE_MAP;
    }
}
*/
