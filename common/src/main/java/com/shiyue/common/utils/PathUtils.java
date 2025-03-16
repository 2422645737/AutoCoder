package com.shiyue.common.utils;

import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * 路径URL工具类
 * @author 24226
 * @date 2025/02/11
 */
@Component
public class PathUtils {

    @Resource
    private  RequestMappingHandlerMapping requestMappingHandlerMapping;


    /**
     * 通过url获取对应的Method，用于后续解析源码
     * @param pathUrl
     * @return {@link Method }
     */
    public Method getMethod(String pathUrl){
        Map<RequestMappingInfo, HandlerMethod> handlerMethods = requestMappingHandlerMapping.getHandlerMethods();
        for (Map.Entry<RequestMappingInfo, HandlerMethod> entry : handlerMethods.entrySet()) {
            RequestMappingInfo key = entry.getKey();
            HandlerMethod value = entry.getValue();
            for (String pattern : key.getPatternValues()) {
                if (pattern.equals(pathUrl)) {
                    Method method = value.getMethod();
                    System.out.println("Found method: " + method.getDeclaringClass().getName() + "." + method.getName());
                    return method;
                }
            }
        }
        return null;
    }

    public static String join(String... paths){
        StringBuilder sb = new StringBuilder();
        if (paths != null && paths.length > 0){
            for (String path : paths) {
                if (path != null && !path.isEmpty()){
                    int len = path.length();
                    if(path.charAt(len - 1) == '/'){
                        path = path.substring(0,len - 1);
                    }
                    if(path.charAt(0) != '/'){
                        path = "/" + path;
                    }
                    sb.append(path);
                }
            }
        }
        return sb.toString();
    }

    /**
     * 移除路径最前方的'/'
     * @param path
     * @return {@link String }
     */

    public static String removeFirstSlash(String path){
        if (path != null && !path.isEmpty()){
            int len = path.length();
            if(path.charAt(0) == '/'){
                path = path.substring(1,len);
            }
        }
        return path;
    }
}