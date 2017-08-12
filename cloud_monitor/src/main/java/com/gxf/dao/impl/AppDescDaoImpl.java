package com.gxf.dao.impl;

import com.gxf.dao.AppDescDao;
import com.gxf.dao.InstanceInfoDao;
import com.gxf.entity.AppDesc;
import com.gxf.entity.InstanceInfo;
import com.gxf.util.SqlSessionFactoryUtil;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by 58 on 2017/8/12.
 */
public class AppDescDaoImpl implements AppDescDao {
    private static SqlSessionFactory sessionFactory = SqlSessionFactoryUtil.sessionFactiory;
    private static Logger logger = LoggerFactory.getLogger(AppDescDaoImpl.class);


    public AppDesc queryByAppkey(@Param("appKey") String appkey) {
        SqlSession session = null;
        AppDesc appDesc = null;
        try{
            session = sessionFactory.openSession();
            AppDescDao appDescDao = session.getMapper(AppDescDao.class);
            appDesc = appDescDao.queryByAppkey(appkey);
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
        return appDesc;
    }
}
