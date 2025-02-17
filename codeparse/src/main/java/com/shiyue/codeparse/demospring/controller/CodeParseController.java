package com.shiyue.codeparse.demospring.controller;

import com.shiyue.codeparse.demospring.entity.Param;
import com.shiyue.codeparse.demospring.service.CodeParseService;
import com.shiyue.codeparse.parse.entity.MethodCallTree;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.io.FileNotFoundException;

/**
 * @description:
 * @fileName: CodeParseController
 * @author: wanghui
 * @createAt: 2025/02/11 05:45:00
 * @updateBy:
 * @copyright:
 */

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api")
public class CodeParseController {
    @Resource
    CodeParseService codeParseService;

    @PostMapping("/parse")
    public String parse(@RequestBody String code) {
        return codeParseService.parse(code);
    }


    /**
     * @param param
     * @return {@link MethodCallTree }
     */

    @PostMapping("/getSourceCode")
    public MethodCallTree getSourceCode(@RequestBody Param param) throws FileNotFoundException {
        getOldCode1(param.getPath());
        getOldCode2(param.getPath());
        getOldCode3(param.getPath());
        getOldCode0(param.getPath());
        return codeParseService.getSourceCode(param);
    }

    /**
     * 这是一个测试接口
     * @param param
     * @return {@link String }
     */

    @PostMapping("/getSourceCode1")
    public String getSourceCode1(@RequestBody Param param) throws FileNotFoundException {
        getOldCode1(param.getPath());
        getOldCode2(param.getPath());
        getOldCode3(param.getPath());
        getOldCode0(param.getPath());
        return codeParseService.parse(param.getPath());
    }
    private String getOldCode1(String code) {
        //生成一段随机字符串
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            sb.append((char) (Math.random() * 26 + 'a'));
        }
        getOldCode2(code);
        return sb.toString();
    }

    /**
     * 获取旧代码
     * @param code
     * @return {@link String }
     */

    private String getOldCode0(String code) {
        //生成一段随机字符串
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            sb.append((char) (Math.random() * 26 + 'a'));
        }
        return sb.toString();
    }

    /**
     * 获取旧代码
     * @param code
     * @return {@link String }
     */

    private String getOldCode2(String code) {
        //生成一段随机字符串
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            sb.append((char) (Math.random() * 26 + 'a'));
        }
        getOldCode0(code);
        return sb.toString();
    }

    /**
     * 获取旧代码
     * @param code
     * @return {@link String }
     */

    private String getOldCode3(String code) {
        //生成一段随机字符串
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            sb.append((char) (Math.random() * 26 + 'a'));
        }
        getOldCode1(code);
        return sb.toString();

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