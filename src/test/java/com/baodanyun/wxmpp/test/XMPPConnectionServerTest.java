package com.baodanyun.wxmpp.test;

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.chat.ChatManager;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class XMPPConnectionServerTest {
    protected static final Logger logger = LoggerFactory.getLogger(XMPPConnectionServerTest.class);
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private AbstractXMPPConnection conn;
    private static ChatManager chatManager;

    private String tousername = "yutao000023@126xmpp";

    private String username = "zwv11132";
    private String password = "00818863ff056f1d66c8427836f94a87";

    private String message = "00000000XCVBNM---我是测试";

    //获取一个新的连接

    /**
     * 穿件连接
     *
     * @throws IOException
     * @throws XMPPException
     * @throws SmackException
     */
    //@Test
    public void connect() throws IOException, XMPPException, SmackException {
        conn = XMPPConnection.connect();

    }

    /**
     * 登录 并且生成 chatManager
     */
    //@Test
    public void login() throws XMPPException, IOException, SmackException {
        connect();
        conn.login(username, password);

    }


    /**
     * 发送消息
     */
    //@Test
    public void sendMessage() throws XMPPException, IOException, SmackException {

        login();

        chatManager = ChatManager.getInstanceFor(conn);

        ChatManagerListenerTest mt = new ChatManagerListenerTest();

        chatManager.addChatListener(mt);

        ChatMessageListenerTest li = new ChatMessageListenerTest();

        Chat newChat = chatManager.createChat(tousername, li);

        newChat.sendMessage(message);
    }

    /**

     *//**
     * 使用了createOrJoin。
     * @param roomName
     *//*
    public void joinRoom(String roomName){

        try {
            // Get the MultiUserChatManager
            MultiUserChatManager manager = MultiUserChatManager.getInstanceFor(conn);

            // Create a MultiUserChat using an XMPPConnection for a room

            String servicename="";
            if("".equals(serviceName.getText())){
                servicename="@127.0.0.1";
            }else{
                servicename=roomName+"@"+serviceName.getText()+".127.0.0.1";
            }
            MultiUserChat muc = manager.getMultiUserChat(servicename);

            // User2 joins the new room using a password and specifying
            // the amount of history to receive. In this example we are requesting the last 5 messages.
            DiscussionHistory history = new DiscussionHistory();
            //不设置即接受所有聊天记录
//          history.setMaxStanzas(5);
            muc.join(username, "", history, conn.getPacketReplyTimeout());
            muc.addMessageListener(new MessageListener() {

                @Override
                public void processMessage(Message msg) {
                    System.out.println("join");
                    textArea.append(msg.getFrom()+":"+msg.getBody());
                    textArea.append("\n");
                    textArea.append("\n");
                    DelayInformation d=(DelayInformation) msg.getExtension("urn:xmpp:delay");
                    //若是自己发送的消息，那stamp==null

                }
            });


            textArea.append("已经加入聊天室"+roomName);
            textArea.append("\n");
        } catch (XMPPErrorException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NoResponseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NotConnectedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void quitRoom(){
        try {
            // Get the MultiUserChatManager
            MultiUserChatManager manager = MultiUserChatManager.getInstanceFor(conn);

            String servicename="";
            if("".equals(serviceName.getText())){
                servicename="@127.0.0.1";
            }else{
                servicename=roomnameField.getText()+"@"+serviceName.getText()+".127.0.0.1";
            }

            MultiUserChat muc = manager.getMultiUserChat(servicename);
            if(muc.isJoined()){
                muc.leave();
                textArea.append(muc.getNickname()+"离开"+muc.getRoom());
                textArea.append("\n");
            }

        } catch (NotConnectedException e) {
            e.printStackTrace();
        }
    }

    *//**
     * * 注册用户信息 * @param user 账号，是用来登陆用的，不是用户昵称 * @param password 账号密码 * @param
     * attributes 账号其他属性，参考AccountManager.getAccountAttributes()的属性介绍 * @return
     *//*
    public boolean registerUser(String user, String password, Map<String, String> attributes) {
        if (!conn.isConnected()) {
            return false;
        }
        try {

            AccountManager.sensitiveOperationOverInsecureConnectionDefault(false);
            AccountManager.getInstance(conn).createAccount(user, password,attributes);
            textArea.append("注册成功!\n");
            return true;
        } catch (NoResponseException | XMPPErrorException | NotConnectedException e) {
            e.printStackTrace();
            textArea.append("注册失败!\n");
            return false;
        }
    }

    public void disConnented(){
        if(conn!=null){
            conn.disconnect();
            textArea.append("注销成功!");
            textArea.append("\n");
            this.setTitle("未登录");
        }

    }

    *//**
     * 创建并加入聊天室
     *//*
    public void createRoom(){
        try{
            // Get the MultiUserChatManager
            MultiUserChatManager manager = MultiUserChatManager.getInstanceFor(conn);

            String servicename="";
            if("".equals(serviceName.getText())){
                servicename="roomnameField.getText()+@127.0.0.1";
            }else{
                servicename=roomnameField.getText()+"@"+serviceName.getText()+".127.0.0.1";
            }

            // Create a MultiUserChat using an XMPPConnection for a room
            MultiUserChat muc =  manager.getMultiUserChat(servicename);

            // Create the room
            muc.create(username);


            // Get the the room's configuration form
            Form form = muc.getConfigurationForm();
            // Create a new form to submit based on the original form
            Form submitForm = form.createAnswerForm();
            // Add default answers to the form to submit
            for (Iterator fields = form.getFields().iterator(); fields.hasNext();) {
                FormField field = (FormField) fields.next();

                if (!FormField.Type.hidden.equals(field.getType()) && field.getVariable() != null) {
                    // Sets the default value as the answer
                    submitForm.setDefaultAnswer(field.getVariable());
                }
            }
            // 设置聊天室是持久聊天室，即将要被保存下来
            submitForm.setAnswer("muc#roomconfig_persistentroom", false);
            // 房间仅对成员开放
            submitForm.setAnswer("muc#roomconfig_membersonly", false);
            // 允许占有者邀请其他人
            submitForm.setAnswer("muc#roomconfig_allowinvites", true);
            // 能够发现占有者真实 JID 的角色
            // submitForm.setAnswer("muc#roomconfig_whois", "anyone");
            // 登录房间对话
            submitForm.setAnswer("muc#roomconfig_enablelogging", true);
            // 仅允许注册的昵称登录
            submitForm.setAnswer("x-muc#roomconfig_reservednick", false);
            // 允许使用者修改昵称
            submitForm.setAnswer("x-muc#roomconfig_canchangenick", false);
            // 允许用户注册房间
            submitForm.setAnswer("x-muc#roomconfig_registration", false);
            //房间最大人数，0表示无限制
            List<String> maxPeopleList=new ArrayList<>();
            maxPeopleList.add("0");
            submitForm.setAnswer("muc#roomconfig_maxusers",maxPeopleList);
            // Send the completed form (with default values) to the server to configure the room
            muc.sendConfigurationForm(submitForm);

            muc.addMessageListener(new MessageListener() {

                @Override
                public void processMessage(Message msg) {
                    textArea.append(msg.getFrom()+":"+msg.getBody());
                    textArea.append("\n");
                    textArea.append("\n");
                    DelayInformation d=(DelayInformation) msg.getExtension("urn:xmpp:delay");
                    //若是自己发送的消息，那stamp==null
                    if(d != null){
                        System.out.println(d.getStamp());
                    }

                }
            });

            textArea.append(roomnameField.getText()+"创建成功!");
            textArea.append("\n");
        }catch(Exception e){
            textArea.append("createRoom.error\n");
            e.printStackTrace();
        }
    }

    public void findExistRooms(){
        try {
            // Get the MultiUserChatManager
            MultiUserChatManager manager = MultiUserChatManager.getInstanceFor(conn);

            // Discover information about the room roomName@conference.myserver
            String servicename="";
            if("".equals(serviceName.getText())){
                servicename="127.0.0.1";
            }else{
                servicename=serviceName.getText()+".127.0.0.1";
            }
            List<HostedRoom> hostedRooms = manager.getHostedRooms(servicename);
            for(HostedRoom r:hostedRooms){

                textArea.append(r.getJid());
                textArea.append(":");
                textArea.append(r.getName());
                textArea.append("\n");
                textArea.append("\n");
            }
        } catch (NoResponseException | XMPPErrorException | NotConnectedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void sendgroupMessage(){
        try {
            MultiUserChatManager manager = MultiUserChatManager.getInstanceFor(conn);
            MultiUserChat muc = manager.getMultiUserChat(roomnameField.getText()+"@"+serviceName.getText()+".127.0.0.1");

            muc.sendMessage(message.getText());
        } catch (NotConnectedException e) {

            e.printStackTrace();
        }
    }*/


}