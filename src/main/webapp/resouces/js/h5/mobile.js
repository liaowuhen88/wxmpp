!function (w) {
    'use strict';
    var DOUBAO_CHAT = w.top.DOUBAO_CHAT ? w.top.DOUBAO_CHAT.MOBILE : {};
    chat({
        DOUBAO_CHAT: DOUBAO_CHAT,
        ID: DOUBAO_CHAT.NAME || 'DOUBAO_CHAT_MOBILE'
    });
}(window);
