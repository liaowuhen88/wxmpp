package com.baodanyun.websocket.util;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import java.security.Key;
import java.security.SecureRandom;

/** 
 * DESede Coder<br/> 
 * secret key length:   112/168 bit, default:   168 bit<br/> 
 * mode:    ECB/CBC/PCBC/CTR/CTS/CFB/CFB8 to CFB128/OFB/OBF8 to OFB128<br/> 
 * padding: Nopadding/PKCS5Padding/ISO10126Padding/ 
 * @author Aub 
 *  
 */  
public class DESedeCoder {  
	private static final String KEY="e59586e59f8ee8b186e58c853130";
      
    /** 
     * 密钥算法 
    */  
    private static final String KEY_ALGORITHM = "DESede";  
      
//  private static final String DEFAULT_CIPHER_ALGORITHM = "DESede/ECB/PKCS5Padding";  
    private static final String DEFAULT_CIPHER_ALGORITHM = "DESede/ECB/ISO10126Padding";  
      
    /** 
     * 初始化密钥 
     *  
     * @return byte[] 密钥  
     * @throws Exception 
     */  
    public static byte[] initSecretKey() throws Exception{  
        //返回生成指定算法的秘密密钥的 KeyGenerator 对象  
        KeyGenerator kg = KeyGenerator.getInstance(KEY_ALGORITHM);  
        
//        如果设定随机产生器就用如相代码初始化 
        SecureRandom secrand=new SecureRandom(); 
        secrand.setSeed(KEY.getBytes()); //初始化随机产生器 
        kg.init(168,secrand);     //初始化密钥生成器 

//        kg.init(168);  
        //生成一个密钥  
        SecretKey  secretKey = kg.generateKey();  
        return secretKey.getEncoded();  
    }  
      
    /** 
     * 转换密钥 
     *  
     * @param key   二进制密钥 
     * @return Key  密钥 
     * @throws Exception 
     */  
    private static Key toKey(byte[] key) throws Exception{  
        //实例化DES密钥规则  
        DESedeKeySpec dks = new DESedeKeySpec(key);  
        //实例化密钥工厂  
        SecretKeyFactory skf = SecretKeyFactory.getInstance(KEY_ALGORITHM);  
        //生成密钥  
        SecretKey  secretKey = skf.generateSecret(dks);  
        return secretKey;  
    }  
      
    /** 
     * 加密 
     *  
     * @param data  待加密数据 
     * @param key   密钥 
     * @return byte[]   加密数据 
     * @throws Exception 
     */  
    public static byte[] encrypt(byte[] data,Key key) throws Exception{  
        return encrypt(data, key,DEFAULT_CIPHER_ALGORITHM);  
    }  
      
    /** 
     * 加密 
     *  
     * @param data  待加密数据 
     * @param key   二进制密钥 
     * @return byte[]   加密数据 
     * @throws Exception 
     */  
    public static byte[] encrypt(byte[] data,byte[] key) throws Exception{  
        return encrypt(data, key,DEFAULT_CIPHER_ALGORITHM);  
    }  
      
      
    /** 
     * 加密 
     *  
     * @param data  待加密数据 
     * @param key   二进制密钥 
     * @param cipherAlgorithm   加密算法/工作模式/填充方式 
     * @return byte[]   加密数据 
     * @throws Exception 
     */  
    public static byte[] encrypt(byte[] data,byte[] key,String cipherAlgorithm) throws Exception{  
        //还原密钥  
        Key k = toKey(key);  
        return encrypt(data, k, cipherAlgorithm);  
    }  
      
    /** 
     * 加密 
     *  
     * @param data  待加密数据 
     * @param key   密钥 
     * @param cipherAlgorithm   加密算法/工作模式/填充方式 
     * @return byte[]   加密数据 
     * @throws Exception 
     */  
    public static byte[] encrypt(byte[] data,Key key,String cipherAlgorithm) throws Exception{  
        //实例化  
        Cipher cipher = Cipher.getInstance(cipherAlgorithm);  
        //使用密钥初始化，设置为加密模式  
        cipher.init(Cipher.ENCRYPT_MODE, key);  
        //执行操作  
        return cipher.doFinal(data);  
    }  
      
    /** 
     * 解密 
     *  
     * @param data  待解密数据 
     * @param key   密钥 
     * @param cipherAlgorithm   加密算法/工作模式/填充方式 
     * @return byte[]   解密数据 
     * @throws Exception 
     */  
    public static byte[] decrypt(byte[] data,Key key,String cipherAlgorithm) throws Exception{  
        //实例化  
        Cipher cipher = Cipher.getInstance(cipherAlgorithm);  
        //使用密钥初始化，设置为解密模式  
        cipher.init(Cipher.DECRYPT_MODE, key);  
        //执行操作  
        return cipher.doFinal(data);  
    }
      
