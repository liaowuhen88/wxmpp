!function (w) {
    'use strict';
    var DOUBAO_CHAT = w.top.DOUBAO_CHAT ? w.top.DOUBAO_CHAT.DESKTOP : {};
    chat({
        DOUBAO_CHAT: DOUBAO_CHAT,
        ID: DOUBAO_CHAT.NAME || 'DOUBAO_CHAT_DESKTOP'
    });
}(window);
