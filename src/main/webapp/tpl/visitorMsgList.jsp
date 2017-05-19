<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<script id="mleft" type="text/html">
    <div class="component-message" id="{{id}}">
        <div class="avatar"><img src="{{icon}}"/></div>
        <div class="content">
            <div class="name">{{fromName}}</div>
            <div class="message-wrapper text">{{content}}</div>
        </div>
    </div>
</script>

<script id="mright" type="text/html">
    <div class="component-message me-message" id="{{id}}">
        <div class="avatar"><img src="{{icon}}"/></div>
        <div class="content">
            <div class="name">{{fromName}}</div>
            <div class="message-wrapper text">{{content}}</div>
        </div>
    </div>
</script>

<script id="imgLeft" type="text/html">
    <div class="component-message" id="{{id}}">
        <div class="avatar"><img src="{{icon}}"></div>
        <div class="content">
            <div class="name">{{fromName}}</div>
            <div class="message-wrapper emoji" style="width:100%;max-width:265px;"><a href="{{content}}" rel="lightbox" ><img src="{{content}}"></a>
            </div>
        </div>
    </div>
</script>

<script id="imgRight" type="text/html">
    <div class="component-message me-message" id="{{id}}">
        <div class="avatar"><img src="{{icon}}"></div>
        <div class="content">
            <div class="name">{{fromName}}</div>
            <div class="message-wrapper emoji" style="width:100%;max-width:265px;"><a href="{{content}}" rel="lightbox" ><img src="{{content}}"></a>
            </div>
        </div>
    </div>
</script>