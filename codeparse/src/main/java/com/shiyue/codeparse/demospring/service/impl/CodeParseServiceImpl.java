package com.shiyue.codeparse.demospring.service.impl;

import com.shiyue.codeparse.demospring.entity.Param;
import com.shiyue.codeparse.demospring.service.CodeParseService;
import com.shiyue.codeparse.parse.JavaMethodParser;
import com.shiyue.codeparse.parse.builder.SpoonModelManage;
import com.shiyue.codeparse.parse.entity.MethodCallTree;
import com.shiyue.common.constants.FileConst;
import com.shiyue.common.utils.ClassUtils;
import com.shiyue.common.utils.PathUtils;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import spoon.reflect.CtModel;
import spoon.reflect.code.CtInvocation;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtInterface;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.declaration.CtType;
import spoon.reflect.reference.CtExecutableReference;
import spoon.reflect.reference.CtTypeReference;

import java.io.FileNotFoundException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * @description:
 * @fileName: CodeParseServiceImpl
 * @author: wanghui
 * @createAt: 2025/02/11 05:27:31
 * @updateBy:
 * @copyright:
 */

@Service
public class CodeParseServiceImpl implements CodeParseService {

    @Resource
    JavaMethodParser javaMethodParser;

    @Resource
    PathUtils pathUtils;

    /**
     * 解析代码
     * @param code
     * @return {@link String }
     */

    @Override
    public String parse(String code) {
        //将code拆分为字符串数组
        System.out.println(code);
        //深度优先搜索算法
        for (int i = 0; i < code.length(); i++) {
            //如果当前字符是字母或者数字，则继续遍历
            if (Character.isLetterOrDigit(code.charAt(i))) {
                //如果当前字符是字母，则将当前字符和下一个字符拼接起来，如果拼接后的字符在字典中存在，则将其替换为对应的中文字符
                if (Character.isLetter(code.charAt(i))) {
                    StringBuilder sb = new StringBuilder();
                   sb.append(code.charAt(i));
                }
            }
        }
        getOldCode1(code);
        getOldCode2(code);
        getOldCode3(code);
        getOldCode0(code);
        return getOldCode1(code);
    }

    /**
     * 解析源代码
     * @param param
     * @return {@link String }
     */

    @Override
    public MethodCallTree getSourceCode(Param param) throws FileNotFoundException {
        Method targetMethod = pathUtils.getMethod(param.getPath());
        
        String pathOfRepositoryRoot = ClassUtils.getRepositoryRootName(targetMethod.getDeclaringClass());

        System.out.println("开始解析源代码");
        long current = System.currentTimeMillis();
        CtModel model = SpoonModelManage.getInstance(pathOfRepositoryRoot + "/" + FileConst.SRC_PATH).getModel();
        System.out.println("解析完成，耗时:" + (System.currentTimeMillis() - current) + "毫秒");
        MethodCallTree methodCallTree = recursiveSpread(model, targetMethod.getDeclaringClass().getName(),targetMethod.getName());

        System.out.println("解析完毕，耗时：" + (System.currentTimeMillis() - current) + "毫秒");
        getOldCode1(param.getPath());
        getOldCode2(param.getPath());
        getOldCode3(param.getPath());
        getOldCode0(param.getPath());
        return methodCallTree;
    }

