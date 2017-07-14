<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="zh-CN">
<%
    String path = request.getContextPath();%>
<head>
    <meta charset="UTF-8"/>
    <title>豆包客服</title>
    <script>
        window.base = '<%=path%>';
    </script>
    <jsp:include page="/tpl/common/commonjs.jsp"/>
    <jsp:include page="common/common.jsp"/>
    <link type="text/css" href="<%=request.getContextPath()%>/resouces/js/webuploader/css/webuploader.css"/>
    <script src="<%=request.getContextPath()%>/resouces/js/webuploader/webuploader.js"></script>
    <script src="<%=request.getContextPath()%>/resouces/js/webuploader/uploadimg.js"></script>
    <script src="<%=request.getContextPath()%>/resouces/ajax/common.js"></script>
    <script src="<%=request.getContextPath()%>/resouces/scripts/laypage/laypage.js"></script>
    <script src="<%=request.getContextPath()%>/resouces/scripts/chat.js"></script>
    <script src="<%=request.getContextPath()%>/resouces/scripts/laypage/laypage.js"></script>
    <script src="<%=request.getContextPath()%>/resouces/scripts/history.js"></script>
    <script src="<%=request.getContextPath()%>/resouces/scripts/set.js"></script>
    <script src="<%=request.getContextPath()%>/resouces/scripts/leaveMessage.js"></script>
    <script src="<%=request.getContextPath()%>/resouces/js/wechatface.js"></script>

    <script>
        $(function () {

            var wu = ${wu};
            window.user = wu.vcard;
            window.currentId = wu.user.id;

            var chat = new Chat();
            var history = new History();
            var set = new Set();
            var leaveMessage = new LeaveMessage();
            //页面初始化建立连接
            xchat.connect();
            chat.init();
            history.init();
            set.init();
            leaveMessage.init();
        });
    </script>
</head>

<body>
<%-- 历史记录 --%>
<jsp:include page="history.jsp"/>
<%-- 个人中心设置 --%>
<jsp:include page="set.jsp"/>
<%-- 离线消息 --%>
<jsp:include page="leaveMessage.jsp"/>
<%-- 等待列表 --%>
<div class="modal" data-target="holdListBtn">
    <div class="modal_backdrop"></div>
    <div class="modal_dialog">
        <div class="modal_content">
            <div class="modal_header">
                <span class="modal_close">X</span>待接入
            </div>
            <div class="modal_body">
                <ul class="holdList" id="holdListCont">
                </ul>
                <div class="pages" id="holdListPages"></div>
            </div>
        </div>
    </div>
</div>

<div class="modal" data-target="holdListWeiXInBtn">
    <div class="modal_backdrop"></div>
    <div class="modal_dialog">
        <div class="modal_content">
            <div class="modal_header">
                <span class="modal_close">X</span>用户查询
            </div>
            <div class="modal_body">
                <div class="wx_user_search">
                    <label for="wx_uname">用户姓名： </label>
                    <input type="text" id="wx_uname">
                    <label for="wx_phone">电话： </label>
                    <input type="text" id="wx_phone">
                    <label for="wx_nickName">昵称： </label>
                    <input type="text" id="wx_nickName">
                    <button id="wx_search">搜索</button>
                </div>
                <table id="wx_table">
                    <tr>
                        <th>姓名</th>
                        <th>电话</th>
                        <th>微信账号</th>
                        <th>当前客服账号</th>
                        <th>当前客服名称</th>
                        <th>接入</th>
                    </tr>
                </table>
            </div>
        </div>
    </div>
</div>


<audio id="msgTipAudio" src="<%=request.getContextPath()%>/resouces/audio/tip.wav"></audio>
<div id="bg"></div>
<input type="hidden" value="<%=request.getContextPath()%>" id="baseUrl"/>
<!-- sidebar -->
<jsp:include page="/tpl/common/sidebar.jsp"/>
<div class="wrapper chat">
    <!-- chat-list -->
    <jsp:include page="left/chat-list.jsp"/>
    <!-- chat-window -->
    <jsp:include page="window/chat-window.jsp"/>
    <!-- chat-detail -->
    <jsp:include page="right/chat-detail.jsp"/>
</div>
</body>

</html>
