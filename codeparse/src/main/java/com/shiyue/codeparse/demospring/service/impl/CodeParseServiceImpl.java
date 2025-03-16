package com.shiyue.codeparse.demospring.service.impl;

import com.shiyue.codeparse.demospring.dao.ArticleMapper;
import com.shiyue.codeparse.demospring.entity.Param;
import com.shiyue.codeparse.demospring.entity.spring.po.ArticlePO;
import com.shiyue.codeparse.demospring.feign.OrderFeign;
import com.shiyue.codeparse.demospring.service.CodeParseService;
import com.shiyue.codeparse.parse.MapperParser;
import com.shiyue.codeparse.parse.builder.SpoonModelManage;
import com.shiyue.codeparse.parse.entity.FeignRequestMethod;
import com.shiyue.codeparse.parse.entity.MapperStructure;
import com.shiyue.codeparse.parse.entity.MethodCallTree;
import com.shiyue.codeparse.parse.resolver.MapperResolver;
import com.shiyue.common.constants.FileConst;
import com.shiyue.common.utils.ClassUtils;
import com.shiyue.common.utils.PathUtils;
import com.shiyue.common.utils.SpringContextUtils;
import jakarta.annotation.Resource;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import spoon.reflect.CtModel;
import spoon.reflect.code.CtInvocation;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtInterface;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.declaration.CtType;
import spoon.reflect.reference.CtExecutableReference;
import spoon.reflect.reference.CtTypeReference;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

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

    @Resource
    MapperParser mapperParser;

    @Resource
    ArticleMapper articleMapper;
    /**
     * 解析代码
     * @param code
     * @return {@link String }
     */

    @Override
    public String parse(String code) {
        //将code拆分为字符串数组
        System.out.println(code);
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
        orderFeign.queryByDictType(123L);
        List<ArticlePO> article = articleMapper.findArticle(Arrays.asList(100L, 200L, 300L));
        return getOldCode1(code);
    }

    /**
     * 解析源代码
     * @param param
     * @return {@link String }
     */

    @Override
    public MethodCallTree getSourceCode(Param param){
        Method targetMethod = pathUtils.getMethod(param.getPath());
        
        String pathOfRepositoryRoot = ClassUtils.getRepositoryRootName(targetMethod.getDeclaringClass());

        System.out.println("开始解析源代码");
        long current = System.currentTimeMillis();
        CtModel model = SpoonModelManage.getInstance(pathOfRepositoryRoot + "/" + FileConst.SRC_PATH).getModel();
        System.out.println("解析完成，耗时:" + (System.currentTimeMillis() - current) + "毫秒");
        MethodCallTree methodCallTree = recursiveSpread(model, targetMethod.getDeclaringClass().getName(),targetMethod.getName());

        System.out.println("解析完毕，耗时：" + (System.currentTimeMillis() - current) + "毫秒");
        return methodCallTree;
    }

    @Override
    public List<MapperStructure> testXML() throws Exception{
        List<MapperStructure> mapperSQL = mapperParser.getMapperSQL("mapper/ArticleMapper.xml");
        return mapperSQL;
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
                                                        FeignRequestMethod feignRequestMethod = new FeignRequestMethod();
                                                        PostMapping methodAnnotation = m.getAnnotation(PostMapping.class);
                                                        feignRequestMethod.setFeignPath(PathUtils.join(path , methodAnnotation.value()[0]));
                                                        feignRequestMethod.setFeignMethod(m.getSimpleName());
                                                        feignRequestMethod.setServiceName(SpringContextUtils.getProperty(serviceName));
                                                        feignMethod.setFeignRequestMethod(feignRequestMethod);
                                                        methodCallTree.addCall(feignMethod);
                                                        return;
                                                    }
                                                }
                                            }
                                            //考虑是否为Mapper方法，此时需要解析
                                            Mapper mapperAnnotation = serviceClass.getAnnotation(Mapper.class);
                                            if(mapperAnnotation != null){
                                                String mapperPath = MapperResolver.getMapperPath(serviceClass.getQualifiedName());
                                                if(mapperPath == null){
                                                    return;
                                                }
                                                try {
                                                    List<MapperStructure> mapperSQL = mapperParser.getMapperSQL(mapperPath);
                                                    MapperStructure mapperStructure = mapperSQL.stream().filter(e -> e.getSqlId().equals(calledMethod.getSimpleName())).findFirst().orElse(null);
                                                    if(mapperStructure != null){
                                                        for (CtMethod<?> m : serviceClass.getMethods()) {
                                                            if(m.getSimpleName().equals(calledMethod.getSimpleName())){
                                                                MethodCallTree mapperMethod = new MethodCallTree(m, 1);
                                                                mapperMethod.setMapperStructure(mapperStructure);
                                                                methodCallTree.addCall(mapperMethod);
                                                            }
                                                        }
                                                    }
                                                } catch (Exception e) {
                                                    throw new RuntimeException(e);
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
        int[] arr = new Random().ints(10, 1, 100).toArray();

        // 2. 排序数组
        boolean swapped; // 优化标记：若本轮未交换则已有序
        for (int i = 0; i < arr.length - 1; i++) {
            swapped = false;
            // 每轮遍历后，最大值会"冒泡"到末尾，因此内层循环可减少一次比较
            for (int j = 0; j < arr.length - 1 - i; j++) {
                if (arr[j] > arr[j + 1]) { // 逆序则交换
                    // 使用异或交换（避免临时变量）
                    arr[j] = arr[j] ^ arr[j + 1];
                    arr[j + 1] = arr[j] ^ arr[j + 1];
                    //arr[j] = arr[j] ^ arr[j + 1];
                    swapped = true;
                }
            }
            if (!swapped) break; // 提前终止排序
        }

        // 3. 转换为字符串
        String result = Arrays.toString(arr);
        return result.toString();
    }

}