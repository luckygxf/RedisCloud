package com.gxf.web.controller;

import com.gxf.dao.MachineStaticsDao;
import com.gxf.entity.MachineStatics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * Created by 58 on 2017/8/1.
 */
@Controller
@RequestMapping("/machinestatics")
public class MachineStaticsController {

    @Autowired
    private MachineStaticsDao machineStaticsDao;

    /**
     * 查询所有的机器统计信息
     * */
    @RequestMapping("/list")
    public ModelAndView listMachines(){
        List<MachineStatics> listOfMachineStatics = machineStaticsDao.queryAll();
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.getModel().put("listOfMachineStatics", listOfMachineStatics);
        modelAndView.setViewName("machineStaticsList");
        return modelAndView;
    }
}
