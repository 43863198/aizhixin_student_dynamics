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
    public static boolean validateDir(String dir) {
        File d = new File(dir);
        if (d.exists() && d.isDirectory() && d.canRead()) {
            return true;
        }
        return false;
    }

    /**
     * 验证并创建目录
     */
    public static boolean validateAndCreateDir(String dir) {
        File d = new File(dir);
        synchronized (d) {
            if (d.exists() && d.isDirectory()) {
                return true;
            }
            return d.mkdirs();
        }
    }



    /**
     *  在基础目录的基础的基础上附加日期（yyyyMMdd）目录，然后创建目录
     *  目录的分隔符必须是/
     */
    public static String createDateDirByBase(String baseDir) {
        StringBuilder d = new StringBuilder(baseDir);
        if (!baseDir.endsWith("/")) {
            d.append("/");
        }
        d.append(DateUtil.getCurrentDay_yyyyMMdd());
        if(!validateAndCreateDir(d.toString())) {
            log.warn("Create dir ({}) fail", d.toString());
        }
        return d.toString();
    }
}
