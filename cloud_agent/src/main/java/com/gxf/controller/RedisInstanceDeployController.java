package com.gxf.controller;

import com.gxf.agent.commandExec.CommandExec;
import com.gxf.agent.constants.BaseConstant;
import com.gxf.service.MachineCenter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by 58 on 2017/7/13.
 */
public class RedisInstanceDeployController {
    private static Logger logger = LoggerFactory.getLogger(RedisInstanceDeployController.class);

    /**
     * 启动redis实例
     * */
    public static boolean runInstance(int port, int type, String password, String configFileName, List<String> redisConfigs, String runShell, String machinePath){
        printConfig(redisConfigs, type, port);
        boolean isSuccess = CommandExec.createConfigFile(configFileName, redisConfigs, machinePath);
        if(!isSuccess){
            System.out.println("runInstance:create config file" + machinePath + configFileName + " failed.");
            return false;
        }
        System.out.println("runInstance:create config file" + machinePath + configFileName + " success.");
        System.out.println("runInstance:exec shell = " + runShell);
        boolean isStartSuccess = MachineCenter.startRedisAtPort(port, password, type, runShell);

        if(!isStartSuccess){
            System.out.println("runInstance:exec shell = " + runShell + " failed.");
            return false;
        }

        System.out.println("runInstance:start redis instance at port: " + port + "success");

        return true;
    }

    /**
     * 启动sentinel实例
     * machinePath用于创建配置文件，文件路径
     *
     * */
    public static boolean runSentinel(int port, String configFileName, List<String> confis, String runShell
                                        ,String machinePath){
        printConfig(confis, BaseConstant.CACHE_REDIS_SENTINEL, port);
        boolean isSuccess = CommandExec.createConfigFile(configFileName, confis, machinePath);
        if (!isSuccess){
            logger.error("runSentinel: create config file:{} failed", machinePath + configFileName);
            return false;
        }
        logger.info("runSentinel: create config file:{} success", machinePath + configFileName);
        boolean isStartSuccess = MachineCenter.startRedisAtPort(port, "", BaseConstant.CACHE_REDIS_SENTINEL, runShell);
        if(!isStartSuccess){
            logger.error("runSentinel:exec shell = {} error" , runShell);
            return false;
        }

        logger.info("runSentinel: start redis nstance at port:{} success", port);
        return true;
    }

    /***
     * 打印接收到的redis配置，类型，端口
     * */
    private static void printConfig(List<String> redisConfig, int type, int port){
        System.out.println("config from web manager");
        for(String line : redisConfig){
            System.out.println(line);
        }
    }
}
