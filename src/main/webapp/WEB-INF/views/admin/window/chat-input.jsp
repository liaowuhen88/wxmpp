<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<div class="chat-input" id="chatInput">
    <ul class="tools">
        <%--<li data-toggle="tooltip" data-placement="top" title="表情"><i class="tools-icon icon-emoji"></i></li>--%>
        <li data-toggle="tooltip" data-placement="top" title="图像"><i class="tools-icon icon-image" id="imgUploader"></i></li>
        <%--<li data-toggle="tooltip" data-placement="top" title="文件"><i class="tools-icon icon-file"></i></li>--%>
        <%--<li data-toggle="tooltip" data-placement="top" title="评价"><i class="tools-icon icon-invite" id="modal"></i></li>--%>
    </ul>


    <textarea placeholder="请输入" id="enterChat"></textarea>

    <div class="send-btn" >
        <button id="send-btn">发送</button>
    </div>
</div>
