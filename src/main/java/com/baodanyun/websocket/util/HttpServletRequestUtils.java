package com.baodanyun.websocket.util;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;

/**
 * Created by zrm on 2016-07-26 14:15.
 */
public class HttpServletRequestUtils {


    public static String getBody(HttpServletRequest request) throws IOException {
        StringBuffer buffer = new StringBuffer();

        BufferedReader reader = null;
        try {
            reader = request.getReader();
            String inputLine;
            while ((inputLine = reader.readLine()) != null) {
                buffer.append(inputLine);
            }

        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                }
            }
        }
        return buffer.toString();
    }

}
