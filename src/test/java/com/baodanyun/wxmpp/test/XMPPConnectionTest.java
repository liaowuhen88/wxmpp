package com.baodanyun.wxmpp.test;

import com.baodanyun.websocket.util.Config;
import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.ConnectionConfiguration.SecurityMode;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.SmackException.NoResponseException;
import org.jivesoftware.smack.SmackException.NotConnectedException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.XMPPException.XMPPErrorException;
import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.chat.ChatManager;
import org.jivesoftware.smack.chat.ChatManagerListener;
import org.jivesoftware.smack.chat.ChatMessageListener;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jivesoftware.smackx.delay.packet.DelayInformation;
import org.jivesoftware.smackx.iqregister.AccountManager;
import org.jivesoftware.smackx.muc.DiscussionHistory;
import org.jivesoftware.smackx.muc.HostedRoom;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.jivesoftware.smackx.muc.MultiUserChatManager;
import org.jivesoftware.smackx.xdata.Form;
import org.jivesoftware.smackx.xdata.FormField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.*;
import java.util.List;

public class XMPPConnectionTest extends JFrame {
    protected static final Logger logger = LoggerFactory.getLogger(XMPPConnectionTest.class);
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private static AbstractXMPPConnection conn;

    private static ChatManager chatManager;

    private String username = "未登录";
    private String password = "00818863ff056f1d66c8427836f94a87";

//  private static String username="test01";
//  private static String password="1234567";


    private JTextField usernameField = new JTextField(10);
    private JTextField passwordField = new JTextField(10);
    private JButton loginBtn = new JButton("登录");
    private JButton registerBtn = new JButton("注册");
    private JButton logoutBtn = new JButton("注销");

    private JTextField receiverName = new JTextField(10);
    private JTextField message = new JTextField(10);
    private JButton button = new JButton("发送");
    private JButton sendGroupBtn = new JButton("群送");
    private JButton createRoombutton = new JButton("创建房间");
    private JButton joinRoombutton = new JButton("加入房间");

    private JTextField roomnameField = new JTextField(10);

    private JTextField serviceName = new JTextField("conference", 10);

    private JTextArea textArea = new JTextArea(10, 20);

