package com.gxf.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by 58 on 2017/7/25.
 */
@Controller
@RequestMapping("/manage/app")
public class AppManageController {

    @RequestMapping("/initAppDeploy")
    public String initAppDeploy(){

        return "initAppDeploy";
    }

    @RequestMapping("/deployInstance")
    public ModelAndView deployInstance(){

        return new ModelAndView("initAppDeploy");
    }
}
