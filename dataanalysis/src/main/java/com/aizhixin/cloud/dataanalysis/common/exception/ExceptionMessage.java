package com.aizhixin.cloud.dataanalysis.common.exception;

import lombok.Data;

@Data
public class ExceptionMessage {
    private Integer code;
    private String cause;

    public ExceptionMessage() {}

    public ExceptionMessage(Integer code, String cause) {
        this.code = code;
        this.cause = cause;
    }
}