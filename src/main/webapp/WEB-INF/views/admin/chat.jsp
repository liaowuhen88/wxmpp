<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="zh-CN">

<style type="text/css">
    .label-primary {
        background-color: #337ab7;
    }

    .label {
        display: inline;
        padding: .2em .6em .3em;
        font-size: 75%;
        font-weight: 700;
        line-height: 1;
        color: #fff;
        text-align: center;
        white-space: nowrap;
        vertical-align: baseline;
        border-radius: .25em;
    }

</style>
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
        window.colors = {};
        $(function () {
            initColor();

            var wu = ${wu};
            window.user = wu.vcard;
            window.currentId = wu.user.id;

            if (window.currentId && (window.currentId == "zhangqike@126xmpp" || window.currentId == "yutao@126xmpp")) {
                $("#addQuickReplyBtn").hide();
                $("#quick_chat_message").text("大数据智能推荐");
            } else {
                $("#changeQuickReplyBtn").hide();

            }
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

            if (wu.isAdmin == 1) {
                $('#qualityCheck').show();
            }


        });


        function initColor() {
            getRandomColor('remind', window.colors);
            getRandomColor('notice', window.colors);
            getRandomColor('success', window.colors);
            getRandomColor('Dishonor', window.colors);
            getRandomColor('appointment', window.colors);
            getRandomColor('ToVoid', window.colors);
            getRandomColor('Revoke', window.colors);
            getRandomColor('mall', window.colors);
            getRandomColor('settlementOfClaims', window.colors);
            getRandomColor('physicalExamination', window.colors);
            getRandomColor('hello', window.colors);
            getRandomColor('healthy', window.colors);

        }
        function getRandomColor(key, map) {
            var color = map[key];
            if (!color) {
                color = '#' + ('00000' + (Math.random() * 0x1000000 << 0).toString(16)).substr(-6);
                map[key] = color;
            }

            return color;
        }
    </script>
</head>

<body>
<%-- 历史记录 --%>
<jsp:include page="history.jsp"/>
<%-- 个人中心设置 --%>
<jsp:include page="set.jsp"/>
<%-- 离线消息 --%>
<jsp:include page="leaveMessage.jsp"/>

<div style="
    position: fixed;
    left: 0;
    top: 0;
    right: 0;
    bottom: 0;
    background: rgba(3,3,3,0.8);
    z-index: 999;
    text-align: center;
" id="onlineStatus">
    <div style="
    background: #fff;
    font-size: 25px;
    padding: 20px;
    margin: 200px auto 0;
    display: inherit;
    width: 500px;
">
        您已掉线,请刷新
    </div>
</div>

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
