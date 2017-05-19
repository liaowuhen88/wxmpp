<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%--模态框--%>
<div class="modal" data-target="historyBtn">
    <div class="modal_backdrop"></div>
    <div class="modal_dialog">
        <div class="modal_content">
            <div class="modal_header">
                <span class="modal_close">X</span>历史记录
            </div>
            <div class="modal_body">
                <div class="history" id="history">
                    <%--<div class="search">
                        <label for="searchHistoryInput"><input type="text" name="searchHistory" id="searchHistoryInput"
                                                               placeholder="请输入访客昵称"></label>
                        <button id="searchBtn">查找</button>
                    </div>--%>
                    <table id="table"></table>
                    <div class="pages" id="pagesCont"></div>
                </div>
            </div>
            <div class="modal_footer">
                <button class="btn cancel-btn" data-close="modal">关闭</button>
            </div>
        </div>
    </div>
</div>
<div class="showHistory" id="showHistory" data-id="">
    <div class="chat-window">
        <div class="chat-screen">
            <div class="chat-title"><span class="close">关闭</span>
                您和 <span id="historyFromUsername"></span> 的聊天记录
            </div>
            <div class="chat-show" id="showHistoryChatShow">
                <div class="chat-timeline" id="showHistoryChatTimeline">
                    <div id="showHistoryContainer"></div>
                </div>
            </div>
            <%--<div class="chat-input">
                <ul class="tools">
                    <li data-toggle="tooltip" data-placement="top" title="" data-original-title="图像"><i class="tools-icon icon-image" id="showHistoryImgUploader"></i></li>
                </ul>
                <textarea placeholder="请输入" id="showHistoryEnterChat"></textarea>
                <div class="send-btn" id="showHistorySendBtn">
                    <button>发送</button>
                </div>
            </div>--%>
        </div>
    </div>
</div>