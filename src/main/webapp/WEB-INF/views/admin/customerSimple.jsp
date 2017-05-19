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
        window.user = ${user};
        //window.vCard = ${vCard};
        window.currentId = user.id;
        window.destJid = "${to}";
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
    <script type="text/javascript" src="<%=request.getContextPath()%>/resouces/js/customerSimple-chat.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/resouces/scripts/visitors.js"></script>

</head>
<body>
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

        xchat.connect();
        xchat.initSuccessQueueStatusHandelEvent();
    });
</script>
</html>
