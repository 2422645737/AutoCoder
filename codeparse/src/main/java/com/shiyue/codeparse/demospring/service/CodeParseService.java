package com.shiyue.codeparse.demospring.service;

import com.shiyue.codeparse.demospring.entity.Param;
import com.shiyue.codeparse.parse.entity.MapperStructure;
import com.shiyue.codeparse.parse.entity.MethodCallTree;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

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

    /**
     * @return {@link String }
     */

    List<MapperStructure> testXML() throws Exception;
}