package com.aizhixin.cloud.dataground.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Data
public class Config {
    @Value("${sys.version}")
    private String sysVersion;

    @Value("${ftp.host}")
    private String ftpHost;

    @Value("${ftp.port}")
    private Integer ftpPort;

    @Value("${ftp.work-dir}")
    private String ftpWorkDir;

    @Value("${ftp.username}")
    private String ftpUserName;

    @Value("${ftp.password}")
    private String ftpPassword;

    @Value("${ftp.down-dir}")
    private String ftpDownDir;

    @Value("${ftp.complete-dir}")
    private String ftpCompleteDir;

//    @Value("${ftp.up-dir}")
//    private String ftpUpDir;

//    @Value("${db.config.dir}")
//    private String dbConfigDir;

//    @Value("${db.out.dir}")
//    private String dbOutDir;

//    @Value("${db.batch.size}")
//    private int dbBatchSize = 10000;

}
