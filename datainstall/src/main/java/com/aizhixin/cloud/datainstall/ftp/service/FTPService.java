package com.aizhixin.cloud.datainstall.ftp.service;

import com.aizhixin.cloud.datainstall.config.Config;
import com.aizhixin.cloud.datainstall.ftp.dto.UploadRetry;
import com.aizhixin.cloud.datainstall.ftp.utils.ZXFTPClient;
import com.aizhixin.cloud.datainstall.ftp.utils.ZipUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.utils.CharsetNames;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class FTPService {
    @Autowired
    private ZXFTPClient zxftpClient;
    @Autowired
    private Config config;

    private Map<String, UploadRetry> uploadMap = new HashMap<>();

    public void uploadSyncFile() throws Exception {
        //默认文件夹
        String fileName = DateFormatUtils.format(new Date(), "yyyyMMdd");
        File file = new File(config.getDbOutDir(), fileName);
        uploadSyncFile(file.getAbsolutePath(), "data" + fileName);
    }

    public void uploadSyncFile(String dirName, String fileName) throws Exception {
        uploadFile(dirName, fileName, true, false);
    }

    private void uploadFile(String dirName, String fileName, boolean isRetry, boolean isDelSrc) throws Exception {
        log.info("开始上传数据文件 {} {}", dirName, fileName);
        //zip
        File zipDir = new File(config.getFtpUpDir());
        if (!zipDir.exists()) {
            zipDir.mkdirs();
        }
        File zipFile = new File(config.getFtpUpDir(), fileName + ".zip");
        boolean zipresult = ZipUtil.zip(dirName, zipFile.getAbsolutePath());
        log.info("完成压缩 {}", zipresult);
        if (!zipresult) {
            zipFile.delete();
            return;
        }
        //upload
        try {
            zxftpClient.uploadFile(zipFile);
            log.info("上传成功 {} {}", dirName, fileName);
            if (isRetry) {
                uploadMap.remove(zipFile.getAbsolutePath());
            }
            if (isDelSrc) {
                zipFile.delete();
            }
        } catch (Exception e) {
            log.info("上传失败 {} {}", dirName, fileName);
            if (isRetry) {
                if (uploadMap.get(zipFile.getAbsolutePath()) != null) {
                    UploadRetry dto = uploadMap.get(zipFile.getAbsolutePath());
                    if (dto.getCount() < 5) {
                        log.info("上传文件进入重试队列 {} {}", dirName, fileName);
                        dto.setCount(dto.getCount() + 1);
                        Date nextTime = new Date();
                        DateUtils.addHours(nextTime, dto.getCount() * 30);
                        dto.setNextTime(nextTime);
                        uploadMap.put(zipFile.getAbsolutePath(), dto);
                    } else {
                        log.info("达到重试次数，不再上传 {} {}", dirName, fileName);
                        uploadMap.remove(zipFile.getAbsolutePath());
                    }

                } else {
                    log.info("上传文件进入重试队列 {} {}", dirName, fileName);
                    UploadRetry dto = new UploadRetry();
                    dto.setCount(1);
                    Date nextTime = new Date();
                    DateUtils.addMinutes(nextTime, 30);
                    dto.setNextTime(nextTime);
                    dto.setDirName(dirName);
                    dto.setFileName(fileName);
                    uploadMap.put(zipFile.getAbsolutePath(), dto);
                }
            }
        }
        log.info("完成上传 {} {}", dirName, fileName);
    }

    public void checkUpload() throws Exception {
        Long time = new Date().getTime();
        for (String key : uploadMap.keySet()) {
            UploadRetry dto = uploadMap.get(key);
            if (time >= dto.getNextTime().getTime()) {
                uploadFile(dto.getDirName(), dto.getFileName(), true, false);
                uploadMap.remove(key);
            }
        }
    }

    public boolean downloadConfig() {
        try {
            log.info("下载同步配置");
            String absolutePath = zxftpClient.downloadFile(config.getDbConfigFileName(), true);
            if (StringUtils.isNotEmpty(absolutePath)) {
                log.info("同步配置下载成功:{}", absolutePath);
                ZipUtil.unzip(absolutePath, config.getDbConfigDir());
                log.info("解压配置文件完成");
                File zipFile = new File(absolutePath);
                if (zipFile.exists()) {
                    zipFile.delete();
                }
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
            String absolutePath = zxftpClient.downloadFile(config.getFtpCommandFileName(), true);
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
        String currDate = DateFormatUtils.format(new Date(), "yyyy-MM-dd");
        uploadLogs(currDate);
    }

    public void uploadLogs(String date) {
        try {
            String logFile = config.getLogDir() + "/datainstall." + date + ".log";
            File file = new File(logFile);
            if (file.exists() && file.length() > 0) {
                uploadFile(logFile, "log" + date, true, true);
            }
        } catch (Exception e) {
            log.warn("Exception", e);
        }
    }
}
