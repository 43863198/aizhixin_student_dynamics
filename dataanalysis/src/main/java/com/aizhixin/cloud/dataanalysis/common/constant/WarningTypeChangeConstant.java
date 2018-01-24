package com.aizhixin.cloud.dataanalysis.common.constant;

import com.aizhixin.cloud.dataanalysis.analysis.constant.DataType;

public enum WarningTypeChangeConstant {
    Register("迎新报到预警",1),
    LeaveSchool("退学预警",2),
    AttendAbnormal("修读异常预警",3),
    Absenteeism("旷课预警",4),
    TotalAchievement("总评成绩预警",5),
    SupplementAchievement("补考成绩预警",6),
    PerformanceFluctuation("成绩波动预警",7),
    Cet("CET-4成绩预警",8);

    // 成员变量
    private String name;
    private int index;

    // 构造方法
    private WarningTypeChangeConstant(String name, int index) {
        this.name = name;
        this.index = index;
    }

    // 获取类型名称方法
    public static String getName(int index) {
        for (WarningTypeChangeConstant c : WarningTypeChangeConstant.values()) {
            if (c.getIndex() == index) {
                return c.name;
            }
        }
        return null;
    }

    // 获取类型方法
    public static String getType(int index) {
        for (DataType c : DataType.values()) {
            if (c.getIndex() == index) {
                return c.toString();
            }
        }
        return null;
    }

    // get set 方法
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
