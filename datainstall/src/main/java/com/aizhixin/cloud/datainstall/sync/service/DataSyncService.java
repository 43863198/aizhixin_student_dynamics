package com.aizhixin.cloud.datainstall.sync.service;

import com.aizhixin.cloud.datainstall.commons.JsonUtil;
import com.aizhixin.cloud.datainstall.config.Config;
import com.aizhixin.cloud.datainstall.db.service.DataBaseQueryService;
import com.aizhixin.cloud.datainstall.ftp.service.FTPService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.utils.CharsetNames;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class DataSyncService {
    @Autowired
    private FTPService ftpService;
    @Autowired
    private DataBaseQueryService dataBaseQueryService;
    @Autowired
    private Config config;

    /**
     * 1. 下载配置, 解压
     * 2. 导出sql文件
     * 3. 压缩, 上传
     */
    public void syncData() {
        boolean isUploadLogs = false;
        try {
            log.info("开始同步");
            //1. 下载配置, 解压
            isUploadLogs = !ftpService.downloadConfig();
            //2. 导出sql文件
            boolean isOk = dataBaseQueryService.fromDbConfigReadAndOutJson();
            if (!isUploadLogs && !isOk) {
                isUploadLogs = true;
            }
            //3. 压缩, 上传
            ftpService.uploadSyncFile();
            log.info("同步结束");
            dataBaseQueryService.deleteDataFile();
        } catch (Exception e) {
            log.warn("Exception", e);
            isUploadLogs = true;
        }
        if (isUploadLogs) {
            //4.上传日志
            ftpService.uploadLogs();
        }
    }

    /**
     * 1001 上传日志
     * 1011 立即上传数据
     * 1021 停止自动上传数据
     * 1031 恢复自动上传数据
     */
    public void checkCommand() {
        String commandStr = ftpService.downloadCommand();
        if (StringUtils.isNotEmpty(commandStr)) {
            log.info("获取命令: {}", commandStr);
            String[] commands = commandStr.split("\n");
            if (commands != null && commands.length > 0) {
                switch (commands[0]) {
                    case "1001":
                        if (commands.length > 1 && StringUtils.isNotEmpty(commands[1])) {
                            ftpService.uploadLogs(commands[1]);
                        } else {
                            ftpService.uploadLogs();
                        }
                        break;
                    case "1011":
                        syncData();
                        break;
                    case "1021":
                        stopSchedule();
                        break;
                    case "1031":
                        startSchedule();
                        break;
                }
            }
        }
    }

    private void stopSchedule() {
        log.info("设置停止自动任务");
        setConfig("isschedule", "false");
    }

    private void startSchedule() {
        log.info("设置开始自动任务");
        setConfig("isschedule", "true");
    }

    private void setConfig(String key, String value) {
        try {
            File file = new File(config.getFtpConfigFile());
            String configStr = "";
            if (file.exists()) {
                configStr = FileUtils.readFileToString(file, CharsetNames.UTF_8);
            }
            Map<String, Object> data;
            if (StringUtils.isNotEmpty(configStr)) {
                data = JsonUtil.decode(configStr, Map.class);
            } else {
                data = new HashMap<>();
            }
            data.put(key, value);
            configStr = JsonUtil.encode(data);
            FileUtils.writeStringToFile(file, configStr, CharsetNames.UTF_8);
            config.setSchedule(Boolean.parseBoolean(value));
        } catch (Exception e) {
            log.warn("Exception", e);
        }
    }

    public void initConfig() {
        try {
            File file = new File(config.getFtpConfigFile());
            if (file.exists()) {
                String configStr = FileUtils.readFileToString(file, CharsetNames.UTF_8);
                if (StringUtils.isNotEmpty(configStr)) {
                    Map<String, String> data = JsonUtil.decode(configStr, Map.class);
                    if (data.get("isschedule") != null && data.get("isschedule").equals("false")) {
                        config.setSchedule(false);
                    }
                }
            }
            log.info("初始化设置 {}", config.isSchedule());
        } catch (Exception e) {
            log.warn("Exception", e);
        }
    }

    public void checkUpload() {
        try {
            ftpService.checkUpload();
        } catch (Exception e) {
            log.warn("Exception", e);
        }
    }
}
