<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%
    String base = request.getContextPath();
%>
<!DOCTYPE html>
<html>
<head>
    <title>豆包客服</title>
    <script src="<%=request.getContextPath()%>/resouces/js/plugins/jquery.form.js"></script>
    <script src="<%=request.getContextPath()%>/resouces/js/plugins/jquery-migrate-1.1.1.js"></script>
    <script>
        window.base = "<%=base%>";
    </script>

</head>
<body>
<div align="center" id="tipDiv"></div>
</body>
<script>

    $(function () {
        talk('${user.username}', '${user.to}');
    });

    function talk(username, to) {
        if (!username || !to) {
            alert('客户名或者用户为空');
            return false;
        }
        var url = window.base + '/api/customerAndJoin?username=' + username + '&to=' + to;
        $.ajax({
            url: url,
            type: 'POST',
            success: function (data) {
                console.log(data);
                if (data.success) {
                    $('#tipDiv').html("用户已接入，3秒后关闭");
                    setTimeout(function () {
                        window.close();
                    }, 3000);
                } else {
                    if (data.code == -1) {//客服未登录
                        url = window.base + '/api/doLoginForUecUser?username=' + username + '&to=' + to;
                        window.location.href = url;
                    } else {//异常提示
                        alert(data.msg);
                    }
                }

            }
        });
    }
</script>
</html>