    public XMPPConnectionTest() {
        connect();

        this.setTitle(username);
        this.setSize(800, 500);

        this.setLayout(new FlowLayout());
        this.add(new JLabel("用户名"));
        this.add(usernameField);

        this.add(new JLabel("密码"));
        this.add(passwordField);
        this.add(loginBtn);
        //登录按钮点击事件
        loginBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                login();
            }
        });

        this.add(registerBtn);
        //注册按钮点击事件
        registerBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registerUser(usernameField.getText(), passwordField.getText(), new HashMap<String, String>());
            }
        });

        this.add(logoutBtn);
        //注销按钮点击事件
        logoutBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                disConnented();
            }
        });

        this.add(new JLabel("接收人"));
        this.add(receiverName);

        this.add(new JLabel("消息"));
        this.add(message);

        this.add(button);
        //发送按钮点击事件
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendMessage(receiverName.getText(), message.getText());
            }
        });

        this.add(sendGroupBtn);
        //群发按钮点击事件
        sendGroupBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendgroupMessage();
            }
        });


        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(this.EXIT_ON_CLOSE);
        //关闭窗口事件
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                disConnented();
                super.windowClosing(e);
            }
        });

        this.add(new JLabel("房间名"));
        this.add(roomnameField);

        this.add(new JLabel("serviceName"));
        this.add(serviceName);

        this.add(createRoombutton);
        //创建房间事件
        createRoombutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createRoom();
            }
        });

        this.add(joinRoombutton);
        joinRoombutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                joinRoom(roomnameField.getText());
            }
        });

        JButton quitRoomBtn = new JButton("离开房间");
        this.add(quitRoomBtn);
        quitRoomBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                quitRoom();
            }
        });

        JButton findAllRoomsBtn = new JButton("搜索所有房间");
        this.add(findAllRoomsBtn);
        findAllRoomsBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                findExistRooms();
            }
        });

        textArea.setLineWrap(true);        //激活自动换行功能
        textArea.setWrapStyleWord(true);   // 激活断行不断字功能
        JScrollPane jsp = new JScrollPane(textArea);
        this.add(jsp);
        this.setVisible(true);
    }

    //获取一个新的连接
    public void connect() {
        XMPPTCPConnectionConfiguration config = XMPPTCPConnectionConfiguration.builder()
                .setServiceName(Config.xmppdomain).setHost(Config.xmppurl)
                .setPort(Integer.parseInt(Config.xmppport)).setCompressionEnabled(true).setSecurityMode(SecurityMode.disabled).build();

        conn = new XMPPTCPConnection(config);
        try {
            conn.connect();
        } catch (SmackException | IOException | XMPPException e1) {

            e1.printStackTrace();
        }
    }

    public void login() {
        try {
            this.username = this.usernameField.getText();
            this.password = this.passwordField.getText();
            if (conn == null || !conn.isConnected()) {
                connect();
            }
            conn.login(username, password);

            Presence presence = new Presence(Presence.Type.available);   //这里如果改成unavailable则会显示用户不在线
            presence.setStatus("Go fishing");
            conn.sendPacket(presence);


            this.setTitle(username);

            textArea.append(username + ":connected");
            textArea.append("\n");

            //添加消息接受器
            chatManager = ChatManager.getInstanceFor(conn);
            chatManager.addChatListener(new ChatManagerListener() {
                @Override
                public void chatCreated(Chat arg0, boolean arg1) {
                    //if (!arg1) {
                    arg0.addMessageListener(new ChatMessageListener() {
                        @Override
                        public void processMessage(Chat arg0, Message msg) {
                            textArea.append(msg.getFrom() + ":" + msg.getBody());
                            textArea.append("\n");
                        }
                    });
                    /// }
                }
            });

        } catch (SmackException | IOException | XMPPException e) {
            e.printStackTrace();
            textArea.append("登陆失败");
            textArea.append("\n");
        }
    }


    public void sendMessage(String toUsername, String message) {
        //toUsername = "jsmith@jivesoftware.com";
        Chat newChat = chatManager.createChat(toUsername, new ChatMessageListener() {
            @Override
            public void processMessage(Chat arg0, Message arg1) {
                textArea.append("已发送:" + arg1 + "\n");
                textArea.append("\n");
            }
        });
        try {
            newChat.sendMessage(message);
        } catch (Exception e) {
            e.printStackTrace();
            ;
            textArea.append("Error Delivering block\n");
        }
    }

    ;

    /**
     * 使用了createOrJoin。
     *
     * @param roomName
     */
    public void joinRoom(String roomName) {

        try {
            // Get the MultiUserChatManager
            MultiUserChatManager manager = MultiUserChatManager.getInstanceFor(conn);

            // Create a MultiUserChat using an XMPPConnection for a room

            String servicename = "";
            if ("".equals(serviceName.getText())) {
                servicename = "@127.0.0.1";
            } else {
                servicename = roomName + "@" + serviceName.getText() + ".127.0.0.1";
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
                    textArea.append(msg.getFrom() + ":" + msg.getBody());
                    textArea.append("\n");
                    textArea.append("\n");
                    DelayInformation d = (DelayInformation) msg.getExtension("urn:xmpp:delay");
                    //若是自己发送的消息，那stamp==null

                }
            });


            textArea.append("已经加入聊天室" + roomName);
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

    public void quitRoom() {
        try {
            // Get the MultiUserChatManager
            MultiUserChatManager manager = MultiUserChatManager.getInstanceFor(conn);

            String servicename = "";
            if ("".equals(serviceName.getText())) {
                servicename = "@127.0.0.1";
            } else {
                servicename = roomnameField.getText() + "@" + serviceName.getText() + ".127.0.0.1";
            }

            MultiUserChat muc = manager.getMultiUserChat(servicename);
            if (muc.isJoined()) {
                muc.leave();
                textArea.append(muc.getNickname() + "离开" + muc.getRoom());
                textArea.append("\n");
            }

        } catch (NotConnectedException e) {
            e.printStackTrace();
        }
    }

    /**
     * * 注册用户信息 * @param user 账号，是用来登陆用的，不是用户昵称 * @param password 账号密码 * @param
     * attributes 账号其他属性，参考AccountManager.getAccountAttributes()的属性介绍 * @return
     */
    public boolean registerUser(String user, String password, Map<String, String> attributes) {
        if (!conn.isConnected()) {
            return false;
        }
        try {

            AccountManager.sensitiveOperationOverInsecureConnectionDefault(false);
            AccountManager.getInstance(conn).createAccount(user, password, attributes);
            textArea.append("注册成功!\n");
            return true;
        } catch (NoResponseException | XMPPErrorException | NotConnectedException e) {
            e.printStackTrace();
            textArea.append("注册失败!\n");
            return false;
        }
    }

    public void disConnented() {
        if (conn != null) {
            conn.disconnect();
            textArea.append("注销成功!");
            textArea.append("\n");
            this.setTitle("未登录");
        }

    }

    /**
     * 创建并加入聊天室
     */
    public void createRoom() {
        try {
            // Get the MultiUserChatManager
            MultiUserChatManager manager = MultiUserChatManager.getInstanceFor(conn);

            String servicename = "";
            if ("".equals(serviceName.getText())) {
                servicename = "roomnameField.getText()+@127.0.0.1";
            } else {
                servicename = roomnameField.getText() + "@" + serviceName.getText() + ".127.0.0.1";
            }

            // Create a MultiUserChat using an XMPPConnection for a room
            MultiUserChat muc = manager.getMultiUserChat(servicename);

            // Create the room
            muc.create(username);


            // Get the the room's configuration form
            Form form = muc.getConfigurationForm();
            // Create a new form to submit based on the original form
            Form submitForm = form.createAnswerForm();
            // Add default answers to the form to submit
            for (Iterator fields = form.getFields().iterator(); fields.hasNext(); ) {
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
            List<String> maxPeopleList = new ArrayList<>();
            maxPeopleList.add("0");
            submitForm.setAnswer("muc#roomconfig_maxusers", maxPeopleList);
            // Send the completed form (with default values) to the server to configure the room
            muc.sendConfigurationForm(submitForm);

            muc.addMessageListener(new MessageListener() {

                @Override
                public void processMessage(Message msg) {
                    textArea.append(msg.getFrom() + ":" + msg.getBody());
                    textArea.append("\n");
                    textArea.append("\n");
                    DelayInformation d = (DelayInformation) msg.getExtension("urn:xmpp:delay");
                    //若是自己发送的消息，那stamp==null
                    if (d != null) {
                        System.out.println(d.getStamp());
                    }

                }
            });

            textArea.append(roomnameField.getText() + "创建成功!");
            textArea.append("\n");
        } catch (Exception e) {
            textArea.append("createRoom.error\n");
            e.printStackTrace();
        }
    }

    public void findExistRooms() {
        try {
            // Get the MultiUserChatManager
            MultiUserChatManager manager = MultiUserChatManager.getInstanceFor(conn);

            // Discover information about the room roomName@conference.myserver
            String servicename = "";
            if ("".equals(serviceName.getText())) {
                servicename = "127.0.0.1";
            } else {
                servicename = serviceName.getText() + ".127.0.0.1";
            }
            List<HostedRoom> hostedRooms = manager.getHostedRooms(servicename);
            for (HostedRoom r : hostedRooms) {

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

    public void sendgroupMessage() {
        try {
            MultiUserChatManager manager = MultiUserChatManager.getInstanceFor(conn);
            MultiUserChat muc = manager.getMultiUserChat(roomnameField.getText() + "@" + serviceName.getText() + ".127.0.0.1");

            muc.sendMessage(message.getText());
        } catch (NotConnectedException e) {

            e.printStackTrace();
        }
    }


}