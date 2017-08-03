package com.gxf.web.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

/**
 * Created by 58 on 2017/8/3.
 */
public class ResponseUtil {
    private static Logger logger = LoggerFactory.getLogger(ResponseUtil.class);

    /**
     * 向httpresponse中写内容
     * */
    public static void writeResonponseContent(HttpServletResponse httpServletResponse, String content){
        OutputStream outputStream = null;
        try{
            outputStream = httpServletResponse.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream));
            bufferedWriter.write(content);
            bufferedWriter.flush();
        }catch (Exception e){
            logger.error("write content:{} to response failed.", content);
            logger.error(e.getMessage(), e);
        }
    }
}
