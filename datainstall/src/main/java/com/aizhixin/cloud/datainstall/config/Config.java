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

    @Value("${ftp.remote-dir}")
    private String ftpRemoteDir;

    @Value("${ftp.local-dir}")
    private String ftpLocalDir;

    public String getFtpConfigFile() {
        return ftpLocalDir + "/config.txt";
    }

    public String getFtpDownDir() {
        return ftpLocalDir + "/ftpdown";
    }

    public String getFtpUpDir() {
        return ftpLocalDir +"/ftpup";
    }

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

    @Value("${log-dir}")
    private String logDir;

}
