package com.gxf.web.controller;


import com.gxf.dao.InstanceConfigDao;
import com.gxf.entity.InstanceConfig;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@SpringBootApplication
@MapperScan("com.gxf.dao")
public class SampleController {

    @Autowired
    private InstanceConfigDao instanceConfigDao;

    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String index() {
        List<InstanceConfig> listOfConfig = instanceConfigDao.queryAll();
        for(int i = 0; i < listOfConfig.size(); i++){
            System.out.println(listOfConfig.get(i).getConfigKey());
        }
        return "index";
    }

    public static void main(String[] args) throws Exception {
        SpringApplication.run(SampleController.class, args);
    }
}
