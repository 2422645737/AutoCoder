package com.shiyue.codeparse.parse.entity;

import lombok.Data;
import spoon.reflect.declaration.CtMethod;

import java.util.List;

/**
 * @description: 方法调用树
 * @fileName: MethodCallTree
 * @author: wanghui
 * @createAt: 2025/02/13 06:43:09
 * @updateBy:
 * @copyright:
 */
@Data
public class MethodCallTree {

    /**
     *
     */

    public MethodCallTree(){

    }

    /**
     * 通过CtMethod初始化参数
     * @param method
     */

    public MethodCallTree(CtMethod<?> method, int level){
        this.level = level;
        this.methodName = method.getSimpleName();
        this.methodDesc = method.getSignature();
        this.methodLocation = method.getPosition().getFile().getAbsolutePath();
        this.soureceCode = method.getBody().toString();
        this.recursive = false;
    }
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
    private String methodLocation;

    /**
     * 源代码
     */
    private String soureceCode;

    /**
     * 调用层级    最低级为0，表示根方法
     */
    private int level;

    /**
     * 调用方法
     */
    private List<MethodCallTree> calls;

    /**
     * 是否递归调用
     */
    private boolean recursive = false;
}