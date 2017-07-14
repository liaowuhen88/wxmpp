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
     * emoji表情转换
     * <ul>
     * <li>可能表情格式: <code><span class="emoji emoji1f44f"></span></code>则取出1f44f转换表情后替换span标签</li>
     * <li>可能表情内容: <code>\u003cspan class</code>中的unicode转换</li>
     * </ul>
     */
    public static String tranformEemojiContent(String span) {
        span = StringEscapeUtils.unescapeJava(span); //转换unicode码

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

}
