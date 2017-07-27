package com.gxf.web.controller;

import com.gxf.common.util.ConstUtil;
import com.gxf.entity.InstanceInfo;
import com.gxf.redis.RedisCenter;
import com.gxf.redis.RedisDeployCenter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

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

    @RequestMapping("/initAppDeploy")
    public String initAppDeploy(){

        return "initAppDeploy";
    }

    @RequestMapping("/deployInstance")
    public ModelAndView deployInstance(InstanceInfo instanceInfo){
        redisDeployCenter.deployRedisInstance(instanceInfo.getHost(), instanceInfo.getPort(), ConstUtil.CACHE_REDIS_STANDALONE);
        return new ModelAndView("initAppDeploy");
    }
}
