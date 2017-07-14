<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%--alert--%>
<script id="alert" type="text/html">
    <div class="alert" id="look_history">查看漫游历史记录</div>
</script>
<%--访客信息--%>
<script id="mleft" type="text/html">
    <div class="timeline timeline-from">
        <div class="avatar"><img src="{{icon}}" alt="{{username}}"></div>
        <span class="time">{{time}}</span>
        <span class="time">{{direction}}</span>
        <span class="message">{{&content}}</span>
    </div>
</script>

<script id="imgLeft" type="text/html">
    <div class="timeline timeline-from" id="{{id}}">
        <div class="avatar"><img src="{{icon}}" alt="{{username}}"></div>
        <span class="time">{{direction}}</span>
        <span class="time">{{time}}</span>

        <%--<div class="message-wrapper emoji" style="width:265px;height:201px;"><img src="{{content}}" width="265">
            </div>--%>
        <span class="message"><a href="{{content}}" rel="lightbox" ><img src="{{content}}" alt=""></a></span>
    </div>
</script>

<script id="audioLeft" type="text/html">
    <div class="timeline timeline-from" id="{{id}}">
        <div class="avatar"><img src="{{icon}}" alt="{{username}}"></div>
        <span class="time">{{direction}}</span>
        <span class="time">{{time}}</span>

        <%--<div class="message-wrapper emoji" style="width:265px;height:201px;"><img src="{{content}}" width="265">
            </div>--%>
        <span class="message"><audio src="{{content}}" controls="controls"></audio></span>
    </div>
</script>


<%--客服信息--%>
<script id="mright" type="text/html">
    <div class="timeline timeline-go">
        <div class="avatar"><img src="{{icon}}" alt="{{username}}"></div>
        <span class="time">{{direction}}</span>
        <span class="time">{{time}}</span>

        <span class="message">{{&content}}</span>
    </div>
</script>

<script id="imgRight" type="text/html">
    <div class="timeline timeline-go" id="{{id}}">
        <div class="avatar"><img src="{{icon}}" alt="{{username}}"></div>
        <span class="time">{{direction}}</span>
        <span class="time">{{time}}</span>

        <%--<div class="message-wrapper emoji" style="width:265px;height:201px;"><img src="{{content}}" width="265">--%>
        <span class="message"><a href="{{content}}" rel="lightbox" ><img src="{{content}}" alt=""></a></span>
    </div>
    </div>
</script>