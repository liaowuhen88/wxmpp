<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<style>
    .chat-tpl-list li {
        border: 1px solid #DEDCDC;
    }

    .chat-tab ul {
        padding: 0 0 5px;
    }
</style>
<div class="chat-tpl-search">
    <label>
        <input type="text" name="" value="" placeholder="请输入您要搜索到文字" id="quickReplyInput">
    </label>
</div>

<ul class="chat-tpl-tools">
    <li id="addQuickReplyBtn">新增快捷回复</li>
    <li id="changeQuickReplyBtn">换一批推荐</li>
</ul>


<section class="chat-tpl">
    <ul class="chat-tpl-list" id="message_tpl">
        <li>您好，请问有什么可以帮您？
            <div class="tpl-btns">
                <i class="tpl-btns-icon icon-edit"></i>
                <i class="tpl-btns-icon icon-delete"></i>
            </div>
        </li>
        <li>请问我怎么称呼您呢？
            <div class="tpl-btns">
                <i class="tpl-btns-icon icon-edit"></i>
                <i class="tpl-btns-icon icon-delete"></i>
            </div>
        </li>
        <li>方便告诉我一下你的联系方式么？
            <div class="tpl-btns">
                <i class="tpl-btns-icon icon-edit"></i>
                <i class="tpl-btns-icon icon-delete"></i>
            </div>
        </li>
        <li>好的，我马上处理一下！
            <div class="tpl-btns">
                <i class="tpl-btns-icon icon-edit"></i>
                <i class="tpl-btns-icon icon-delete"></i>
            </div>
        </li>
    </ul>


    <ul class="chat-tpl-list" id="message_tpl_2"></ul>

    <ul>
        <div id="waiting_quick">
            <img src="<%=request.getContextPath()%>/resouces/images/ajax-loader.gif">
        </div>
    </ul>

</section>
