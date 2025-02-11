package com.shiyue.codeparse.demospring.controller;

import com.shiyue.codeparse.demospring.entity.Param;
import com.shiyue.codeparse.demospring.service.CodeParseService;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.Resource;

/**
 * @description:
 * @fileName: CodeParseController
 * @author: wanghui
 * @createAt: 2025/02/11 05:45:00
 * @updateBy:
 * @copyright:
 */

@RestController
@RequestMapping("/api")
public class CodeParseController {
    @Resource
    CodeParseService codeParseService;

    @PostMapping("/parse")
    public String parse(@RequestBody String code) {
        return codeParseService.parse(code);
    }


    @PostMapping("/getSourceCode")
    public String getSourceCode(@RequestBody Param param) {
        return codeParseService.getSourceCode(param);
    }
}