package com.aizhixin.cloud.datainstall.sync.schedule;

import com.aizhixin.cloud.datainstall.sync.service.DataSyncService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DataSyncSchedule {
    @Autowired
    private DataSyncService syncService;

    @Scheduled(cron = "1 0 0 * * ?")
    public void dayDataTask() {
        log.info("--------开始同步--------");
        syncService.syncData();
        log.info("--------同步结束--------");
    }


}
