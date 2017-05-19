<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%--模态框--%>
<div class="modal" data-target="leaveMessageBtn">
    <div class="modal_backdrop"></div>
    <div class="modal_dialog">
        <div class="modal_content">
            <div class="modal_header">
                <span class="modal_close">X</span>访客留言 <span class="message-status status-active" data-status="">全部</span><span class="message-status" data-status="1">未处理</span><span class="message-status" data-status="2">已处理</span>
            </div>
            <div class="modal_body">
                <div class="leave-message">
                    <ul class="message-list" id="leaveMessageCont">
                        <li>
                            <dl class="type">
                                <dt>联系姓名：</dt>
                                <dd>黄晓明</dd>
                                <dt>联系电话：</dt>
                                <dd>19283474291</dd>
                                <dt>咨询问题：</dt>
                                <dd>111</dd>
                                <dt>处理备注：</dt>
                                <dd><textarea id="" cols="30" rows="10"></textarea></dd>
                            </dl>
                            <div class="process-btn">已处理</div>
                        </li>
                        <li>
                            <dl class="type">
                                <dt>联系姓名：</dt>
                                <dd>黄晓明</dd>
                                <dt>联系电话：</dt>
                                <dd>19283474291</dd>
                                <dt>咨询问题：</dt>
                                <dd>111</dd>
                                <dt>处理备注：</dt>
                                <dd>这里是备注信息</dd>
                            </dl>
                        </li>
                    </ul>
                    <div class="pages" id="leaveMessagePages"></div>
                </div>
            </div>
            <div class="modal_footer">
                <button class="btn cancel-btn" data-close="modal">关闭</button>
            </div>
        </div>
    </div>
</div>
