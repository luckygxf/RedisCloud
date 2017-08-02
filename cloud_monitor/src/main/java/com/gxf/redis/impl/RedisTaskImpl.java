package com.gxf.redis.impl;

import com.gxf.common.util.StringUtil;
import com.gxf.dao.InstanceInfoDao;
import com.gxf.dao.InstanceStaticsDao;
import com.gxf.dao.impl.InstanceInfoDaoImpl;
import com.gxf.dao.impl.InstanceStaticsDaoImpl;
import com.gxf.entity.InstanceInfo;
import com.gxf.entity.InstanceStatics;
import com.gxf.enums.InstanceRoleEnum;
import com.gxf.enums.InstanceStatusEnum;
import com.gxf.redis.RedisTask;
import com.gxf.util.EtcdUtil;
import com.gxf.util.HostAndPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by 58 on 2017/8/2.
 */
public class RedisTaskImpl implements RedisTask {
    private static Logger logger = LoggerFactory.getLogger(RedisTaskImpl.class);
    private static InstanceStaticsDao instanceStaticsDao = new InstanceStaticsDaoImpl();
    private static InstanceInfoDao instanceInfoDao = new InstanceInfoDaoImpl();
    private static ScheduledExecutorService scheduledExecutorService = new ScheduledThreadPoolExecutor(5);


    public static void main(String[] args) {
        RedisTask task = new RedisTaskImpl();
        scheduledExecutorService.scheduleAtFixedRate(task, 0, 60, TimeUnit.SECONDS);
    }
    /**
     * 定期监控redis实例
     * 1. 从etcd获取要监控的redis实例
     * 2. 启动线程池，每隔1分钟从redis实例拉取数据
     * 3. 更新收集到的数据到数据库
     * */
    public void run(){
        //1. 获取到所有etcd注册的redis实例
        List<HostAndPort> appList = EtcdUtil.getAllAppList();
        for(HostAndPort hostAndPort : appList){
            InstanceInfo instanceInfo = instanceInfoDao.queryInstanceInfoByHostAndPort(hostAndPort.getHost(), hostAndPort.getPort());
            InstanceStatics instanceStatics = getRedisInstanceInfo(hostAndPort.getHost(), hostAndPort.getPort(), instanceInfo.getPassword());
            InstanceStatics instanceStaticsFromDB = instanceStaticsDao.queryByHostAndPort(hostAndPort.getHost(), hostAndPort.getPort());
            if(null == instanceStaticsFromDB){
                instanceStaticsDao.add(instanceStatics);
            }else{
                instanceStaticsDao.update(instanceStatics);
            }
        }
    }

    /**
     * 收集redis实例信息
     * */
    public InstanceStatics getRedisInstanceInfo(String ip, int port, String password) {
        String info = getRedisInfo(ip, port, password);
        InstanceStatics instanceStatics =  processInfo(info);
        instanceStatics.setIp(ip);
        instanceStatics.setPort(port);
        if(!StringUtil.isEmpty(info)){
            instanceStatics.setIsRun(InstanceStatusEnum.RUNNING.getValue());
        }else{
            instanceStatics.setIsRun(InstanceStatusEnum.NOT_RUN.getValue());
        }
        return instanceStatics;
    }

    /**
     * 处理info，放到map集合里面
     * */
    private Map<String, Map<String, String>> getRedisStatMap(String info){
        Map<String, Map<String, String>> redisStatMap = new HashMap<String, Map<String, String>>();
        //TODO:wredis这里做了分区处理，后面需要在研究一下
        return redisStatMap;
    }

    /**
     * 根据获取到的info信息，解析出需要的信息
     * */
    private InstanceStatics processInfo(String info){
        InstanceStatics instanceStatics = new InstanceStatics();
        //把所有info数据放到一个map里
        Map<String, String> redisStatMap = new HashMap<String, String>();
        String statData[] = info.split("\r\n");
        for(String statLine : statData){
            if(StringUtil.isEmpty(statLine) || statLine.indexOf("#") >= 0){
                continue;
            }
            if(statLine.contains(":")){
                String keyValue[] = statLine.split(":");
                redisStatMap.put(keyValue[0].trim(), keyValue[1].trim());
            }
        }
        try{
            //填充instanceStatics
            String role = redisStatMap.get("role");
            instanceStatics.setRole(InstanceRoleEnum.getValueByDesc(role));
            instanceStatics.setMaxMemory(Long.valueOf(redisStatMap.get("maxmemory")));
            instanceStatics.setUsedMemory(Long.valueOf(redisStatMap.get("used_memory")));
            //# Keyspace
            //db0:keys=3,expires=0,avg_ttl=0
            String db0 = redisStatMap.get("db0");
            if(null != db0){
                String keys = db0.split(",")[0];
                instanceStatics.setCurrItems(Long.valueOf(keys.substring("keys=".length())));
            }
            instanceStatics.setCurrConnections(Integer.valueOf(redisStatMap.get("connected_clients")));
            instanceStatics.setMisses(Long.valueOf(redisStatMap.get("keyspace_misses")));
            instanceStatics.setHits(Long.valueOf(redisStatMap.get("keyspace_hits")));
            instanceStatics.setModifyTime(new Date());
        } catch (Exception e){
            logger.error(e.getMessage(), e);
        }
        return instanceStatics;
    }

    /**
     * 对redis实例执行info命令
     * */
    private String getRedisInfo(String ip, int port, String password){
        String info = "";
        try{
            Jedis jedis = new Jedis(ip, port);
            if(!StringUtil.isEmpty(password)){
                jedis.auth(password);
            }
            info = jedis.info();
            jedis.close();
        } catch (Exception e){
            logger.error("get redis instance host:{}, port:{} info failed.", ip, port);
            logger.error(e.getMessage(), e);
        }
        return info;
    }
}
