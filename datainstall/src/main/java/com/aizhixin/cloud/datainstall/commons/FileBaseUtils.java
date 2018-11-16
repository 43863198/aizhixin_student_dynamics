package com.aizhixin.cloud.datainstall.commons;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 文件及目录操作工具类
 */
@Slf4j
public class FileBaseUtils {

    /**
     * 验证目录是否存在，如果不存在，创建他。
     * 返回false表示不存在并且创建失败
     */
    public static boolean validateAndCreateDir(String dir) {
        File d = new File(dir);
        synchronized (d) {
            if (d.exists() && d.isDirectory()) {
                return true;
            }
        }
        return d.mkdirs();
    }

    /**
     *  在基础目录的基础的基础上附加日期（yyyyMMdd）目录，然后创建目录
     *  目录的分隔符必须是/
     */
    public static String createDateDirByBase(String baseDir) {
        SimpleDateFormat f = new SimpleDateFormat("yyyyMMdd");
        StringBuilder d = new StringBuilder(baseDir);
        if (!baseDir.endsWith("/")) {
            d.append("/");
        }
        d.append(f.format(new Date()));
        if(!validateAndCreateDir(d.toString())) {
            log.warn("Create dir ({}) fail", d.toString());
        }
        return d.toString();
    }

    /**
     * 获取目录下的所有文件数据
     */
    public static List<File> getDirFileNames(String dir) {
        List<File> list = new ArrayList<>();
        File d = new File(dir);
        if (d.isDirectory() && d.canRead()) {
            File[] fs = d.listFiles();
            for (File f : fs) {
                list.add(f);
            }
        }
        return list;
    }
}
