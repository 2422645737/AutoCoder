package com.shiyue.codeparse.demospring.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * @description:
 * @fileName: Param
 * @author: wanghui
 * @createAt: 2025/02/11 05:55:58
 * @updateBy:
 * @copyright:
 */
@Data
public class Param {
    /**
     * 接口路径
     */
    @ApiModelProperty(value = "接口路径")
    private String path;
}