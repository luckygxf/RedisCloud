package com.gxf.cache.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * Created by 58 on 2017/7/16.
 */
@SpringBootApplication
public class ApplicationStarter{
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(ApplicationStarter.class);
        app.run(args);
    }
}
