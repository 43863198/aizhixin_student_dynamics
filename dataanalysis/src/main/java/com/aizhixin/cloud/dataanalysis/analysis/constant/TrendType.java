package com.aizhixin.cloud.dataanalysis.analysis.constant;

/**
 * @author: Created by jianwei.wu
 * @E-mail: wujianwei@aizhixin.com
 * @Date: 2017-11-30
 */
public enum TrendType {
    NEW_STUDENTS_COUNT("录取数"),
    ALREADY_REPORT("报到数"),
    ALREADY_PAY("缴费人数"),
    CONVENIENCE_CHANNEL("绿色通道人数"),
    UNREPORT("未报到数"),
    PAY_PROPORTION("缴费人占比"),
    REPORT_PROPORTION("报到率"),
    CHANNEL_PROPORTION("绿色通道人数占比");

    private String value ;

    private TrendType(String value){
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}

