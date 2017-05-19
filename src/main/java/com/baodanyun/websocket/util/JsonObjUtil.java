package com.baodanyun.websocket.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;

import org.apache.http.protocol.HTTP;
import org.apache.log4j.Logger;

import com.google.gson.Gson;

/**
 * 
 * @author 峥桂
 * desc 编码+加密==》解密+解码
 */
public class JsonObjUtil {

	private static final Logger logger = Logger.getLogger(JsonObjUtil.class);

	private static final String DEFAULT_ENCODE = HTTP.UTF_8;

	/**
	 * 对象转换json串(加密)
	 * @param obj 
	 * @param flag 是否需url编码后加密
	 * @return
	 */
	public static String obj2JosnAndEncrypt(Object obj, boolean flag) {
		Gson gson = new Gson();
		try {
			String data =gson.toJson(obj);
			return encryptString(data,flag);
		} catch (Exception e) {
			logger.info(e.getMessage(), e);
			return "";
		}

	}
	/**
	 * 加密字符串
	 * @param
	 * @param flag 是否需url编码后加密
	 * @return
	 */
	public static String encryptString(String data, boolean flag) {
		try {
			String ecd_data=data;
			if (flag) {
				ecd_data= URLEncoder.encode(data, DEFAULT_ENCODE);
			}
			return Hex.encodeHexStr(bytes2String(SecretUtils.encryptMode(ecd_data.getBytes())).getBytes());
//			return encryptString(ecd_data);
		} catch (Exception e) {
			logger.info(e.getMessage(), e);
			return "";
		}

	}


	/**
	 * 对象转换json串(不加密)
	 * @param obj 
	 * @param flag 是否需url编码
	 * @return
	 */
	public static String obj2Josn(Object obj, boolean flag) {
		Gson gson = new Gson();
		try {
			String data = gson.toJson(obj);
			if (flag) {
				return URLEncoder.encode(data, DEFAULT_ENCODE);
			}
			return data;
		} catch (Exception e) {
			logger.info(e.getMessage(), e);
			return "";
		}

	}
	/**
	 * 加密数据
	 * 
	 * @param data
	 * @return
	 * @throws Exception
	 */
	private static String encryptString(String data) throws Exception {
		byte[] k = DESedeCoder.initSecretKey();
		return bytes2String(DESedeCoder.encrypt(data.getBytes(), k));
	}



	
	/**
	 * 解密json串
	 * @param json
	 * @param flag 是否需要url解密后解码
	 * @return
	 */
	public static String decryptJsonToString(String json,boolean flag) {
		try {
//			byte[] b=decryptJson(json);
			//解码进制码
			String hex=new String(Hex.decodeHex(json.toCharArray()));
			//将以逗号分割的串转为byte数组
			byte[] b = string2Bytes(hex);
			String data=new String(SecretUtils.decryptMode(b));
			if(flag){
				data = URLDecoder.decode(data,DEFAULT_ENCODE);
			}
			return data;
		} catch (Exception e) {
			logger.info(e.getMessage(), e);
			return null;
		}
	}
	/**
	 * 解密json串
	 * @param json
	 * @return
	 */
	private static byte[] decryptJson(String json) throws Exception {
		return decryptString2Bytes(json);
	}

	/**
	 * 解密数据
	 * 
	 * @param data
	 * @return
	 * @throws Exception
	 */
	private static String decryptString(String data) throws Exception {
		byte[] b = string2Bytes(data);
		byte[] k = DESedeCoder.initSecretKey();
		return bytes2String(DESedeCoder.decrypt(b, k));

	}
	
	/**
	 * 解密数据
	 * 
	 * @param data
	 * @return
	 * @throws Exception
	 */
	private static byte[] decryptString2Bytes(String data) throws Exception {
		return string2Bytes(decryptString(data));

	}

	/**
	 * 将数组转换为以逗号分隔的字符串
	 * @param data
	 * @return
	 */
	private static String bytes2String(byte[] data) {
		if (null == data) {
			return null;
		}
		StringBuilder sb = new StringBuilder();
		for (byte b : data) {
			sb.append(b).append(",");
		}
		sb.deleteCharAt(sb.length() - 1);
		return sb.toString();
	}

	/**
	 * 将以逗号分割的byte还原
	 * @param data
	 * @return
	 */
	private static byte[] string2Bytes(String data) {
		if (null == data) {
			return null;
		}
		String[] slitstr = data.split(",");
		byte[] b = new byte[slitstr.length];
		int i = 0;
		for (String s : slitstr) {
			b[i] = Byte.parseByte(s);
			i++;
		}
		return b;
	}

	public static String getBody(HttpServletRequest request) {
        StringBuffer buffer = new StringBuffer();

        BufferedReader reader = null;
        try {
            reader = request.getReader();
            String inputLine;
            while ((inputLine = reader.readLine()) != null) {
                buffer.append(inputLine);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(reader != null){
                try {
                    reader.close();
                } catch (IOException e) {
                }
            }
        }
        return buffer.toString();
    }

	public static void main(String[] args) throws UnsupportedEncodingException {
//
//		WeiXinUser user = new WeiXinUser();
//		user.setWeixin_openid("oAH_qsufC85Zh9rYKe3OlLAF1h-4");
//		user.setNick("肖鹏");
//		user.setProvince("背景声");
//		user.setCity("北京市");
//		System.out.println("加密前的数据：" + JsonObjUtil.obj2Josn(user, false));
//		String json = JsonObjUtil.obj2JosnAndEncrypt(user,true);
//		System.out.println("加密后的数据：" + json);
////		byte[] rejson = JsonObjUtil.decryptJson(json,true);
//		System.out.println("解密后的数据：" + JsonObjUtil.decryptJsonToString(json, true));
//
		String SS=JsonObjUtil.encryptString("你好哈，dddd",true);
		System.out.println("加密后的数据：" + SS);
//		SS="67%2C-102%2C-84%2C73%2C122%2C116%2C110%2C-55%2C24%2C-52%2C-68%2C56%2C-33%2C-72%2C120%2C-29";
		System.out.println("解密后的数据：" + JsonObjUtil.decryptJsonToString(SS, true));
	}

}
