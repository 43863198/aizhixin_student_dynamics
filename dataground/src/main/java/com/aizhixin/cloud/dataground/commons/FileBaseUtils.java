package com.aizhixin.cloud.dataground.commons;

import lombok.extern.slf4j.Slf4j;

import java.io.File;

/**
 * 文件及目录操作工具类
 */
@Slf4j
public class FileBaseUtils {

    /**
     * 验证目录是否存在并且可读
     * 返回false表示不存在或者不可读
     */
    public static boolean validateAndCreateDir(String dir) {
        File d = new File(dir);
        synchronized (d) {
            if (d.exists() && d.isDirectory() && d.canRead()) {
                return true;
            }
        }
        return false;
    }
}
