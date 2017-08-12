package com.gxf.dao.impl;

import com.gxf.dao.InstanceInfoDao;
import com.gxf.entity.InstanceInfo;
import com.gxf.util.SqlSessionFactoryUtil;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by 58 on 2017/8/2.
 */
public class InstanceInfoDaoImpl implements InstanceInfoDao {
    private static SqlSessionFactory sessionFactory = SqlSessionFactoryUtil.sessionFactiory;
    private static Logger logger = LoggerFactory.getLogger(InstanceInfoDaoImpl.class);

    public static void main(String[] args) {
        String host = "192.168.211.131";
        int port = 6340;
        InstanceInfo instanceInfo = new InstanceInfoDaoImpl().queryInstanceInfoByHostAndPort(host, port);
        System.out.println(instanceInfo.getPassword());
    }

    /**
     * 添加redis实例信息
     * */
    public void addInstanceInfo(InstanceInfo instanceInfo) {
        SqlSession session = null;
        try{
            session = sessionFactory.openSession();
            InstanceInfoDao instanceInfoDao = session.getMapper(InstanceInfoDao.class);
            instanceInfoDao.addInstanceInfo(instanceInfo);
            session.commit();
            if(session != null){
                session.close();
            }
        } catch (Exception e){
            logger.error(e.getMessage(), e);
        } finally {
            if(session != null){
                try{
                    session.close();
                } catch (Exception e){
                    logger.error(e.getMessage(), e);
                }
            }
        } //finally
    }

    /**
     * 根据host和端口查询redis实例信息
     * */
    public InstanceInfo queryInstanceInfoByHostAndPort(String host, int port) {
        SqlSession session = null;
        InstanceInfo instanceInfo = null;
        try{
            session = sessionFactory.openSession();
            InstanceInfoDao instanceInfoDao = session.getMapper(InstanceInfoDao.class);
            instanceInfo = instanceInfoDao.queryInstanceInfoByHostAndPort(host, port);
            session.commit();
            if(session != null){
                session.close();
            }
        } catch (Exception e){
            logger.error(e.getMessage(), e);
        } finally {
            if(session != null){
                try{
                    session.close();
                } catch (Exception e){
                    logger.error(e.getMessage(), e);
                }
            }
        } //finally
        return instanceInfo;
    }

    public List<InstanceInfo> queryByAppId(int appId) {
        SqlSession session = null;
        List<InstanceInfo> listOfInstanceInfo = null;
        try{
            session = sessionFactory.openSession();
            InstanceInfoDao instanceInfoDao = session.getMapper(InstanceInfoDao.class);
            listOfInstanceInfo = instanceInfoDao.queryByAppId(appId);
            session.commit();
            if(session != null){
                session.close();
            }
        } catch (Exception e){
            logger.error(e.getMessage(), e);
        } finally {
            if(session != null){
                try{
                    session.close();
                } catch (Exception e){
                    logger.error(e.getMessage(), e);
                }
            }
        } //finally
        return listOfInstanceInfo;
    }
}
