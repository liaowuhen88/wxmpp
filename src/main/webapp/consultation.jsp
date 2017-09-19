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

    .register {
        display: none;
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
    <script src="<%=request.getContextPath()%>/resouces/js/hotcss.js"></script>

    <script>
        window.base = "<%=base%>";
    </script>

</head>
<body>
<div class="mark">
    <div class="mark-content">
        正在转接人工客服...请切换到豆包管家与客服聊天
    </div>
</div>

<div class="register">
    <div class="mark-content">
        <img src="" id="register">

        <div>尊敬的用户您好，请您扫描上图二维码关注豆包管家咨询客服。</div>
    </div>
</div>
</body>
<script>

    $(function () {
        var follow = '${follow}';
        console.log(follow);
        if (follow == true || follow == 'true') {
            $('.mark').show();
            setTimeout(function () {
                WeixinJSBridge.invoke('closeWindow', {}, function (res) {
                });
            }, 2000);
        } else {
            $('.register').show();
            var url = '${url}';
            if (!url) {
                url = 'http://vipkefu.oss-cn-shanghai.aliyuncs.com/vvZhuShou/4d26f65a02a64bbba.jpg';
            }
            document.getElementById("register").setAttribute("src", url);
        }
    });


</script>
</html>
