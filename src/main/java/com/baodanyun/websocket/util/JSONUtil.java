package com.baodanyun.websocket.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Type;


/**
 * Created by liaowuhen on 2016/10/31.
 */
public class JSONUtil {

    /**
     * 将对象编译成json
     * @param o
     * @return
     */
    public static String toJson(Object o){
        Gson gson = new GsonBuilder().disableHtmlEscaping().create();
        return gson.toJson(o);
    }

    /**
     *  将json编译成对象
     * @param
     * @return
     */
    public static <T> T toObject(Class<T> c, String json) throws Exception {
        Gson gson = new Gson();

        T msg = gson.fromJson(json,c);

        return msg;
    }


    /**
     * 将json编译成对象
     *
     * @param
     * @return
     */
    public static <T> T fromJson(String json, Type typeOfT) {
        Gson gson = new Gson();
        T msg = gson.fromJson(json, typeOfT);

        return msg;
    }



   /* public  static void  main(String args[]){
        List<String> li = new ArrayList<>();
        li.add("中国");
        li.add("Hanguo");

        String m = JSONUtil.toJson(li);

        System.out.println(m);

        List lis = JSONUtil.toObject(List.class,m);

        System.out.println(lis);

     }*/




}
