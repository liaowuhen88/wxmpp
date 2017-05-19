<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="zh_CN">
<%
    String path = request.getContextPath();
%>
<head>
    <meta charset="UTF-8">
    <title>访客受检</title>
    <link href="https://www.17doubao.com/resources/images/favicon.ico" mce_href="favicon.ico" rel="bookmark" type="image/x-icon" />
    <link href="https://www.17doubao.com/resources/images/favicon.ico" mce_href="favicon.ico" rel="icon" type="image/x-icon" />
    <link href="https://www.17doubao.com/resources/images/favicon.ico" mce_href="favicon.ico" rel="shortcut icon" type="image/x-icon" />
    <jsp:include page="tpl/common/commonjs.jsp"/>
</head>
<body>
<div id="msg">正在检查</div>
<form id="visitorValidForm">
    <input type="hidden" name="type" value="visitor">
    <input type="hidden" name="username" value="<%=request.getParameter("u")==null?"":request.getParameter("u")%>">
    <input type="hidden" name="to" value="<%=request.getParameter("t")==null?"":request.getParameter("t")%>">
    <input type="hidden" name="ic" value="<%=request.getParameter("i")==null?"":request.getParameter("i")%>">
    <input type="hidden" name="nk" value="<%=request.getParameter("n")==null?"":request.getParameter("n")%>">
</form>
<script>
    $(function () {

        <%
        //session 失效后 会重定向当前页面 再次刷新当前连接 页面重新验证
        if(request.getParameter("f")!=null){
        %>
        window.history.go(-1);
        <%
        }else{
        %>
        myUtils.ajaxSubmitNoBtn("visitorValidForm", "<%=path%>/api/loginApi", {}, function () {
            window.location.href = "<%=path%>/visitor";
        }, function (json) {
            $("#msg").empty().append("检查失败，非法登录");
            if (json.code == 1) {
                alert("参数错误")
            } else if (json.code == 2) {
                alert("用户名或者密码错误")
            } else {
                alert("系统错误")
            }
        });
        <%
        }
        %>

    })
</script>
</body>

</html>
