<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%
    String base = request.getContextPath();
%>
<!DOCTYPE html>
<html>
<head>
    <title>豆包客服</title>
    <script>
        window.base = "<%=base%>";
        window.visitor = ${visitor};
        window.currentId = window.visitor.id;

        window.destJid = "${customerJid}";
    </script>
    <link rel="stylesheet" href="<%=base%>/resouces/styles/vistors.css?v=1">
    <link rel="stylesheet" type="text/css"
          href="<%=request.getContextPath()%>/resouces/js/webuploader/css/webuploader.css"/>
    <jsp:include page="/tpl/common/commonjs.jsp"/>
    <jsp:include page="/tpl/visitorMsgList.jsp"/>
    <script type="text/javascript" src="<%=request.getContextPath()%>/resouces/js/webuploader/webuploader.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/resouces/js/webuploader/uploadimg.js"></script>
    <%--<script type="text/javascript" src="<%=request.getContextPath()%>/resouces/js/plugins/dropload.min.js"></script>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/resouces/js/plugins/dropload.css">--%>
    <script type="text/javascript" src="<%=request.getContextPath()%>/resouces/ajax/common.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/resouces/js/ajaxfileupload.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/resouces/js/visitor-chat.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/resouces/scripts/visitors.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/resouces/scripts/visitor.js"></script>

</head>
<body>
<div class="leave-message" style="display: none" id="leaveMessage">
    <div class="info">请在此留下您的联系方式和问题，客服上线后会第一时间回复您</div>
    <div class="form">
        <label for="username">
            <div class="tag">联系姓名</div>
            <input type="text" name="name" value="" id="username">
        </label>
        <label for="phone">
            <div class="tag">联系电话</div>
            <input type="number" name="phone" value="" id="phone">
        </label>
        <label for="problem">
            <div class="tag">咨询问题</div>
            <textarea name="problem" id="problem"></textarea>
        </label>
        <button type="button" id="leaveMessageBtn">留言</button>
    </div>
</div>
<div class="alert" id="alert"></div>
<div class="visitors">
    <div class="base-msgbox" id="msgContainer">
    </div>
    <div class="bottombar">
        <ul class="base-bottombar" id="bottombar">
            <li class="voice-btn"><b>语音按钮</b></li>
            <li class="emoji-btn"><b>表情符号</b></li>
            <li class="other-btn" id="imgUploader"></li>
            <li class="text-btn">
                <label for="message">
                    <textarea id="message"></textarea>
                </label>
            </li>
            <li class="send-btn" id="sendBtn">发送</li>
        </ul>
    </div>
</div>
</body>
<script>
    $(function () {
        var customerIsOnline = ${customerIsOnline};
        if (!customerIsOnline) {
            xchat.leaveMessageShow();
        }else {
            xchat.connect();
        }
        var visitor = new Visitor();
        visitor.init();

    });
</script>
</html>
