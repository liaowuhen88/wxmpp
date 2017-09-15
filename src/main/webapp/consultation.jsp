<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%
    String base = request.getContextPath();
%>
<!DOCTYPE html>
<html>

<head>
    <title>豆包客服</title>
    <meta name="viewport" content="width=device-width, initial-scale=1,user-select=no">
    <script src="<%=request.getContextPath()%>/resouces/js/jquery.js"></script>
    <script>
        window.base = "<%=base%>";

    </script>
    <style>
        html,
        body,
        div {
    margin: 0;
    padding: 0;
        }

        .qrcode {
    text-align: center;
    padding: 60px 0 0 0;
    font-size: 17px;
    line-height: 1.5;
    display: none;
        }

        .qrcode img {
    width: 250px;
        }

        .qrcode p {
    margin: 10px 0;
        }

        .qrcode .pic {
    width: 250px;
    margin: 0 auto;
            background: url(<%=request.getContextPath()%>/resouces/images/qrcode-loading.gif) no-repeat center;
        }

        .mark {
    position: fixed;
    top: 0;
    right: 0;
    bottom: 0;
    left: 0;
    display: none;
            background: rgba(0, 0, 0, .6);
        }

        .mark .mark-content {
            position: absolute;
            top: 40%;
            left: 50%;
            width: 250px;
            font-size: 14px;
            margin: 0 0 0 -125px;
            padding: 10px 5px;
    color: #666;
    background: #fff;
            text-align: center;
            border-radius: 5px;
        }

    </style>
</head>

<body>
<div class="mark">
    <div class="mark-content">
        正在转接人工客服...
        <br>请切换到豆包管家与客服聊天
    </div>
</div>
<div class="qrcode">
    <div class="pic" id="register">
    </div>
    <p>尊敬的用户您好：
        <br>请您扫描上图二维码
        <br>关注豆包管家咨询客服</p>
</div>
</body>
<script>
    $(function () {
        var follow = '${follow}';
        //var follow = 'false';
        // console.log(follow);
        if (follow == true || follow == 'true') {
            $('.mark').show();
            setTimeout(function () {
                WeixinJSBridge.invoke('closeWindow', {}, function (res) {
                });
            }, 2000);
        } else {
            $('.qrcode').show();
            var url = '${url}';
            //url = 'http://vipkefu.oss-cn-shanghai.aliyuncs.com/vvZhuShou/4d26f65a02a64bbba.jpg';
            //var url="https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=gQE58DwAAAAAAAAAAS5odHRwOi8vd2VpeGluLnFxLmNvbS9xLzAydnpvR2dxMF9iR08xVmNvNGhwMVUAAgTMXbtZAwSAOgkA";

            document.getElementById("register").innerHTML = ['<img src="', url, '"/>'].join('');

        }
    });

</script>

</html>
