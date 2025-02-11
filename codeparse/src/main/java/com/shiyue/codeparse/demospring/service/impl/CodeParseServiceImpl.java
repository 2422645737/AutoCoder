package com.shiyue.codeparse.demospring.service.impl;

import com.shiyue.codeparse.demospring.entity.Param;
import com.shiyue.codeparse.demospring.service.CodeParseService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;

/**
 * @description:
 * @fileName: CodeParseServiceImpl
 * @author: wanghui
 * @createAt: 2025/02/11 05:27:31
 * @updateBy:
 * @copyright:
 */

@Service
public class CodeParseServiceImpl implements CodeParseService {

    @Resource
    private RequestMappingHandlerMapping requestMappingHandlerMapping;
    @Override
    public String parse(String code) {
        //将code拆分为字符串数组
        System.out.println(code);
        //深度优先搜索算法
        for (int i = 0; i < code.length(); i++) {
            //如果当前字符是字母或者数字，则继续遍历
            if (Character.isLetterOrDigit(code.charAt(i))) {
                //如果当前字符是字母，则将当前字符和下一个字符拼接起来，如果拼接后的字符在字典中存在，则将其替换为对应的中文字符
                if (Character.isLetter(code.charAt(i))) {
                    StringBuilder sb = new StringBuilder();
                   sb.append(code.charAt(i));
                }
            }
        }

        return getOldCode(code);
    }

    /**
     * 解析源代码
     * @param param
     * @return {@link String }
     */

    @Override
    public String getSourceCode(Param param) {
        Map<RequestMappingInfo, HandlerMethod> handlerMethods = requestMappingHandlerMapping.getHandlerMethods();
        for (Map.Entry<RequestMappingInfo, HandlerMethod> entry : handlerMethods.entrySet()) {
            RequestMappingInfo key = entry.getKey();
            HandlerMethod value = entry.getValue();
            for (String pattern : key.getPatternValues()) {
                if (pattern.equals(param.getPath())) {
                    Method method = value.getMethod();
                    System.out.println("Found method: " + method.getDeclaringClass().getName() + "." + method.getName());
                }
            }



        }
        return null;
    }

    /**
     * 获取旧代码
     * @param code
     * @return {@link String }
     */

    private String getOldCode(String code) {
        //生成一段随机字符串
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            sb.append((char) (Math.random() * 26 + 'a'));
        }
        return sb.toString();
    }

}