package com.gxf.redis.redisImpl;

import com.gxf.common.util.IdempotentConfirmer;
import com.gxf.common.util.StringUtil;
import com.gxf.protocol.Protocol;
import com.gxf.redis.RedisCenter;
import com.gxf.webJedis.WebJedis;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by 58 on 2017/7/21.
 */
public class RedisCenterImpl implements RedisCenter {

    private static Logger logger = LoggerFactory.getLogger(RedisCenterImpl.class);

    /***
     * 执行redis config rewrite命令
     * */
    @Override
    public boolean configRewreite(final String host, final int port, final String password) {
        return new IdempotentConfirmer(){

            @Override
            public boolean execute() {
                WebJedis webJedis = new WebJedis(host, port, Protocol.DEFAULT_TIMEOUT * 2) ;
                webJedis.auth(password);
                try{
                    String response = webJedis.configRewrite();
                    return null != response && response.equalsIgnoreCase("OK");
                } catch (Exception e){
                    logger.error(e.getMessage(), e);
                    return false;
                } finally {
                    webJedis.close();
                } //finally
            } //execute
        }.run();
    }

    @Override
    public boolean shutdownRedis(String host, int port, String password) {
        boolean isInstanceRun = isInstanceRun(host, port, password);
        if(!isInstanceRun){
            return true;
        }

        final WebJedis jedis = new WebJedis(host, port);
        try{
            if(!StringUtil.isEmpty(password)){
                jedis.auth(password);
            }
            boolean isShutdown = new IdempotentConfirmer(){
                @Override
                public boolean execute(){
                    jedis.shutdown();
                    return true;
                }
            }.run();
            if(!isShutdown){
                logger.error("instance is shutdown failed. host:{}, port{}", host, port);
            }

            return isShutdown;
        } catch (Exception e){
            logger.error(e.getMessage(), e);
            return false;
        } finally {
            jedis.close();
        }
    }

    @Override
    public boolean isInstanceRun(final String host, final int port, final String password) {
        boolean isRun = new IdempotentConfirmer(){
            private int timeOutFactor = 1;

            @Override
            public boolean execute(){
                WebJedis jedis = new WebJedis(host, port);
                try{
                    if(!StringUtil.isEmpty(password)){
                        jedis.auth(password);
                    }
                    jedis.getClient().setConnectionTimeout(Protocol.DEFAULT_TIMEOUT * (timeOutFactor ++));
                    jedis.getClient().setSoTimeout(Protocol.DEFAULT_TIMEOUT * (timeOutFactor ++));
                    String pong = jedis.ping();
                    return pong != null && pong.equalsIgnoreCase("PONG");
                } catch (Exception e){
                    logger.error(e.getMessage(), e);
                    if(e.getMessage().startsWith("LOADING")){
                        return true;
                    }
                    logger.error("shutdown instance failed, host:{}, port:{}", host, port);
                    return false;
                } finally {
                    jedis.close();
                }
            }
        }.run();

        return isRun;
    }

    @Override
    public boolean isSingleClusterNode(String host, int port) {
        WebJedis jedis = new WebJedis(host, port);
        try{
            String clusterNodes = jedis.clusterNodes();
            if(StringUtil.isEmpty(clusterNodes)){
                logger.error("host:{}, port:{} clusterNodes is empty", host, port);
                return false;
            }
            String[] nodeInfos = clusterNodes.split("\n");

            return nodeInfos.length == 1 ? true : false;
        } catch (Exception e){
            logger.error(e.getMessage(), e);
            return false;
        } finally {
            jedis.close();
        }
    }
}
