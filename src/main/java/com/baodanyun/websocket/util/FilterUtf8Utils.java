package com.baodanyun.websocket.util;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

/**
 * Created by liaowuhen on 2017/8/29.
 */
public class FilterUtf8Utils {

    public static void main(String[] args) throws UnsupportedEncodingException {
        String mm = "我觉得电话可能说不清楚发票抬头\uD83D\uDE02就联系了客服";
        System.out.println(mm);
        System.out.println(filterOffUtf8Mb4(mm));
        System.out.println(0x20001);

        String a = "名";
        System.out.println("ISO-8859-1:" + a.getBytes("ISO-8859-1").length);
        System.out.println("UTF-8编码长度:" + a.getBytes("UTF-8").length);
        System.out.println("GBK编码长度:" + a.getBytes("GBK").length);
        System.out.println("GB2312编码长度:" + a.getBytes("GB2312").length);
        System.out.println("==========================================");

        String c = "a";
        System.out.println("ISO-8859-1:" + c.getBytes("ISO-8859-1").length);
        System.out.println("UTF-8编码长度:" + c.getBytes("UTF-8").length);
        System.out.println("GBK编码长度:" + c.getBytes("GBK").length);
        System.out.println("GB2312编码长度:" + c.getBytes("GB2312").length);
        System.out.println("==========================================");

        char[] arr = Character.toChars(0x20001);
        a = new String(arr);
        System.out.println("a:" + a);
        System.out.println("UTF-8编码长度:" + a.getBytes("UTF-8").length);
        System.out.println("GBK编码长度:" + a.getBytes("GBK").length);
        System.out.println("GB2312编码长度:" + a.getBytes("GB2312").length);
        System.out.println("==========================================");
    }

    /**
     * 过滤掉超过3个字节的UTF8字符
     *
     * @param text
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String filterOffUtf8Mb4(String text) throws UnsupportedEncodingException {
        byte[] bytes = text.getBytes("utf-8");
        ByteBuffer buffer = ByteBuffer.allocate(bytes.length);
        int i = 0;
        while (i < bytes.length) {
            short b = bytes[i];
            if (b > 0) {
                buffer.put(bytes[i++]);
                continue;
            }

            b += 256; // 去掉符号位

            if (((b >> 5) ^ 0x6) == 0) {
                buffer.put(bytes, i, 2);
                i += 2;
            } else if (((b >> 4) ^ 0xE) == 0) {
                buffer.put(bytes, i, 3);
                i += 3;
            } else if (((b >> 3) ^ 0x1E) == 0) {
                i += 4;
            } else if (((b >> 2) ^ 0x3E) == 0) {
                i += 5;
            } else if (((b >> 1) ^ 0x7E) == 0) {
                i += 6;
            } else {
                buffer.put(bytes[i++]);
            }
        }
        buffer.flip();
        return new String(buffer.array(), "utf-8");
    }
}
