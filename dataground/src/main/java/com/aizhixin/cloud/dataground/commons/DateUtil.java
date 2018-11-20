package com.aizhixin.cloud.dataground.commons;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
    private static SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");

    /**
     * 获取当前时间对应的字符串日期(格式:yyyyMMdd)
     */
    public static String getCurrentDay_yyyyMMdd() {
        return format.format(new Date());
    }
}
