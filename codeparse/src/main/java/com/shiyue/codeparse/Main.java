package com.shiyue.codeparse;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.*;

/**
 * @description:
 * @fileName: Main
 * @author: wanghui
 * @createAt: 2025/02/10 09:28:03
 * @updateBy:
 * @copyright:
 */

public class Main {
    private static Map<String, MethodDeclaration> methodMap = new HashMap<>();
    public static void main(String[] args) throws FileNotFoundException {
        Soulution soulution = new Soulution();
        int[] res = soulution.twoSum(new int[]{2, 7, 11, 15}, 9);
        // 指定 Java 文件路径
        File file = new File("CodeParse/src/main/java/com/shiyue/codeparse/Main.java");
        System.out.println(file.getAbsolutePath());
        FileInputStream in = new FileInputStream(file);

        // 解析 Java 源码
        CompilationUnit cu = new JavaParser().parse(in).getResult().orElseThrow();
        // 构建方法映射表（methodMap）以便快速查找
        cu.findAll(MethodDeclaration.class).forEach(m -> methodMap.put(m.getNameAsString(), m));

        // 目标方法名
        String targetMethodName = "twoSum";
        // 展开并打印方法代码
        Optional<MethodDeclaration> targetMethodOpt = Optional.ofNullable(methodMap.get(targetMethodName));
        targetMethodOpt.ifPresent(method -> {
            System.out.println("展开后的代码:");
            System.out.println(inlineMethod(method, ""));
        });
    }
    // 递归展开方法调用
    private static String inlineMethod(MethodDeclaration method, String indent) {
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
                        result.append(inlineMethod(methodMap.get(calledMethodName), indent + "    "));
                    } else {
                        // 如果方法没找到，直接打印调用语句
                        result.append(indent).append("    ").append(statement).append("\n");
                    }
                } else {
                    // 直接打印普通语句
                    result.append(indent).append("    ").append(statement).append("\n");
                }
            });
        });
        return result.toString();
    }
}
class Soulution {
    public int[] twoSum(int[] nums, int target) {
        int[] res = new int[2];
        for (int i = 0; i < nums.length; i++) {
            for (int j = i + 1; j < nums.length; j++) {
                if (nums[i] + nums[j] == target) {
                    res[0] = i;
                    res[1] = j;
                    return res;
                }
           }
        }
        TestClass testClass = new TestClass();
        testClass.getLength("hellllllll");
        twoSum2(nums,target);
        return res;
    }

    public int[] twoSum2(int[] nums, int target) {
        System.out.println("hello world");
        System.out.println("hello world");
        System.out.println("hello world");

        System.out.println("hello world");
        return new int[]{1, 2};
    }
}