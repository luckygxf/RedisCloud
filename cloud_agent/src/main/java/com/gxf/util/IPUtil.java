package com.gxf.util;

import com.gxf.agent.commandExec.CommandExec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by 58 on 2017/8/1.
 */
public class IPUtil {

    private static Logger logger = LoggerFactory.getLogger(IPUtil.class);
    private static final String COMMAND_HOSTNAME_I = "hostname -i";

    /**
     * 获取虚拟机IP
     * */
    public static String getVirtualMachineIP(){
        String executeResult = "";
        try {
            //hostname -i返回的数据
            //::1 192.168.211.131
            executeResult = CommandExec.execute(COMMAND_HOSTNAME_I);
            String patternString = "((?:(?:25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d)))\\.){3}(?:25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d))))";
            Pattern pattern = Pattern.compile(patternString);
            Matcher matcher = pattern.matcher(executeResult);
            if(matcher.find()){
                executeResult = matcher.group(0);
            }
        } catch (Exception e) {
            logger.error("get agent host failed");
            logger.error(e.getMessage(), e);
        }
        return executeResult;
    }
}
