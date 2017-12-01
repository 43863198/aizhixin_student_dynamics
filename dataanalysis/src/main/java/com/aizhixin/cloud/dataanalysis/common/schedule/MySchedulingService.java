package com.aizhixin.cloud.dataanalysis.common.schedule;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.aizhixin.cloud.dataanalysis.common.service.DistributeLock;
import com.aizhixin.cloud.dataanalysis.rollCall.job.RollCallJob;
import com.aizhixin.cloud.dataanalysis.score.job.ScoreJob;
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
    @Autowired
    private RollCallJob rollCallJob;
    @Autowired
    private ScoreJob scoreJob;


//    @Scheduled(cron = "0 0/1 * * * ?")
    public void stuRegisterJob() {
        if (distributeLock.getStuRegisterLock()) {
            LOG.info("开始启动学生注册报到预警定时任务");
            studentRegisterJob.studenteRegisterJob();
        } else {
            LOG.info("启动学生注册报到预警，获取锁失败");
        }
    }

    
//    @Scheduled(cron = "0 0/5 * * * ?")
    public void rollCallJob() {
        if (distributeLock.getRollCallLock()) {
            LOG.info("开始启动旷课预警定时任务");
            rollCallJob.rollCallJob();;
        } else {
            LOG.info("启动旷课预警，获取锁失败");
        }
    }
    
    
//    @Scheduled(cron = "0 0/1 * * * ?")
    public void rollCallCountJob() {
        if (distributeLock.getRollCallCountLock()) {
            LOG.info("开始启动旷课信息统计定时任务");
            rollCallJob.rollCallCountJob();
        } else {
            LOG.info("启动旷课信息统计任务，获取锁失败");
        }
    }
    
    
//    @Scheduled(cron = "0 0/1 * * * ?")
    public void totalScoreCountJob() {
        if (distributeLock.getTotalScoreCountLock()) {
            LOG.info("开始启动总评不及格成绩信息统计定时任务");
            scoreJob.totalScoreCountJob();;
        } else {
            LOG.info("启动总评不及格成绩统计任务，获取锁失败");
        }
    }
    
    
//    @Scheduled(cron = "0 0/1 * * * ?")
    public void totalScoreJob() {
        if (distributeLock.getTotalScoreLock()) {
            LOG.info("开始启动总评成绩预警定时任务");
            scoreJob.totalScoreJob();
        } else {
            LOG.info("启动总评成绩预警任务，获取锁失败");
        }
    }
    
    
//    @Scheduled(cron = "0 0/1 * * * ?")
    public void makeUpScoreCountJob() {
        if (distributeLock.getMakeUpScoreCountLock()) {
            LOG.info("开始启动补考不及格成绩信息统计定时任务");
            scoreJob.makeUpScoreCountJob();;
        } else {
            LOG.info("启动总评不及格成绩统计任务，获取锁失败");
        }
    }
    
    
//    @Scheduled(cron = "0 0/1 * * * ?")
    public void makeUpScoreJob() {
        if (distributeLock.getMakeUpScoreLock()) {
            LOG.info("开始启动总评成绩预警定时任务");
            scoreJob.makeUpScoreJob();
        } else {
            LOG.info("启动补考成绩预警任务，获取锁失败");
        }
    }
    
//    @Scheduled(cron = "0 0/1 * * * ?")
    public void scoreFluctuateCountJob() {
        if (distributeLock.getScoreFluctuateCountLock()) {
            LOG.info("开始启动获取之前两学期成绩信息统计定时任务");
            scoreJob.scoreFluctuateCountJob();
        } else {
            LOG.info("启动之前两学期成绩统计任务，获取锁失败");
        }
    }
    
    
//    @Scheduled(cron = "0 0/1 * * * ?")
    public void scoreFluctuateJob() {
        if (distributeLock.getScoreFluctuateLock()) {
            LOG.info("开始启动成绩波动预警定时任务");
            scoreJob.scoreFluctuateJob();
        } else {
            LOG.info("启动成绩波动预警任务，获取锁失败");
        }
    }

//    @Scheduled(cron = "0 0/1 * * * ?")
    public void attendAbnormalJob() {
        if (distributeLock.getAttendAbnormalLock()) {
            LOG.info("开始启动修读异常预警定时任务");
            scoreJob.attendAbnormalJob();;
        } else {
            LOG.info("启动修读异常预警任务，获取锁失败");
        }
    }
    
    @Scheduled(cron = "0 0/1 * * * ?")
    public void cet4ScoreJob() {
        if (distributeLock.getCet4ScoreJobLock()) {
            LOG.info("开始启动英语四级成绩预警定时任务");
            scoreJob.cet4ScoreJob();
        } else {
            LOG.info("启动英语四级成绩预警任务，获取锁失败");
        }
    }
}
