package com.shiyue.codeparse.demospring.controller;

import com.shiyue.codeparse.demospring.entity.Param;
import com.shiyue.codeparse.demospring.service.CodeParseService;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.Resource;
import spoon.Launcher;
import spoon.reflect.CtModel;
import spoon.reflect.code.CtInvocation;
import spoon.reflect.declaration.*;
import spoon.reflect.reference.CtExecutableReference;
import spoon.reflect.reference.CtTypeReference;

/**
 * @description:
 * @fileName: CodeParseController
 * @author: wanghui
 * @createAt: 2025/02/11 05:45:00
 * @updateBy:
 * @copyright:
 */

@RestController
@RequestMapping("/api")
public class CodeParseController {
    @Resource
    CodeParseService codeParseService;

    @PostMapping("/parse")
    public String parse(@RequestBody String code) {
        return codeParseService.parse(code);
    }


    @PostMapping("/getSourceCode")
    public String getSourceCode(@RequestBody Param param) {
        //这是一段注释
        System.out.println("getSourceCode");
        //写一段快速排序的代码
        int[] arr = new int[100];
        int n = arr.length;
        boolean swapped;
        for (int i = 0; i < n - 1; i++) {
            swapped = false;
            for (int j = 0; j < n - 1 - i; j++) {
                if (arr[j] > arr[j + 1]) {
                    int temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;
                    swapped = true;
                }
            }
            if (!swapped) {
                break;
            }
        }
        //return codeParseService.getSourceCode(param);
        Launcher launcher = new Launcher();
        launcher.addInputResource("codeparse/src/main/java");
        launcher.buildModel();

        CtModel model = launcher.getModel();

        String targetClassName = "com.shiyue.codeparse.demospring.controller.CodeParseController";
        String targetMethodName = "getSourceCode";

        // 遍历所有类，找到目标方法
        for (CtType<?> ctType : model.getAllTypes()) {
            if (ctType.getQualifiedName().equals(targetClassName) && ctType instanceof CtClass<?>) {
                CtClass<?> ctClass = (CtClass<?>) ctType;

                // 查找指定方法
                for (CtMethod<?> method : ctClass.getMethods()) {
                    if (method.getSimpleName().equals(targetMethodName)) {
                        System.out.println("Method Found: " + method.getSignature());
                        System.out.println("Source Code:\n" + method);

                        // 4️⃣ 过滤 Java 标准库方法，找到业务代码方法调用
                        method.getElements(e -> e instanceof CtInvocation).forEach(invocation -> {
                            CtInvocation<?> methodCall = (CtInvocation<?>) invocation;
                            CtExecutableReference<?> calledMethod = methodCall.getExecutable();
                            CtTypeReference<?> declaringType = calledMethod.getDeclaringType();

                            // 过滤掉Java标准库函数
                            if (declaringType != null && !declaringType.getQualifiedName().startsWith("java.")) {
                                //System.out.println("📌 业务方法调用: " + methodCall);

                                // 5️⃣ 查找 `codeParseService` 的 `getSourceCode` 方法实现
                                CtType<?> serviceClass = model.getAllTypes().stream()
                                        .filter(t -> t.getQualifiedName().equals(declaringType.getQualifiedName()))
                                        .findFirst()
                                        .orElse(null);

                                if (serviceClass != null) {
                                    for (CtMethod<?> serviceMethod : serviceClass.getMethods()) {
                                        if (serviceMethod.getSimpleName().equals(calledMethod.getSimpleName())) {
                                            System.out.println("✅ 找到 `codeParseService.getSourceCode()` 方法的实现:");
                                            System.out.println(serviceMethod);
                                        }
                                    }
                                }
                            }
                        });
                        break;
                    }
                }
            }
        }
        getOldCode(param.getPath());
        return codeParseService.getSourceCode(param);
    }

    /**
     * 获取旧代码
     * @param code
     * @return {@link String }
     */

    private String getOldCode(String code) {
        //生成一段随机字符串
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            sb.append((char) (Math.random() * 26 + 'a'));
        }
        return sb.toString();
    }
}