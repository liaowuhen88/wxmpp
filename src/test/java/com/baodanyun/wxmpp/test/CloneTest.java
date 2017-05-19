package com.baodanyun.wxmpp.test;

import com.baodanyun.websocket.util.JSONUtil;
import org.apache.commons.lang.SerializationUtils;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by liaowuhen on 2017/3/9.
 */
public class CloneTest {

    @Test
    public void cloneList(){
        ArrayList<Map<String,String>> li = new ArrayList<>();
        Map<String,String> map = new HashMap<>();
        map.put("root","root");
        map.put("admin","admin");
        li.add(map);


        System.out.println("li"+JSONUtil.toJson(li));

        ArrayList<Map<String,String>> li2 = (ArrayList)li.clone();
        //ArrayList<Map<String,String>> li3 = (ArrayList) SerializationUtils.clone(li);

        li2.get(0).put("123","123");
        //li3.get(0).put("123","123");
        System.out.println("li"+JSONUtil.toJson(li));
        System.out.println("li2"+JSONUtil.toJson(li2));
        //System.out.println("li3"+JSONUtil.toJson(li3));
    }

    @Test
    public void subString(){
        String content = "客服@maqiumeng";
        String key = "客服@";
        System.out.println(content.indexOf(key));
        String cJid =content.substring(key.length());
        System.out.println(cJid);
    }
}
