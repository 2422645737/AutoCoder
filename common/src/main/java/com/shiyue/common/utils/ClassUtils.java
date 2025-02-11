package com.shiyue.common.utils;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.shiyue.common.constants.FileConst;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.lang.reflect.Method;
import java.util.Optional;

public class ClassUtils {
    /**
     * 获取包路径
     * @param clazz
     * @return {@link String }
     */
    public static String getPackagePath(Class<?> clazz) {
        String path = clazz.getPackageName().replace(".", "/");
        return path + "/" + clazz.getSimpleName() + ".java";
    }

    /**
     * 获取项目相对路径
     * @param clazz
     * @return {@link String }
     */
    public static String getPathOfRepositoryRoot(Class<?> clazz) {
        String packagePath = getPackagePath(clazz);
        packagePath = FileConst.SRC_PATH + packagePath;
        File srcPath = FileUtils.getProjectRoot(new File(packagePath));
        String repositoryPath = srcPath.getAbsoluteFile().getParentFile().getName();
        return repositoryPath + "/" + packagePath;
    }

}