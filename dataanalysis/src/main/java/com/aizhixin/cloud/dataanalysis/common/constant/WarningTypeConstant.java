package com.aizhixin.cloud.dataanalysis.common.constant;

import lombok.Getter;

/**
 * @author: Created by jianwei.wu
 * @E-mail: wujianwei@aizhixin.com
 * @Date: 2017-09-11
 */
public enum WarningTypeConstant {
    Register("迎新报到预警"),
    LeaveSchool("退学预警"),
    AttendAbnormal("修读异常预警"),
    Absenteeism("旷课预警"),
    TotalAchievement("总评成绩预警"),
    SupplementAchievement("补考成绩预警"),
    PerformanceFluctuation("成绩波动预警"),
    Cet("CET-4成绩预警");

    private String value ;

    private WarningTypeConstant(String value){
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
