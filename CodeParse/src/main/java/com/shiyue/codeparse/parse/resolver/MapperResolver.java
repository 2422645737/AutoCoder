package com.shiyue.codeparse.parse.resolver;

import com.shiyue.common.utils.PathUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

/**
 * @description: 构建Mapper的namespace映射
 * @fileName: MapperResolver
 * @author: wanghui
 * @createAt: 2025/03/16 04:11:39
 * @updateBy:
 * @copyright:
 */

public class MapperResolver {
    private static final List<String> basePath = Arrays.asList("/mybatis");
    /**
     * namespace --> mapper path
     */
    private static Map<String,String> mapperNamespace = new HashMap<>();
    static{
        basePath.forEach(p -> {
            String xmlPath = ClassLoader.getSystemResource("mybatis").getFile();
            xmlPath = PathUtils.removeFirstSlash(xmlPath);
            try (Stream<Path> paths = Files.walk(Paths.get(xmlPath))) {
                paths.filter(Files::isRegularFile)
                        .filter(path -> path.toString().endsWith(".xml"))
                        .forEach(path -> {
                            String namespace = extractNamespace(path.toFile());
                            if (namespace != null) {
                                mapperNamespace.put(namespace, path.toAbsolutePath().toString());
                            }
                        });
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

    }
    /**
     * 从 XML 文件中提取 namespace
     */
    private static String extractNamespace(File xmlFile) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(xmlFile);

            Element root = document.getDocumentElement();
            if ("mapper".equals(root.getNodeName())) {
                return root.getAttribute("namespace");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取Mapper地址
     * @param namespace
     * @return {@link String }
     */

    public static String getMapperPath(String namespace){
        return mapperNamespace.get(namespace);
    }
}