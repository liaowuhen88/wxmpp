package com.baodanyun.websocket.bean.msg;

/**
 * Created by liaowuhen on 2016/11/23.
 */
public class HistoryMsg extends Msg {
    private static final long serialVersionUID = 113623413329536376L;
    private transient String toCard;
    private transient String fromCard;
    private String toIcon;

    public String getToCard() {
        return toCard;
    }

    public void setToCard(String toCard) {
        this.toCard = toCard;
    }

    public String getFromCard() {
        return fromCard;
    }

    public void setFromCard(String fromCard) {
        this.fromCard = fromCard;
    }

    public String getToIcon() {
        return toIcon;
    }

    public void setToIcon(String toIcon) {
        this.toIcon = toIcon;
    }
}
