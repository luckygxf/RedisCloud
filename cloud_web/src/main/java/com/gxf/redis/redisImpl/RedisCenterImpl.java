package com.gxf.redis.redisImpl;

import com.gxf.common.util.IdempotentConfirmer;
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
}
