package com.gxf.test;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;

/**
 * Created by 58 on 2017/7/26.
 */
@Controller
@RequestMapping(value = "/test")
public class TestController {

    @RequestMapping(value = "/testRb")
    @ResponseBody
    public Employee testRb(Employee e){
        System.out.println("in controller..");
        return e;
    }

    @RequestMapping(value = "/testCustomObj")
    @ResponseBody
    public Employee testCustomObj(Employee e){
        return e;
    }

    @RequestMapping(value = "/testCustomObjWithRp")
    @ResponseBody
    public Employee testCustomObjWeithRp(@RequestParam Employee e){
        return e;
    }

    @RequestMapping(value = "/testDate")
    @ResponseBody
    public Date testDate(Date date){
        return date;
    }
}
