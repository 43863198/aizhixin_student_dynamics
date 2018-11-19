package com.aizhixin.cloud.datainstall.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Data
public class Config {

    private boolean isSchedule = true;

    @Value("${sys.version}")
    private String sysVersion;

    @Value("${ftp.host}")
    private String ftpHost;

    @Value("${ftp.port}")
    private Integer ftpPort;

    @Value("${ftp.username}")
    private String ftpUserName;

    @Value("${ftp.password}")
    private String ftpPassword;

    @Value("${ftp.config-file}")
    private String ftpConfigFile;

    @Value("${ftp.down-dir}")
    private String ftpDownDir;

    @Value("${ftp.up-dir}")
    private String ftpUpDir;

    @Value("${ftp.command-filename}")
    private String ftpCommandFileName;

    @Value("${db.config.filename}")
    private String dbConfigFileName;

    @Value("${db.config.dir}")
    private String dbConfigDir;

    @Value("${db.out.dir}")
    private String dbOutDir;

    @Value("${db.batch.size}")
    private int dbBatchSize = 10000;

}
