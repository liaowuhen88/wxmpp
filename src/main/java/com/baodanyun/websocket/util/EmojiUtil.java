package com.baodanyun.websocket.util;

import org.apache.commons.lang3.StringEscapeUtils;

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
        span = StringEscapeUtils.unescapeJava(span); //unicode码如\u003cspan

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

    public static void main(String[] args) throws  Exception {
        String content = "<span class=\"emoji emoji1f604\"></span>天天\\u003cspan class\\u003d\\\"emoji emoji1f60a\\\"\\u003e\\u003c/span\\u003e\\u003cspan class\\u003d\\\"emoji emoji1f63a\\\"\\u003e\\u003c/span\\u003e\\u003cspan class\\u003d\\\"emoji emoji1f604\\\"\\u003e\\u003c/span\\u003e\\u003cspan class\\u003d\\\"emoji emoji1f639\\\"\\u003e\\u003c/span\\u003e\\u003cspan class\\u003d\\\"emoji emoji1f62d\\\"\\u003e\\u003c/span\\u003e\"";//"欢<span class=\"emoji emoji1f75f\"></span>迎新<span class=\"emoji emoji1f44f\"></span>会员<span class=\"emoji emoji1f45f\"></span>天天币<span class=\"emoji emoji1f64f\"></span>";

       content =  StringEscapeUtils.unescapeJava(content);

        System.out.println(tranformEemojiContent(content));
    }
}
