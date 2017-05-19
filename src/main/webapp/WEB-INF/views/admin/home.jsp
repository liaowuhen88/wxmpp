<%@ page import="com.baodanyun.websocket.bean.AbstractUserAdapter" %>
<%@ page import="com.baodanyun.websocket.core.common.Common" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="zh-CN">
<%
    String path = request.getContextPath();
    AbstractUserAdapter au = (AbstractUserAdapter) request.getSession().getAttribute(Common.USER_KEY);
%>
<head>
    <meta charset="UTF-8"/>
    <title>豆包客服</title>
    <style>
        .welcome {
            width: 100%;
            height: 100%;
            display: flex;
            justify-content: center;
            align-items: center;
            background-color: #fff;
        }

        .slogan {
            font-size: 30px;
            color: #666;
        }
    </style>

    <jsp:include page="/tpl/common/commonjs.jsp"/>
    <jsp:include page="common/common.jsp"/>
    <script>
        $(function () {
            window.currentId = '<%=au.getLogin().getId()%>';
            window.base = '<%=path%>';
            //初始化
            init();

        });
    </script>
</head>

<body>
<jsp:include page="../../../tpl/modalTpl.jsp"/>
<input type="hidden" value="<%=path%>" id="baseUrl"/>
<!-- sidebar -->
<jsp:include page="/tpl/common/sidebar.jsp"/>
<div class="wrapper chat">
    <div class="welcome">
        <div class="slogan">欢迎使用豆包网客服系统。</div>
    </div>
</div>
</body>

</html>
