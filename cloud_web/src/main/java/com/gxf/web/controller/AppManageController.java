package com.gxf.web.controller;

import com.gxf.common.util.ConstUtil;
import com.gxf.dao.AppDescDao;
import com.gxf.dao.InstanceInfoDao;
import com.gxf.entity.AppDesc;
import com.gxf.entity.InstanceInfo;
import com.gxf.enums.AppStatus;
import com.gxf.machine.MachineCenter;
import com.gxf.redis.RedisCenter;
import com.gxf.redis.RedisDeployCenter;
import com.gxf.util.EtcdUtil;
import com.gxf.util.PasswordUtil;
import com.gxf.webJedis.WebJedis;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
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
    @Autowired
    private AppDescDao appDescDao;

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
        String[] sentinelIps = {"192.168.211.131", "192.168.211.132", "192.168.211.133"};
        int sentinelPorts[] = {26340, 26340, 26340};
        int type = ConstUtil.CACHE_REDIS_SENTINEL;
        List<WebJedis> webJedisList = new ArrayList<WebJedis>();
        int appId = masterPort;
        AppDesc appDesc = new AppDesc();
        appDesc.setAppId(appId);
        appDesc.setPassword(PasswordUtil.genRandomNum(16));
        appDesc.setType(type);
        appDesc.setName(String.valueOf(appId));
        appDesc.setCreateTime(new Date());
        appDesc.setAppKey(PasswordUtil.getAppkey());
        appDesc.setAppPort(appId);
        appDescDao.add(appDesc);

        boolean isSuccess = redisDeployCenter.deploySentinelModel(appId, masterHost, slaveHosts, type, sentinelIps, masterPort, slavePort, sentinelPorts, webJedisList);
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

    @RequestMapping("/initDeployCluster")
    public String initDeployCluster(){
        return "deployCluster";
    }

    /**
     * 部署集群模式
     * */
    @RequestMapping("/deployCluster")
    public ModelAndView deployCluster(HttpServletRequest httpServletRequest){
        String masterHost = httpServletRequest.getParameter("masterHost").trim();
        String slaveHost = httpServletRequest.getParameter("slaveHost").trim();
        String masterPort = httpServletRequest.getParameter("masterPort").trim();
        String slavePort = httpServletRequest.getParameter("slavePort").trim();
        int masterPorts[] = new int[masterPort.split(",").length];
        int slavePorts[] = new int[slavePort.split(",").length];
        for(int i = 0; i < masterPorts.length; i++){
            masterPorts[i] = Integer.parseInt(masterPort.split(",")[i].trim());
        }
        for(int i = 0; i < slavePorts.length; i++){
            slavePorts[i] = Integer.parseInt(slavePort.split(",")[i].trim());
        }
        int appId = appDescDao.getMaxAppId() + 1;
        redisDeployCenter.deployCluster(masterHost, slaveHost, masterPorts, slavePorts, appId);
        AppDesc appDesc = new AppDesc();
        appDesc.setAppId(appId);
        appDesc.setName(String.valueOf(appId));
        appDesc.setAppPort(0);
        appDesc.setStatus(AppStatus.RUNNING.getValue());
        appDesc.setCreateTime(new Date());
        appDesc.setType(ConstUtil.CACHE_TYPE_REDIS_CLUSTER);
        String appKey = PasswordUtil.getAppkey();
        appDesc.setAppKey(appKey);
        appDescDao.add(appDesc);
        return new ModelAndView("deployCluster");
    }
}
