package com.baodanyun.websocket.util;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * Created by 普光 on 2016/8/10
 * DES 加密  解密方法 密钥只能用8位字符
 */
public class DesUtil {
    private final static String DES = "DES";

    /**
     * 加密
     *
     * @param key
     * @param value
     * @return
     */
    public static String encryptECB(String key, String value) throws InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException, UnsupportedEncodingException, BadPaddingException, IllegalBlockSizeException {
        SecretKey secretKey = new SecretKeySpec(key.getBytes("UTF-8"), DES);
        Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] binaryData = cipher.doFinal(value.getBytes("UTF-8"));
        return Base64.encodeBase64String(binaryData);
    }

    /**
     * 解密
     *
     * @param key
     * @param value
     * @return
     */
    public static String decryptECB(String key, String value) throws InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException, UnsupportedEncodingException, BadPaddingException, IllegalBlockSizeException {
        byte[] binaryValue = Base64.decodeBase64(value);
        SecretKey secretKey = new SecretKeySpec(key.getBytes("UTF-8"), DES);
        Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] data = cipher.doFinal(binaryValue);
        return new String(data, "UTF-8");
    }
}
