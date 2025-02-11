package com.shiyue.codeparse.demospring.service;

import com.shiyue.codeparse.demospring.entity.Param;

public interface CodeParseService {
    /**
     * 解析代码
     * @param code
     * @return
     */
    public String parse(String code);


    /**
     * 获取源代码
     * @param param
     * @return
     */
    public String getSourceCode(Param param);
}