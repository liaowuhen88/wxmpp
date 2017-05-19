<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<script id="onlinefriendListTpl" type="text/html">
    <%--<li ><span  style="display: none" id="{{from}}_tip"></span></li>--%>
    <li class="{{onlineStatus}} have-message" id="{{from}}" openId="{{openId}}" data-id="{{name}}">
        <div class="avatar"><img src="{{icon}}" alt=""></div>
        <div class="name">{{nickname}}[{{loginUsername}}]</div>
        <div class="time">{{time}}</div>
        <div class="last-message"></div>
        <!--   <span class="new-message" id="new-message"></span>
        -->
        <span class="" id="m{{from}}"></span>
    </li>
</script>