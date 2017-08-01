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

    /**
     * 更新机器收集信息
     * */
    public void updateMachineStatics(MachineStatics machineStatics){
        SqlSession session = null;
        try{
            session = sessionFactory.openSession();
            MachineStaticsDao machineStaticsDao = session.getMapper(MachineStaticsDao.class);
            machineStaticsDao.updateMachineStatics(machineStatics);
            session.commit();
            if(session != null){
                session.close();
            }
        } catch (Exception e){
            logger.error("MachineStaticsDao udpate statics failed.");
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
     * 根据机器IP查询机器收集信息
     * */
    public MachineStatics queryMachineStaticsByIp(String ip) {
        MachineStatics machineStatics = null;
        SqlSession session = null;
        try{
            session = sessionFactory.openSession();
            MachineStaticsDao machineStaticsDao = session.getMapper(MachineStaticsDao.class);
            machineStatics = machineStaticsDao.queryMachineStaticsByIp(ip);
            if(session != null){
                session.close();
            }
        } catch (Exception e){
            logger.error("MachineStaticsDao udpate statics failed.");
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
        return machineStatics;
    }
}
