package com.shiyue.common.utils;

import java.io.File;

public class FileUtils {

    /**
     * 获取文件的顶层路径
     * @param file
     * @return {@link File }
     */
    public static File getProjectRoot(File file) {
        while (file != null) {
            if (file.getParentFile() == null) {
                return file;
            }
            file = file.getParentFile();
        }
        return null;
    }

}
