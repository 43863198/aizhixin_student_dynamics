package com.aizhixin.cloud.datainstall.schedule;

import com.aizhixin.cloud.datainstall.config.Config;
import com.aizhixin.cloud.datainstall.sync.service.DataSyncService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Slf4j
@Component
public class DataSyncSchedule {
    @Autowired
    private DataSyncService syncService;
    @Autowired
    private Config config;

    @Scheduled(cron = "0 0 22 * * ?")
    public void dayDataTask() {
        if(!config.isSchedule()){
            log.info("已停止自动上传数据");
            return;
        }
        log.info("--------定时同步开始--------");
        syncService.syncData();
        log.info("--------定时同步结束--------");
    }

    @Scheduled(cron = "0 0/10 * * * ?")
    public void commandTask() {
        syncService.checkCommand();
    }

    @Scheduled(cron = "0 0/30 * * * ?")
    public void uploadTask() {
        syncService.checkUpload();
    }

    @PostConstruct
    public void start() {
        syncService.initConfig();
    }

}
