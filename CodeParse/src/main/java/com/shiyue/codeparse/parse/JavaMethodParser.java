package com.shiyue.codeparse.parse;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.shiyue.common.utils.ClassUtils;
import com.shiyue.common.utils.PathUtils;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.lang.reflect.Method;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @description:
 * @fileName: JavaMethodParser
 * @author: wanghui
 * @createAt: 2025/03/13 02:54:24
 * @updateBy:
 * @copyright:
 */

@Component
public class JavaMethodParser {

    @Resource
    private PathUtils pathUtils;

    /**
     * 获取源码
     * @param url
     * @return {@link String }
     * @throws FileNotFoundException
     */
    public String getSourceCode(String url) throws FileNotFoundException {
        //url --> method
        Method method = pathUtils.getMethod(url);

        String pathOfRepositoryRoot = ClassUtils.getPathOfRepositoryRoot(method.getDeclaringClass());

        return this.getSourceCode(pathOfRepositoryRoot,method);
    }

    /**
     * 获取源码
     * @param sourceCodePath
     * @param method
     * @return {@link String }
     */
    public String getSourceCode(String sourceCodePath, Method method) throws FileNotFoundException {

        File file = new File(sourceCodePath);
        FileInputStream in = new FileInputStream(file);

        Map<String, MethodDeclaration> methodMap = new HashMap<>();
        // 解析 Java 源码
        CompilationUnit cu = new JavaParser().parse(in).getResult().orElseThrow();
        // 构建方法映射表（methodMap）以便快速查找
        cu.findAll(MethodDeclaration.class).forEach(m -> methodMap.put(m.getNameAsString(), m));

        // 目标方法名
        String targetMethodName = method.getName();
        // 展开并打印方法代码
        Optional<MethodDeclaration> targetMethodOpt = Optional.ofNullable(methodMap.get(targetMethodName));

        StringBuffer result = new StringBuffer();
        targetMethodOpt.ifPresent(m -> {
            System.out.println("展开后的代码:");
            result.append(inlineMethod(m,"",methodMap));
        });
        return result.toString();
    }
    // 递归展开方法调用
    private static String inlineMethod(MethodDeclaration method, String indent,Map<String,MethodDeclaration> methodMap) {
        StringBuilder result = new StringBuilder();
        result.append(indent).append("Method: ").append(method.getNameAsString()).append("\n");
        method.getBody().ifPresent(body -> {
            body.getStatements().forEach(statement -> {
                if (statement.isExpressionStmt() && statement.asExpressionStmt().getExpression().isMethodCallExpr()) {
                    // 发现方法调用
                    MethodCallExpr methodCall = statement.asExpressionStmt().getExpression().asMethodCallExpr();
                    String calledMethodName = methodCall.getNameAsString();
                    if (methodMap.containsKey(calledMethodName)) {
                        // 递归展开方法体
                        result.append(inlineMethod(methodMap.get(calledMethodName), indent + "    ",methodMap));
                    } else {
                        // 如果方法没找到，直接打印调用语句
                        result.append(indent).append("    ").append(statement).append("\n");
                    }
                }else if(statement.isReturnStmt() && statement.asReturnStmt().getExpression().isPresent()){
                    // 发现 return 语句中的方法调用
                    MethodCallExpr returnMethodCall = statement.asReturnStmt().getExpression().get().asMethodCallExpr();
                    String returnMethodName = returnMethodCall.getNameAsString();
                    if (methodMap.containsKey(returnMethodName)) {
                        // 递归展开返回值的方法
                        result.append(inlineMethod(methodMap.get(returnMethodName), indent + "    ", methodMap));
                    } else {
                        // 方法未找到，直接打印 return 语句
                        result.append(indent).append("    ").append(statement).append("\n");
                    }
                }
                else {
                    // 直接打印普通语句
                    result.append(indent).append("    ").append(statement).append("\n");
                }
            });
        });
        return result.toString();
    }
}