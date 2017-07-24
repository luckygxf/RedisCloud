package com.gxf.service;

import com.gxf.agent.commandExec.CommandExec;
import com.gxf.common.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

/**
 * Created by 58 on 2017/7/13.
 */
public class MachineCenter {
    private static Logger logger = LoggerFactory.getLogger(MachineCenter.class);

    /***
     * 在指定端口运行redis实例
     * */
    public static boolean startRedisAtPort(int port, String password, int type, String shell){
        if(port < 0 || port > 65535){
            System.out.println("port is invalid");
            return false;
        }
        if(StringUtil.isEmpty(shell)){
            System.out.println("shell is empty");
            return false;
        }
        boolean success = true;
        try {
            CommandExec.execute(shell);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        success = CommandExec.isPortUsed(port);
        if(success){
            System.out.println("port = " + port + " is used");
        }else {
            System.out.println("port = " + port + " is not used");
        }
        if(!success){
            success = CommandExec.isRedisRun(port, password, type);
            logger.info("success = {}", success);
            if(success){
                logger.info("redis is run at port = {}", port);
            }else {
                logger.info("redis is not run at port = {}" , port);
            }
        }

        return success;
    }

}