    /** 
     * 解密 
     *  
     * @param data  待解密数据 
     * @param key   二进制密钥 
     * @return byte[]   解密数据 
     * @throws Exception 
     */  
    public static byte[] decrypt(byte[] data,byte[] key) throws Exception{  
        return decrypt(data, key,DEFAULT_CIPHER_ALGORITHM);  
    }  
      
    /** 
     * 解密 
     *  
     * @param data  待解密数据 
     * @param key   密钥 
     * @return byte[]   解密数据 
     * @throws Exception 
     */  
    public static byte[] decrypt(byte[] data,Key key) throws Exception{  
        return decrypt(data, key,DEFAULT_CIPHER_ALGORITHM);  
    }  
      
    /** 
     * 解密 
     *  
     * @param data  待解密数据 
     * @param key   二进制密钥 
     * @param cipherAlgorithm   加密算法/工作模式/填充方式 
     * @return byte[]   解密数据 
     * @throws Exception 
     */  
    public static byte[] decrypt(byte[] data,byte[] key,String cipherAlgorithm) throws Exception{  
        //还原密钥  
        Key k = toKey(key);  
        return decrypt(data, k, cipherAlgorithm);  
    }  
  
  
      
    private static String  showByteArray(byte[] data){  
        if(null == data){  
            return null;  
        }  
        StringBuilder sb = new StringBuilder();  
        for(byte b:data){  
            sb.append(b).append(",");  
        }  
        sb.deleteCharAt(sb.length()-1);  
        return sb.toString();  
    } 
    
    private static byte[]  converStringtoBytes(String data){  
        if(null == data){  
            return null;  
        }  
        String[] slitstr=data.split(",");
        byte[] b=new byte[slitstr.length];
        int i=0;
        for(String s:slitstr){
        	b[i]=Byte.parseByte(s);
        	i++;
        }
        return b;
    } 
      
    public static void main(String[] args) throws Exception {  
        byte[] key = initSecretKey();  
//      byte[] key = "123456789012345678901".getBytes(); 
        
        System.out.println("key："+ showByteArray(key));  
        System.out.println(); 
        System.out.println("key："+ Hex.encodeHexStr("doubao".getBytes())); 
          
        Key k = toKey(key);  
          
//      String data ="DESede数据";  
        String data = "{\"messageHead\":{\"sendTime\":\"2016-08-15 22:46:43\",\"action\":\"Q1\",\"sourceId\":\"61131\"},\"callback\":{\"order\":{\"productCode\":\"252\",\"policyUrl\":\"http://mall.taikang.com/webapp/downFilepolicy?policyNo=21162520879547382&source=61131&md5s=76b2c8e5c21fcd87733607484a490cda\",\"endDate\":\"2022-08-16 00:00:00\",\"policyNo\":\"21162520879547382\",\"beginDate\":\"2016-08-16 00:00:00\",\"totalPremium\":\"1000\",\"clientNo\":\"88\"},\"clientInfo\":{\"insuredInfo\":{\"insuredList\":[null],\"isHolder\":\"true\",\"benefitInfo\":{\"isLegal\":\"true\",\"benefitList\":[null]}},\"holder\":{\"holderCardNo\":\"O3A3KUmWxaqGunZ6vYiTBpwUuZnNua3/\",\"holderSex\":\"0\",\"holderBirthday\":\"1979-05-30\",\"holderMobile\":\"aMGsSUeWULofXqVMn/NDPd+ajEhDyoxI1sncEHMW/Hx8zOxuvTM4qg==\",\"holderCardType\":\"01\",\"holderName\":\"\\\\u81e7\\\\u65ed\"}}}}";
        System.out.println("加密前数据: string:"+data);  
        System.out.println("加密前数据: byte[]:"+showByteArray(data.getBytes()));  
        System.out.println();  
        byte[] encryptData = encrypt(data.getBytes(), k);  
        String requestdate=showByteArray(encryptData);
        System.out.println("加密后数据: byte[]:"+showByteArray(encryptData));  
        System.out.println("加密后数据: hexStr:"+Hex.encodeHexStr(encryptData));  
        System.out.println();  
        byte[] b=converStringtoBytes(requestdate);
        System.out.println("加密后数据: byte[]:"+showByteArray(b));
        System.out.println();
        byte[] decryptData = decrypt(b, k);  
        System.out.println("解密后数据: byte[]:"+showByteArray(decryptData));  
        System.out.println("解密后数据: string:"+new String(decryptData));  
          
    }
      
}  