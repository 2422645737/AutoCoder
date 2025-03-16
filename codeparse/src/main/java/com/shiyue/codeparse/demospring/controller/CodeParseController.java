package com.shiyue.codeparse.demospring.controller;

import com.shiyue.codeparse.demospring.entity.Param;
import com.shiyue.codeparse.demospring.service.CodeParseService;
import com.shiyue.codeparse.parse.entity.MapperStructure;
import com.shiyue.codeparse.parse.entity.MethodCallTree;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

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

    @PostMapping("/testXML")
    public List<MapperStructure> testXML() throws Exception{
        return codeParseService.testXML();
    }


    /**
     * @param param
     * @return {@link MethodCallTree }
     */

    @PostMapping("/getSourceCode")
    public MethodCallTree getSourceCode(@RequestBody Param param) throws FileNotFoundException {
        return codeParseService.getSourceCode(param);
    }

    /**
     * 这是一个测试接口
     * @param param
     * @return {@link String }
     */

    @PostMapping("/getSourceCode1")
    public String getSourceCode1(@RequestBody Param param) throws FileNotFoundException {
        return codeParseService.parse(param.getPath());
    }

}