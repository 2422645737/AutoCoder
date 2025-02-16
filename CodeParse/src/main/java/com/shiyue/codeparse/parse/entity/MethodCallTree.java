package com.shiyue.codeparse.parse.entity;

import com.shiyue.common.utils.SpringContextUtils;
import lombok.Builder;
import lombok.Data;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import spoon.reflect.code.CtComment;
import spoon.reflect.declaration.CtMethod;

import java.lang.reflect.Method;
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

    /**
     * 源代码
     */
    private String sourceCode;

    /**
     * 调用层级    最低级为0，表示根方法
     */
    private int level;

    /**
     * 调用顺序
     */
    private int order;

    /**
     * 调用方法
     */
    private List<MethodCallTree> calls;

    /**
     * 是否递归调用
     */
    private boolean recursive = false;


    public MethodCallTree(){

    }

    /**
     * 通过CtMethod初始化参数
     * @param method
     */

    public MethodCallTree(CtMethod<?> method, int level){
        this.level = level;
        this.methodName = method.getSimpleName();
        this.methodDesc = method.getComments().stream().filter(c -> c.getCommentType() == CtComment.CommentType.JAVADOC).map(CtComment::getContent).findFirst().orElse("该接口暂无注释");
        this.methodLocation = method.getPosition().getFile().getAbsolutePath();
        this.sourceCode = method.getBody().toString();
        this.methodFullPath = method.getDeclaringType().getQualifiedName() + "#" + method.getSimpleName();
        this.recursive = false;
        this.serviceName = SpringContextUtils.getProperty("spring.application.name");
    }

    /**
     * 通过CtMethod初始化参数
     * @param method
     */

    public static void of(CtMethod<?> method, MethodCallTree methodCallTree){
        methodCallTree.methodName = method.getSimpleName();
        methodCallTree.methodDesc = method.getComments().stream().filter(c -> c.getCommentType() == CtComment.CommentType.JAVADOC).map(CtComment::getContent).findFirst().orElse("该接口暂无注释");
        methodCallTree.methodLocation = method.getPosition().getFile().getAbsolutePath();
        methodCallTree.sourceCode = method.getBody().toString();
        methodCallTree.methodFullPath = method.getDeclaringType().getQualifiedName() + "#" + method.getSimpleName();
        methodCallTree.recursive = false;
        methodCallTree.serviceName = SpringContextUtils.getProperty("spring.application.name");
    }
}