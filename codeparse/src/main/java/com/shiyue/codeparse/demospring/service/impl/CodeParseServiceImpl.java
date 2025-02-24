package com.shiyue.codeparse.demospring.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.shiyue.codeparse.demospring.entity.Param;
import com.shiyue.codeparse.demospring.feign.OrderFeign;
import com.shiyue.codeparse.demospring.service.CodeParseService;
import com.shiyue.codeparse.parse.builder.SpoonModelManage;
import com.shiyue.codeparse.parse.entity.FeignRequestInfo;
import com.shiyue.codeparse.parse.entity.MethodCallTree;
import com.shiyue.common.constants.FileConst;
import com.shiyue.common.utils.ClassUtils;
import com.shiyue.common.utils.PathUtils;
import com.shiyue.common.utils.SpringContextUtils;
import jakarta.annotation.Resource;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import spoon.reflect.CtModel;
import spoon.reflect.code.CtInvocation;
import spoon.reflect.code.CtNewClass;
import spoon.reflect.declaration.*;
import spoon.reflect.reference.CtExecutableReference;
import spoon.reflect.reference.CtTypeReference;

import java.io.FileNotFoundException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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
    PathUtils pathUtils;

    @Resource
    OrderFeign orderFeign;
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
        DemoInterface demoInterface = new DemoClass1();
        demoInterface.getName();
        orderFeign.queryByDictType(123L);
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
                                        //TODO: 处理多实现类接口（）
                                        List<? extends CtClass<?>> implClasses = model.getAllTypes().stream()
                                                .filter(t -> t instanceof CtClass<?>)
                                                .map(t -> (CtClass<?>) t)
                                                .filter(cls -> cls.getSuperInterfaces().stream()
                                                        .anyMatch(iface -> iface.getQualifiedName().equals(serviceClass.getQualifiedName())))
                                                .toList();
                                        if(implClasses.size() > 1){
                                            //多实现类处理
                                            //List<CtNewClass<?>> elements = model.getElements(e -> e instanceof CtNewClass<?> newClass && newClass.getType().getSuperInterfaces().contains(serviceClass.getReference());

                                        }
                                        if(true){
                                            //接口没有实现类，需要考虑其是否为Feign接口调用
                                            FeignClient annotation = serviceClass.getAnnotation(FeignClient.class);
                                            if(annotation != null){
                                                String serviceName = annotation.name();
                                                String path = annotation.path();
                                                for (CtMethod<?> m : serviceClass.getMethods()) {
                                                    if(m.getSimpleName().equals(calledMethod.getSimpleName())){
                                                        MethodCallTree feignMethod = new MethodCallTree(m, 1);
                                                        FeignRequestInfo feignRequestInfo = new FeignRequestInfo();
                                                        PostMapping methodAnnotation = m.getAnnotation(PostMapping.class);
                                                        feignRequestInfo.setFeignPath(PathUtils.join(path , methodAnnotation.value()[0]));
                                                        feignRequestInfo.setFeignMethod(m.getSimpleName());
                                                        feignRequestInfo.setServiceName(SpringContextUtils.getProperty(serviceName));
                                                        feignMethod.setFeignRequestInfo(feignRequestInfo);
                                                        methodCallTree.addCall(feignMethod);
                                                    }
                                                }
                                            }
                                        }
                                        for (CtClass<?> implClass : implClasses) {
                                            System.out.println("找到接口的实现类：" + implClass.getQualifiedName());
                                            for (CtMethod<?> implMethod : implClass.getMethods()) {
                                                if (implMethod.getSimpleName().equals(calledMethod.getSimpleName())) {
                                                    MethodCallTree methodCallTree1 = new MethodCallTree(implMethod,1);
                                                    MethodCallTree children = recursiveSpread(model, implMethod.getDeclaringType().getQualifiedName(), implMethod.getSimpleName());
                                                    methodCallTree1.setCalls(children.getCalls());
                                                    methodCallTree.addCall(methodCallTree1);
                                                }
                                            }
                                        }
                                    }else{
                                        for (CtMethod<?> serviceMethod : serviceClass.getMethods()) {
                                            if (serviceMethod.getSimpleName().equals(calledMethod.getSimpleName())) {
                                                MethodCallTree methodCallTree1 = new MethodCallTree(serviceMethod, 1);
                                                MethodCallTree children = recursiveSpread(model, serviceMethod.getDeclaringType().getQualifiedName(), serviceMethod.getSimpleName());
                                                methodCallTree1.setCalls(children.getCalls());
                                                methodCallTree.addCall(methodCallTree1);
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