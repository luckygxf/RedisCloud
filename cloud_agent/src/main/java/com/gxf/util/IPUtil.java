package com.gxf.util;

import com.gxf.agent.commandExec.CommandExec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

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
            executeResult = executeResult.substring(4);
        } catch (Exception e) {
            logger.error("get agent host failed");
            logger.error(e.getMessage(), e);
        }
        return executeResult;
    }
}
