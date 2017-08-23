<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%
    String base = request.getContextPath();
%>
<!DOCTYPE html>
<html>
<style>
    .mark {
        position: fixed;
        top: 0;
        right: 0;
        bottom: 0;
        left: 0;
        display: none;
        background: rgba(0, 0, 0, .8);
    }

    .mark .mark-content {
        font-size: .64rem;
        width: 13.86667rem;
        margin: 4.26667rem auto;
        padding: .64rem .53333rem;
        letter-spacing: .04267rem;
        color: #666;
        background: #fff;
    }

</style>
<head>
    <title>豆包客服</title>
    <script src="<%=request.getContextPath()%>/resouces/js/jquery.js"></script>

    <script>
        window.base = "<%=base%>";
    </script>

</head>
<body>
<div class="mark">
    <div class="mark-content">
        正在转接人工客服...
    </div>
</div>
</body>
<script>

    $(function () {
        $('.mark').show();
        setTimeout(function () {
            WeixinJSBridge.invoke('closeWindow', {}, function (res) {
            });
        }, 2000);
    });


</script>
</html>
