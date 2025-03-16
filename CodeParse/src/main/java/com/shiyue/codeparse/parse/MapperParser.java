package com.shiyue.codeparse.parse;

import com.shiyue.codeparse.parse.entity.MapperStructure;
import org.apache.ibatis.io.Resources;
import org.springframework.stereotype.Component;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @description:
 * @fileName: MapperParser
 * @author: wanghui
 * @createAt: 2025/03/13 02:58:39
 * @updateBy:
 * @copyright:
 */
@Component
public class MapperParser {
    public List<MapperStructure> getMapperSQL(String path) throws Exception {
        // 1. 读取 XML 文件
        String resource = "mybatis/ArticleMapper.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);

        // 2. 解析 XML
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        org.w3c.dom.Document document = builder.parse(inputStream);

        // 3. 解析 <sql> 片段
        Map<String, String> sqlFragments = new HashMap<>();
        NodeList sqlNodes = document.getElementsByTagName("sql");
        for (int i = 0; i < sqlNodes.getLength(); i++) {
            Node node = sqlNodes.item(i);
            String id = node.getAttributes().getNamedItem("id").getNodeValue();
            String sqlText = node.getTextContent().trim();
            sqlFragments.put(id, sqlText);
        }
        List<MapperStructure> result = new ArrayList<>();
        // 4. 解析 <select> 语句
        NodeList selectNodes = document.getElementsByTagName("select");
        for (int i = 0; i < selectNodes.getLength(); i++) {
            Element selectElement = (Element) selectNodes.item(i);
            String id = selectElement.getAttribute("id");
            String parsedSQL = parseSQL(selectElement, sqlFragments);
            result.add(new MapperStructure(parsedSQL, "select", id));
        }
        // 4. 解析 <select> 语句
        NodeList deleteNodes = document.getElementsByTagName("delete");
        for (int i = 0; i < deleteNodes.getLength(); i++) {
            Element selectElement = (Element) deleteNodes.item(i);
            String id = selectElement.getAttribute("id");
            String parsedSQL = parseSQL(selectElement, sqlFragments);
            result.add(new MapperStructure(parsedSQL, "delete", id));
        }
        return result;
    }

    /**
     * 递归解析 SQL 语句，手动拼接 SQL 代码
     */
    private static String parseSQL(Node node, Map<String, String> sqlFragments) {
        StringBuilder sqlBuilder = new StringBuilder();

        NodeList childNodes = node.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node child = childNodes.item(i);

            if (child.getNodeType() == Node.TEXT_NODE) {
                sqlBuilder.append(child.getTextContent().trim()).append(" ");
            } else if (child.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) child;
                String tagName = element.getTagName();

                if ("include".equals(tagName)) {
                    String refId = element.getAttribute("refid");
                    String includedSQL = sqlFragments.getOrDefault(refId, "[MISSING INCLUDE: " + refId + "]");
                    sqlBuilder.append(includedSQL).append(" ");
                }
//                else if ("where".equals(tagName)) {
//                    // 解析 <where> 标签，避免重复处理
//                    sqlBuilder.append(parseSQL(element, sqlFragments));
//                }
                else {
                    sqlBuilder.append(getNodeAsString(element));
                }
            }
        }
        return sqlBuilder.toString().trim();
    }
    /**
     * 这里把节点单独打出来，不做任何格式处理了
     */
    private static String getNodeAsString(Node node) {
        StringBuilder xmlString = new StringBuilder();
        xmlString.append("\n<").append(node.getNodeName());

        // 添加属性
        NamedNodeMap attributes = node.getAttributes();
        for (int j = 0; j < attributes.getLength(); j++) {
            Node attr = attributes.item(j);
            xmlString.append(" ").append(attr.getNodeName()).append("=\"").append(attr.getNodeValue()).append("\"");
        }
        xmlString.append(">");

        // 递归解析子节点
        NodeList childNodes = node.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node child = childNodes.item(i);
            if (child.getNodeType() == Node.TEXT_NODE) {
                xmlString.append(child.getTextContent().trim());
            } else {
                xmlString.append(getNodeAsString(child));
            }
        }

        xmlString.append("\n</").append(node.getNodeName()).append(">\n");
        return xmlString.toString();
    }
}