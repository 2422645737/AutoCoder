package com.shiyue.codeparse.demospring.service;

import com.shiyue.codeparse.demospring.entity.Param;
import com.shiyue.codeparse.parse.entity.MethodCallTree;

import java.io.FileNotFoundException;

public interface CodeParseService {
    /**
     * 解析代码
     * @param code
     * @return
     */
     String parse(String code);


    /**
     * @param param
     * @return
     */
     MethodCallTree getSourceCode(Param param) throws FileNotFoundException;
}