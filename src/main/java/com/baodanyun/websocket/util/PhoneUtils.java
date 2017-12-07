package com.baodanyun.websocket.util;

import org.apache.commons.lang.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by liaowuhen on 2017/5/25.
 */
public class PhoneUtils {
    public static boolean isMobile(String mobiles) {
        if (StringUtils.isNotEmpty(mobiles)) {
            Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0-9]))\\d{8}$");
            Matcher m = p.matcher(mobiles);
            return m.matches();
        }

        return false;
    }
}
