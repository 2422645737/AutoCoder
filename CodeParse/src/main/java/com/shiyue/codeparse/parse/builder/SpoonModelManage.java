package com.shiyue.codeparse.parse.builder;

import org.apache.commons.lang3.StringUtils;
import spoon.Launcher;
import spoon.reflect.CtModel;

/**
 * @description: model builder
 * @fileName: SpoonModelBuilder
 * @author: wanghui
 * @createAt: 2025/02/14 10:59:49
 * @updateBy:
 * @copyright:
 */

public class SpoonModelManage {
    private static volatile SpoonModelManage instance;
    private Launcher launcher;
    private SpoonModelManage() {

    }

    public static SpoonModelManage getInstance(String rootPath) {
        if (instance == null) {
            synchronized (SpoonModelManage.class) {
                if (instance == null) {
                    instance = new SpoonModelManage();
                    instance.launcher = new Launcher();
                    instance.launcher.addInputResource(rootPath);
                    instance.launcher.buildModel();
                }
            }
        }
        return instance;
    }


    public CtModel addSource(String path){
        if(StringUtils.isBlank(path)){
            return this.launcher.getModel();
        }
        this.launcher.addInputResource(path);
        this.launcher.buildModel();
        return this.launcher.getModel();
    }

    public CtModel getModel(){
        return this.launcher.getModel();
    }
}