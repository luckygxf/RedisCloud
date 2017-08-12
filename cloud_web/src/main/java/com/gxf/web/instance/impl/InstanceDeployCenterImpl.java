package com.gxf.web.instance.impl;

import com.gxf.dao.AppDescDao;
import com.gxf.dao.InstanceInfoDao;
import com.gxf.entity.AppDesc;
import com.gxf.entity.InstanceInfo;
import com.gxf.enums.InstanceStatusEnum;
import com.gxf.machine.MachineCenter;
import com.gxf.redis.RedisCenter;
import com.gxf.util.TypeUtil;
import com.gxf.web.instance.InstanceDeployCenter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by 58 on 2017/8/12.
 */
public class InstanceDeployCenterImpl implements InstanceDeployCenter {
    @Autowired
    private InstanceInfoDao instanceInfoDao;
    @Autowired
    private AppDescDao appDescDao;
    @Autowired
    private RedisCenter redisCenter;
    @Autowired
    private MachineCenter machineCenter;
    private static Logger logger = LoggerFactory.getLogger(InstanceDeployCenterImpl.class);

    @Override
    public boolean startExistInstance(int instanceId) {
        InstanceInfo instanceInfo = instanceInfoDao.queryById(instanceId);
        int type = instanceInfo.getType();
        String host = instanceInfo.getHost();
        int port = instanceInfo.getPort();
        boolean isRun;
        if(TypeUtil.isRedisType(type)){
            int appId = instanceInfo.getAppId();
            AppDesc appDesc = appDescDao.queryByAppid(appId);
            if(null == appDesc){
                return  false;
            } //if
            if(TypeUtil.isRedisSentinel(type)){
                isRun = redisCenter.isInstanceRun(host, port, "");
            }else{
                isRun = redisCenter.isInstanceRun(host, port, appDesc.getPassword());
            }
            if(isRun){
                logger.warn("{}:{} instance is Running", host, port);
            }else {
                boolean startSuccess = machineCenter.startProcessAtPort(host, port, type, appDesc.getPassword());
                if(!startSuccess){
                    logger.error("start host:{}, port:{} failed.", host, port);
                    return false;
                }else{
                    logger.info("host:{}, port:{} instance run success.", host, port);
                }
                if(TypeUtil.isRedisSentinel(type)){
                    isRun = redisCenter.isInstanceRun(host, port, "");
                }else{
                    isRun = redisCenter.isInstanceRun(host, port, appDesc.getPassword());
                }
            }
        } //if
        else{
            logger.error("type = {} not match!", type);
            isRun = false;
        }
        if (isRun){
            instanceInfo.setStatus(InstanceStatusEnum.RUNNING.getValue());
            instanceInfoDao.update(instanceInfo);
        }
        return isRun;
    }
}
