package com.shiyue.codeparse.parse.entity;

import lombok.Data;

/**
 * @description:
 * @fileName: MapperStructure
 * @author: wanghui
 * @createAt: 2025/03/13 03:02:14
 * @updateBy:
 * @copyright:
 */
@Data
public class MapperStructure {
    private String sqlBody;

    private String sqlType;

    private String sqlId;

    public MapperStructure(String sqlBody, String sqlType, String sqlId) {
        this.sqlBody = sqlBody;
        this.sqlType = sqlType;
        this.sqlId = sqlId;
    }
    public MapperStructure() {
    }
}