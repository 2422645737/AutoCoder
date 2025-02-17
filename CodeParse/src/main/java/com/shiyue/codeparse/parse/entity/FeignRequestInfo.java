package com.shiyue.codeparse.parse.entity;

import lombok.Data;

/**
 * @description:
 * @fileName: FeignRequestInfo
 * @author: wanghui
 * @createAt: 2025/02/17 04:20:02
 * @updateBy:
 * @copyright:
 */
@Data
public class FeignRequestInfo {
    private String feignPath;
    private String feignMethod;
    private String feignMethodDesc;
    private String serviceName;
    private String requestParam;
}