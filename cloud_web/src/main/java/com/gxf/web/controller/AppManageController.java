package com.gxf.web.controller;

import com.gxf.common.util.ConstUtil;
import com.gxf.dao.InstanceInfoDao;
import com.gxf.entity.InstanceInfo;
import com.gxf.machine.MachineCenter;
import com.gxf.redis.RedisCenter;
import com.gxf.redis.RedisDeployCenter;
import com.gxf.util.EtcdUtil;
import com.gxf.webJedis.WebJedis;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 58 on 2017/7/25.
 */
@Controller
@RequestMapping("/manage/app")
public class AppManageController {
    @Autowired
    private RedisCenter redisCenter;
    @Autowired
    private RedisDeployCenter redisDeployCenter;
    @Autowired
    private InstanceInfoDao instanceInfoDao;
    @Autowired
    private MachineCenter machineCenter;
    private static Logger logger = LoggerFactory.getLogger(AppManageController.class);

    @RequestMapping("/initAppDeploy")
    public String initAppDeploy(){

        return "initAppDeploy";
    }

    @RequestMapping("/deployInstance")
    public ModelAndView deployInstance(InstanceInfo instanceInfo){
        redisDeployCenter.deployRedisInstance(instanceInfo.getHost(), instanceInfo.getPort(), ConstUtil.CACHE_REDIS_STANDALONE);
        return new ModelAndView("initAppDeploy");
    }

    @RequestMapping("/deploySentinelModel")
    public ModelAndView deploySentinelModel(HttpServletRequest request){
        String masterHost = request.getParameter("masterHost");
        int masterPort = Integer.valueOf(request.getParameter("masterPort").trim());
        String slaveHost = request.getParameter("slaveHost");
        int slavePort = Integer.valueOf(request.getParameter("slavePort").trim());
        String[] slaveHosts = {slaveHost};
        String[] sentinelIps = {"192.168.211.131"};
        int sentinelPorts[] = {26340, 26341, 26342};
        int type = ConstUtil.CACHE_REDIS_STANDALONE;
        List<WebJedis> webJedisList = new ArrayList<WebJedis>();

        boolean isSuccess = redisDeployCenter.deploySentinelModel(masterHost, slaveHosts, type, sentinelIps, masterPort, slavePort, sentinelPorts, webJedisList);
        if(!isSuccess){
            logger.error("delpoySentinelModel failed");
        }
        else {
            logger.info("deploySentinelModel success");
            EtcdUtil.createInstanceNode(masterHost, masterPort);
            EtcdUtil.createInstanceNode(slaveHost, slavePort);
        }
        return new ModelAndView("initAppDeploy");
    }

    /**
     * 启动redis实例
     * */
    @RequestMapping("/startRedisInstance")
    public void startRedisInstance(HttpServletRequest httpServletRequest){
        //这里需要前端传回ip、端口
        //查询数据库，传redis密码给agent
        String host = httpServletRequest.getParameter("host");
        int port = Integer.valueOf(httpServletRequest.getParameter("port").trim());
        InstanceInfo instanceInfo = instanceInfoDao.queryByHostAndPort(host, port);
        boolean isSuccess = machineCenter.startProcessAtPort(host, port, ConstUtil.CACHE_REDIS_STANDALONE, instanceInfo.getPassword());
        if(isSuccess){
            logger.info("start redis instance success, host:{}, port:{}", host, port);
        }else{
            logger.error("start redis instance failed, host:{}, port:{}", host, port);
        }
    }
}
