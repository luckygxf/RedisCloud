package com.gxf.web.controller;

import com.gxf.dao.InstanceStaticsDao;
import com.gxf.entity.InstanceStatics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * Created by 58 on 2017/8/2.
 */

@Controller
@RequestMapping("/instancestatics")
public class InstanceStaticsController {

    @Autowired
    private InstanceStaticsDao instanceStaticsDao;

    @RequestMapping("/list")
    public ModelAndView listInstanceStatics(){
        ModelAndView modelAndView = new ModelAndView();
        List<InstanceStatics> list = instanceStaticsDao.queryAll();
        modelAndView.getModel().put("list", list);
        modelAndView.setViewName("instanceStaticsList");
        return modelAndView;
    }
}
