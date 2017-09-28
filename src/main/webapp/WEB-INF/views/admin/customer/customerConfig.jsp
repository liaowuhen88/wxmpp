<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<jsp:include page="/tpl/chatContainerTpl.jsp"/>
<!DOCTYPE html>
<html lang="zh-CN">
<%
    String path = request.getContextPath();
%>

<head>
    <meta charset="UTF-8"/>
    <title>客户配置</title>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/resouces/styles/admin.css">
    <jsp:include page="/tpl/common/commonjs.jsp"/>
    <link rel="stylesheet" type="text/css"
          href="<%=request.getContextPath()%>/resouces/css/datetimepicker/jquery.datetimepicker.css"/>
    <script src="<%=request.getContextPath()%>/resouces/js/common.js"></script>
    <script src="<%=request.getContextPath()%>/resouces/js/plugins/jquery.form.js"></script>
    <script src="<%=request.getContextPath()%>/resouces/js/plugins/jquery-migrate-1.1.1.js"></script>
    <script src="<%=request.getContextPath()%>/resouces/js/util.js"></script>
    <script src="<%=request.getContextPath()%>/resouces/js/plugins/mustache.js"></script>
    <script src="<%=request.getContextPath()%>/resouces/js/datetimepicker/jquery.datetimepicker.full.js"></script>
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
    <style>
        #mask {
            width: 100%;
            height: 100%;
            position: fixed;
            z-index: 999999;
            display: none;
            background: url("<%=request.getContextPath()%>/resouces/images/loading.gif") no-repeat center;
            background-color: rgba(255, 255, 255, .6);
        }
    </style>
</head>

<body>
<!--蒙层-->
<div id="mask"></div>

<form action="<%=request.getContextPath()%>/api/uploadExcel" method="post" enctype="multipart/form-data"
      onsubmit="return check();">
    <div style="margin: 30px;">
        <input id="excel_file" type="file" name="file" size="80"/>
        <input id="excel_button" type="submit" value="导入Excel"/></div>
</form>


</html>
