<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8"/>
    <title>登录</title>
    <meta name="Access-Control-Allow-Origin" content="*">

    <script>
        window.base = '<%=request.getContextPath()%>';
    </script>
    <!-- 公用头部 -->
    <jsp:include page="tpl/common/commonjs.jsp"/>
    <jsp:include page="./WEB-INF/views/admin/common/common.jsp"/>

</head>
<script>
    $(function () {
        var t = document.getElementsByTagName("body")[0];
        var $signBtn = $('#signInBtn');
        $.ajax({
            url: window.base + "/api/findLoginImage", type: "GET", timeout: 3e3, success: function (e) {
                var o = 'http://cn.bing.com/' + e.images[0].url;
                var $copyright = $('#bgInfo');
                $(t).css({
                    background: "url(" + o + ") center / cover",
                    position: "fixed",
                    top: "0",
                    left: "0",
                    width: "100%",
                    height: "100%",
                    overflow: "hidden"
                });
                $copyright.text(e.images[0].copyright);
                /*var message=$("#message").val();
                 console.log("$(message).val()"+(message != 'null'));
                 if(message != null && message !='' && message !='null'){
                 alert("登录失败，请正确填写用户名和密码或联系管理员");
                 }*/
            }, error: function (t) {
                console.log(t)
            }
        });
        $signBtn.on('click', function () {
            var accessType = $('#accessType').val();
            if (!accessType) {
                alert("请选择是否接入用户");
                return false;
            }
            var $btn = $(this).button('loading');
            myUtils.ajaxSubmit("signInBtnForm", "signInBtn", window.base + "/api/loginApi", {}, function () {
                $btn.button('reset');
                window.location.href = window.base + "/api/customer_chat";
            }, function (json) {
                $btn.button('reset');
                alert(json.msg)
            });
        })
    });
</script>

<body id="bodyAdmin">
<div class="sign sign-in">
    <div class="logo">
        豆包网客服系统
    </div>
    <form id="signInBtnForm">
        <input type="hidden" name="type" value="customer">
        <label>
            <input type="text" name="username" value="111111" id="username" placeholder="账号">
        </label>
        <label>
            <input type="password" name="password" value="111111" id="password" placeholder="密码">
        </label>

        <label>
            <select id="accessType" name="accessType">
                <option value="">请选择是否接入用户</option>
                <option value="1">不接入用户</option>
                <option value="2">接入用户</option>
            </select>
        </label>

        <button type="button" class="sign-btn" id="signInBtn" autocomplete="off" data-loading-text="Loading...">登录
        </button>
    </form>
</div>
<div class="copyright">&copy;豆包网 背景图片来自bing.com</div>
</body>
</html>
