package com.aizhixin.cloud.datainstall.ftp.service;

import com.aizhixin.cloud.datainstall.config.Config;
import com.aizhixin.cloud.datainstall.ftp.utils.ZXFTPClient;
import com.aizhixin.cloud.datainstall.ftp.utils.ZipUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.utils.CharsetNames;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Date;

@Service
@Slf4j
public class FTPService {
    @Autowired
    private ZXFTPClient zxftpClient;
    @Autowired
    private Config config;

    public void uploadSyncFile() throws Exception {
        //默认文件夹
        String fileName = DateFormatUtils.format(new Date(), "yyyyMMdd");
        File file = new File(config.getDbOutDir(), fileName);
        uploadSyncFile(file.getAbsolutePath(), "data" + fileName);
    }

    public void uploadSyncFile(String dirName, String fileName) throws Exception {
        uploadFile(dirName, fileName);
    }

    private void uploadFile(String dirName, String fileName) throws Exception {
        log.info("开始上传数据文件 {} {}", dirName, fileName);
        //zip
        File zipDir = new File(config.getFtpUpDir());
        if (!zipDir.exists()) {
            zipDir.mkdirs();
        }
        File zipFile = new File(config.getFtpUpDir(), fileName + ".zip");
        ZipUtil.zip(dirName, zipFile.getAbsolutePath());
        log.info("完成压缩 {} {}", dirName, fileName);
        //upload
        zxftpClient.uploadFile(zipFile);
        log.info("完成上传 {} {}", dirName, fileName);
    }

    public boolean downloadConfig() {
        try {
            log.info("下载同步配置");
            String absolutePath = zxftpClient.downloadFile(config.getDbConfigFileName(), true);
            if (StringUtils.isNotEmpty(absolutePath)) {
                log.info("同步配置下载成功:{}", absolutePath);
                ZipUtil.unzip(absolutePath, config.getDbConfigDir());
                log.info("解压配置文件完成");
            } else {
                log.info("无新同步配置");
            }
            return true;
        } catch (Exception e) {
            log.warn("Exception", e);
            return false;
        }
    }

    public String downloadCommand() {
        try {
            String absolutePath = zxftpClient.downloadFile(config.getCommandFilePath(), true);
            if (StringUtils.isNotEmpty(absolutePath)) {
                log.info("下载命令文件");
                //读取命令
                File file = new File(absolutePath);
                if (file.exists()) {
                    String commandStr = FileUtils.readFileToString(file, CharsetNames.UTF_8);
                    return commandStr;
                } else {
                    log.info("命令文件不存在");
                }
            }
        } catch (Exception e) {
            log.warn("Exception", e);
        }
        return null;
    }

    public void uploadLogs() {
        String currDate = DateFormatUtils.format(new Date(), "yyyyMMdd");
        uploadLogs(currDate);
    }

    public void uploadLogs(String date) {
        try {
            String logDir = "~/Workspace/dinglicom/logs/datainstall." + date + ".log";
            uploadFile(logDir, "log" + date);
        } catch (Exception e) {
            log.warn("Exception", e);
        }
    }
}
