package com.baodanyun;

import org.apache.commons.codec.binary.Base64;

/**
 * Created by liaowuhen on 2017/11/1.
 */
public class Base64Utils {
    public static String base64(String text) {
        byte[] b = text.getBytes();
        Base64 base64 = new Base64();
        b = base64.encode(b);
        String s = new String(b);
        return s;
    }
}
