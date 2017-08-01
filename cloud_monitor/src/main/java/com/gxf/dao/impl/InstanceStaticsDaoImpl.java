package com.gxf.dao.impl;

import com.gxf.dao.InstanceStaticsDao;
import com.gxf.entity.InstatnceStatics;
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

    public void add(InstatnceStatics instatnceStatics) {
        SqlSession session = null;
        try{
            session = sessionFactory.openSession();
            InstanceStaticsDao instanceStaticsDao = session.getMapper(InstanceStaticsDao.class);
            instanceStaticsDao.add(instatnceStatics);
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
