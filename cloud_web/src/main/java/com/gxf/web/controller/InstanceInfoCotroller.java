package com.gxf.web.controller;

import com.gxf.common.util.ConstUtil;
import com.gxf.dao.InstanceInfoDao;
import com.gxf.entity.InstanceInfo;
import com.gxf.enums.InstanceStatusEnum;
import com.gxf.enums.ResponseCode;
import com.gxf.machine.MachineCenter;
import com.gxf.redis.RedisCenter;
import com.gxf.web.utils.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by 58 on 2017/8/3.
 */
@RequestMapping("/instanceInfo")
@Controller
public class InstanceInfoCotroller {
    private static Logger logger = LoggerFactory.getLogger(InstanceInfoCotroller.class);
    @Autowired
    private InstanceInfoDao instanceInfoDao;
    @Autowired
    private MachineCenter machineCenter;
    @Autowired
    private RedisCenter redisCenter;

    @RequestMapping("/list")
    public ModelAndView list(){
        List<InstanceInfo> infoList = instanceInfoDao.queryAll();
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.getModel().put("infoList", infoList);
        modelAndView.setViewName("instanceInfoList");
        return modelAndView;
    }

    /**
     * 启动redis实例
     * */
    @RequestMapping("/startRedisInstance")
    public void startRedisInstance(HttpServletRequest request, HttpServletResponse response){
        int instanceInfoId = Integer.valueOf(request.getParameter("instanceInfoId").trim());
        InstanceInfo instanceInfo = instanceInfoDao.queryById(instanceInfoId);
        boolean isSuccess = machineCenter.startProcessAtPort(instanceInfo.getHost(), instanceInfo.getPort(), instanceInfo.getType(), instanceInfo.getPassword());
        if(isSuccess){
            logger.info("startRedisInstance success, host:{}, port{}", instanceInfo.getHost(), instanceInfo.getPort());
            instanceInfo.setStatus(InstanceStatusEnum.RUNNING.getValue());
            instanceInfoDao.update(instanceInfo);
            ResponseUtil.writeResonponseContent(response, String.valueOf(ResponseCode.SUCCESS.getValue()));
        }else{
            logger.error("startRedisInstance failed, host:{}, port{}", instanceInfo.getHost(), instanceInfo.getPort());
            instanceInfo.setStatus(InstanceStatusEnum.NOT_RUN.getValue());
            instanceInfoDao.update(instanceInfo);
            ResponseUtil.writeResonponseContent(response, String.valueOf(ResponseCode.FAILED.getValue()));
        }
    }

    /**
     * 关闭redis实例
     * */
    @RequestMapping("/shutdownRedisInstance")
    public void shutdownRedisInstance(HttpServletRequest request, HttpServletResponse response){
        int instanceInfoId = Integer.valueOf(request.getParameter("instanceInfoId").trim());
        InstanceInfo instanceInfo = instanceInfoDao.queryById(instanceInfoId);
        boolean isSuccess = redisCenter.shutdownRedis(instanceInfo.getHost(), instanceInfo.getPort(), instanceInfo.getPassword());
        if(isSuccess){
            logger.info("shutdownRedisInstance success, host:{}, port{}", instanceInfo.getHost(), instanceInfo.getPort());
            instanceInfo.setStatus(InstanceStatusEnum.NOT_RUN.getValue());
            instanceInfoDao.update(instanceInfo);
            ResponseUtil.writeResonponseContent(response, String.valueOf(ResponseCode.SUCCESS.getValue()));
        }else{
            logger.error("shutdownRedisInstance failed, host:{}, port{}", instanceInfo.getHost(), instanceInfo.getPort());
            instanceInfo.setStatus(InstanceStatusEnum.RUNNING.getValue());
            instanceInfoDao.update(instanceInfo);
            ResponseUtil.writeResonponseContent(response, String.valueOf(ResponseCode.FAILED.getValue()));
        }
    }

}
