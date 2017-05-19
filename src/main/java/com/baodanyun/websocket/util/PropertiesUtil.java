package com.baodanyun.websocket.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

/**
 * Created by Administrator on 2015/9/7.
 */
public class PropertiesUtil {

    public static Map<String,String> get(ClassLoader loader,String path){
        Map<String,String> map = new HashMap<String,String>();

        InputStream inputStream = loader.getResourceAsStream(path);
        Properties p = new Properties();
        try{
            p.load(inputStream);

            Iterator<Map.Entry<Object, Object>> iterator = p.entrySet().iterator();
            while(iterator.hasNext()){
                Map.Entry<Object, Object> enty = iterator.next();
                map.put((String)enty.getKey(),(String)enty.getValue());
            }
        } catch (IOException e1){
            e1.printStackTrace();
        }

        return map;
    }

}
