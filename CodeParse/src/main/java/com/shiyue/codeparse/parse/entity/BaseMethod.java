package com.shiyue.codeparse.parse.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @description:
 * @fileName: BaseMethod
 * @author: wanghui
 * @createAt: 2025/03/16 03:58:37
 * @updateBy:
 * @copyright:
 */
@Data
public class BaseMethod implements Serializable {
    /**
     * 方法名
     */
    private String methodName;

    /**
     * 方法描述
     */
    private String methodDesc;


    /**
     * 方法位置
     */
    private String methodFullPath;

    /**
     * 方法位置(绝对路径)
     */
    private String methodLocation;

    /**
     * 服务名
     */
    private String serviceName;

}