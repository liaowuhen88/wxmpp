package com.baodanyun.websocket.bean.dubbo;

/**
 * Created by liaowuhen on 2017/9/14.
 */
public class QRCodeContent {
    //String content="{\"channel\":\"doubao\",\"content\":{\"name\":\"临时二维码\",\"pic\":\"http://m.17doubao.com/mall/themes/wap/images/xiaodoubao.jpg\",\"type\":5,\"url\":\"http://m.17doubao.com/mall/index.html\"}}";

    private String channel;
    private Content content = new Content();

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public Content getContent() {
        return content;
    }

    public void setContent(Content content) {
        this.content = content;
    }

    public class Content {
        private String name;
        private String pic;
        private String url;
        private Byte type;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPic() {
            return pic;
        }

        public void setPic(String pic) {
            this.pic = pic;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public Byte getType() {
            return type;
        }

        public void setType(Byte type) {
            this.type = type;
        }
    }
}
