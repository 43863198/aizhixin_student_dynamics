package com.aizhixin.cloud.datainstall.ftp.dto;

import lombok.Data;

import java.util.Date;

@Data
public class UploadRetry {
    private Integer count;
    private Date nextTime;
    private String dirName;
    private String fileName;
}
