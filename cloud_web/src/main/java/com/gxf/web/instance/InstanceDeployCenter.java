package com.gxf.web.instance;

/**
 * Created by 58 on 2017/8/12.
 */
public interface InstanceDeployCenter {

    /**
     * 启动已经存在的实例
     * */
    boolean startExistInstance(int instanceId);
}
