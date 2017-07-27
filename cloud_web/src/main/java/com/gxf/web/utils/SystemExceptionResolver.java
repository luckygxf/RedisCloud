package com.gxf.web.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by 58 on 2017/7/27.
 */
public class SystemExceptionResolver implements HandlerExceptionResolver {
    private static Logger logger = LoggerFactory.getLogger(SystemExceptionResolver.class);

    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        if(null != ex){
            logger.error(ex.getMessage(), ex);
        }
        ModelAndView modelAndView = new ModelAndView("error/error");
        return modelAndView;
    }
}
