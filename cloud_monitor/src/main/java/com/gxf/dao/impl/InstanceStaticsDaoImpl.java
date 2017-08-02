package com.gxf.dao.impl;

import com.gxf.dao.InstanceStaticsDao;
import com.gxf.entity.InstanceStatics;
import com.gxf.util.SqlSessionFactoryUtil;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by 58 on 2017/8/1.
 */
public class InstanceStaticsDaoImpl implements InstanceStaticsDao {
    private static SqlSessionFactory sessionFactory = SqlSessionFactoryUtil.sessionFactiory;
    private static Logger logger = LoggerFactory.getLogger(MachineStaticsDaoImpl.class);

    public static void main(String[] args) {
        String host = "192.168.211.131";
        int port = 0;
        InstanceStatics instanceStatics = new InstanceStaticsDaoImpl().queryByHostAndPort(host, port);
        instanceStatics.setMaxMemory(1111L);
        new InstanceStaticsDaoImpl().update(instanceStatics);
        System.out.println(instanceStatics.getRole());
    }

    public void add(InstanceStatics instanceStatics) {
        SqlSession session = null;
        try{
            session = sessionFactory.openSession();
            InstanceStaticsDao instanceStaticsDao = session.getMapper(InstanceStaticsDao.class);
            instanceStaticsDao.add(instanceStatics);
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

    public InstanceStatics queryByHostAndPort(String host, int port) {
        SqlSession session = null;
        InstanceStatics instanceStatics = null;
        try{
            session = sessionFactory.openSession();
            InstanceStaticsDao instanceStaticsDao = session.getMapper(InstanceStaticsDao.class);
            instanceStatics = instanceStaticsDao.queryByHostAndPort(host, port);
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
        return instanceStatics;
    }

    public void update(InstanceStatics instanceStatics) {
        SqlSession session = null;
        try{
            session = sessionFactory.openSession();
            InstanceStaticsDao instanceStaticsDao = session.getMapper(InstanceStaticsDao.class);
            instanceStaticsDao.update(instanceStatics);
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
}
