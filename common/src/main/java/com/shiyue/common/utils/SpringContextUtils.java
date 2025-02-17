package com.shiyue.common.utils;

import jakarta.annotation.Resource;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @description:
 * @fileName: SpringContextUtils
 * @author: wanghui
 * @createAt: 2025/02/17 05:52:16
 * @updateBy:
 * @copyright:
 */

@Component
public class SpringContextUtils implements ApplicationContextAware {
    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringContextUtils.applicationContext = applicationContext;
    }


    public static String getProperty(String key) {
        //处理${name}的情况
        if (key.startsWith("${") && key.endsWith("}")) {
            key = key.substring(2, key.length() - 1);
        }
        return applicationContext.getEnvironment().getProperty(key);
    }
}