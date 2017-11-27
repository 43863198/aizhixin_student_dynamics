package com.aizhixin.cloud.dataanalysis.common.schedule;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.aizhixin.cloud.dataanalysis.common.service.DistributeLock;
import com.aizhixin.cloud.dataanalysis.studentRegister.job.StudentRegisterJob;

/**
 * 定时任务入口
 */
@Component
public class MySchedulingService {
    final static private Logger LOG = LoggerFactory.getLogger(MySchedulingService.class);
    @Autowired
    private DistributeLock distributeLock;
    @Autowired
    private StudentRegisterJob studentRegisterJob;


    /**
     * 定时统计实践任务
     */
    @Scheduled(cron = "0 0/2 * * * ?")
    public void stuRegisterJob() {
        if (distributeLock.getStuRegisterLock()) {
            LOG.info("开始启动学生注册报到预警定时任务");
            studentRegisterJob.studenteRegisterJob();
        } else {
            LOG.info("启动学生注册报到预警，获取锁失败");
        }
    }


}