    private MethodCallTree recursiveSpread(CtModel model,String targetClassName,String targetMethodName){

        MethodCallTree methodCallTree = new MethodCallTree();
        for (CtType<?> ctType : model.getAllTypes()) {
            if (ctType.getQualifiedName().equals(targetClassName) && ctType instanceof CtClass<?> ctClass) {
                // 查找指定方法
                for (CtMethod<?> method : ctClass.getMethods()) {
                    if (method.getSimpleName().equals(targetMethodName)) {
                        System.out.println("定位到起点方法: " + method.getSignature());
                        MethodCallTree.of(method,methodCallTree);
                        methodCallTree.setCalls(new ArrayList<>());
                        // 过滤 Java 标准库方法，找到业务代码方法调用
                        method.getElements(e -> e instanceof CtInvocation).forEach(invocation -> {
                            CtInvocation<?> methodCall = (CtInvocation<?>) invocation;
                            CtExecutableReference<?> calledMethod = methodCall.getExecutable();
                            CtTypeReference<?> declaringType = calledMethod.getDeclaringType();

                            // 过滤掉Java标准库函数以及无关的业务调用，这里将来需要写一个过滤器，仅仅保留业务级别的方法调用
                            if (declaringType != null && declaringType.getQualifiedName().startsWith("com.shiyue")) {

                                CtType<?> serviceClass = model.getAllTypes().stream()
                                        .filter(t -> t.getQualifiedName().equals(declaringType.getQualifiedName()))
                                        .findFirst()
                                        .orElse(null);

                                if (serviceClass != null) {
                                    if(serviceClass instanceof CtInterface<?>){
                                        System.out.println("发现接口调用，开始查找其实现类");
                                        List<? extends CtClass<?>> implClasses = model.getAllTypes().stream()
                                                .filter(t -> t instanceof CtClass<?>)
                                                .map(t -> (CtClass<?>) t)
                                                .filter(cls -> cls.getSuperInterfaces().stream()
                                                        .anyMatch(iface -> iface.getQualifiedName().equals(serviceClass.getQualifiedName())))
                                                .toList();

                                        for (CtClass<?> implClass : implClasses) {
                                            System.out.println("找到接口的实现类：" + implClass.getQualifiedName());
                                            for (CtMethod<?> implMethod : implClass.getMethods()) {
                                                if (implMethod.getSimpleName().equals(calledMethod.getSimpleName())) {
                                                    MethodCallTree methodCallTree1 = new MethodCallTree(implMethod,1);
                                                    MethodCallTree children = recursiveSpread(model, implMethod.getDeclaringType().getQualifiedName(), implMethod.getSimpleName());
                                                    methodCallTree1.setCalls(children.getCalls());
                                                    methodCallTree.getCalls().add(methodCallTree1);
                                                }
                                            }
                                        }
                                    }else{
                                        for (CtMethod<?> serviceMethod : serviceClass.getMethods()) {
                                            if (serviceMethod.getSimpleName().equals(calledMethod.getSimpleName())) {
                                                MethodCallTree methodCallTree1 = new MethodCallTree(serviceMethod, 1);
                                                MethodCallTree children = recursiveSpread(model, serviceMethod.getDeclaringType().getQualifiedName(), serviceMethod.getSimpleName());
                                                methodCallTree1.setCalls(children.getCalls());
                                                methodCallTree.getCalls().add(methodCallTree1);
                                                System.out.println(serviceMethod);
                                            }
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
        return methodCallTree;
    }
    /**
     * 获取旧代码
     * @param code
     * @return {@link String }
     */

    private String getOldCode1(String code) {
        //生成一段随机字符串
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            sb.append((char) (Math.random() * 26 + 'a'));
        }
        return sb.toString();
    }

    /**
     * 获取旧代码
     * @param code
     * @return {@link String }
     */

    private String getOldCode0(String code) {
        //生成一段随机字符串
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            sb.append((char) (Math.random() * 26 + 'a'));
        }
        getOldCode1(code);
        return sb.toString();
    }

    /**
     * 获取旧代码
     * @param code
     * @return {@link String }
     */

    private String getOldCode2(String code) {
        //生成一段随机字符串
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            sb.append((char) (Math.random() * 26 + 'a'));
        }
        getOldCode0(code);
        return sb.toString();
    }

    /**
     * 获取旧代码
     * @param code
     * @return {@link String }
     */

    private String getOldCode3(String code) {
        //生成一段随机字符串
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            sb.append((char) (Math.random() * 26 + 'a'));
        }
        getOldCode1(code);
        return sb.toString();

    }
}