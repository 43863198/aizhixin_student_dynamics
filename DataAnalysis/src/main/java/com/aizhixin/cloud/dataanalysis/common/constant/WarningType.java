package com.aizhixin.cloud.dataanalysis.common.constant;

import lombok.Getter;

/**
 * @author: Created by jianwei.wu
 * @E-mail: wujianwei@aizhixin.com
 * @Date: 2017-09-11
 */
public enum WarningType {
    Register("报道注册预警"),
    Academic("学业预警");

    private String value ;

    private WarningType(String value){
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
