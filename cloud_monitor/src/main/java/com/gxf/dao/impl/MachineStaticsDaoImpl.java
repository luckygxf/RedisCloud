package com.gxf.dao.impl;

import com.gxf.dao.MachineStaticsDao;
import com.gxf.entity.MachineStatics;
import com.gxf.util.SqlSessionFactoryUtil;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 58 on 2017/7/31.
 */
public class MachineStaticsDaoImpl implements MachineStaticsDao {
    private static SqlSessionFactory sessionFactory = SqlSessionFactoryUtil.sessionFactiory;
    private static Logger logger = LoggerFactory.getLogger(MachineStaticsDaoImpl.class);

    public void add(MachineStatics machineStatics) {
        SqlSession session = null;
        try{
            session = sessionFactory.openSession();
            MachineStaticsDao machineStaticsDao = session.getMapper(MachineStaticsDao.class);
            machineStaticsDao.add(machineStatics);
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

    public List<MachineStatics> queryAll() {
        List<MachineStatics> result = new ArrayList<MachineStatics>();
        SqlSession session = null;
        try{
            session = sessionFactory.openSession();
            MachineStaticsDao machineStaticsDao = session.getMapper(MachineStaticsDao.class);
            result = machineStaticsDao.queryAll();
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

        return result;
    }
}
