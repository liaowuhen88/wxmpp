package com.baodanyun.websocket.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 微信emoji表情字符串转换
 *
 * @author hubo
 * @since 2017-07-03 16:47
 **/
public class EmojiUtil {

    /**
     * emoji表情转换(hex -> utf-16)
     * 如内容: 欢迎新会员<span class="emoji emoji1f44f"></span>
     * 转换后的内容： 欢迎新会员'表情'
     */
    public static String tranformEemojiContent(String span) {
        Pattern pattern = Pattern.compile("<span.*?>.*?</span>");

        Pattern p = Pattern.compile(".*?<span.*?class=.*?emoji emoji(.*?)\"></span>.*?");
        Matcher matcher = p.matcher(span);
        while (matcher.find()) {
            String base = matcher.group(0);
            String group = matcher.group(1);
            String emoji = String.valueOf(Character.toChars(Integer.parseInt(group, 16)));//表情字串

            Matcher m = pattern.matcher(base);
            if (m.find()) {
                span = span.replaceFirst(m.group(0), emoji);
            }
        }

        return span.trim();
    }

    /*public static void main(String[] args) {
        String content = "";//"欢<span class=\"emoji emoji1f75f\"></span>迎新<span class=\"emoji emoji1f44f\"></span>会员<span class=\"emoji emoji1f45f\"></span>天天币<span class=\"emoji emoji1f64f\"></span>";
        String result = tranformEemojiContent(content);

        System.out.println(result);
    }*/
}
