package com.aizhixin.cloud.dataanalysis.common.schedule;

import com.aizhixin.cloud.dataanalysis.analysis.job.CetStatisticsAnalysisJob;
import com.aizhixin.cloud.dataanalysis.common.service.SyncClassTeacher;
import com.aizhixin.cloud.dataanalysis.setup.entity.WarningType;
import com.aizhixin.cloud.dataanalysis.setup.job.WarningTypeOnAndOffJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.aizhixin.cloud.dataanalysis.alertinformation.job.WarnInforJob;
import com.aizhixin.cloud.dataanalysis.common.service.DistributeLock;
import com.aizhixin.cloud.dataanalysis.monitor.job.RollCallDayJob;
import com.aizhixin.cloud.dataanalysis.monitor.job.TeachingScheduleJob;
import com.aizhixin.cloud.dataanalysis.rollCall.job.RollCallJob;
import com.aizhixin.cloud.dataanalysis.score.job.ScoreJob;
import com.aizhixin.cloud.dataanalysis.studentRegister.job.StudentRegisterJob;

import javax.annotation.PostConstruct;
import java.util.Calendar;

/**
 * 定时任务入口
 */
@Component
public class MySchedulingService {
    final static private Logger LOG = LoggerFactory.getLogger(MySchedulingService.class);
    @Autowired
    private DistributeLock distributeLock;
    @Autowired
    private RollCallJob rollCallJob;
    @Autowired
    private ScoreJob scoreJob;
    @Autowired
    private WarnInforJob warnInforJob;
    @Autowired
    private RollCallDayJob rollCallDayJob;
    @Autowired
    private TeachingScheduleJob teachingScheduleJob;
    @Autowired
    private WarningTypeOnAndOffJob warningTypeOnAndOffJob;
    @Autowired
    private SyncClassTeacher syncClassTeacher;

    @Scheduled(cron = "0 0 3 * * ?")
    public void rollCallCountJob() {
        if (distributeLock.getRollCallCountLock()) {
            LOG.info("开始启动旷课信息统计定时任务");
            Calendar c = Calendar.getInstance();
			//当前年份
			int schoolYear = c.get(Calendar.YEAR);
			//当前月份
			int month = c.get(Calendar.MONTH)+1;
			//当前学期编号
			int semester = 2;
			if( month > 1 && month < 9){
				semester = 1;
			}
			if(month == 1 ){
				schoolYear = schoolYear - 1;
			}
            rollCallJob.rollCallCountJob(schoolYear,semester);
        } else {
            LOG.info("启动旷课信息统计任务，获取锁失败");
        }
    }


//    @Scheduled(cron = "0 0/1 * * * ?")
    public void totalScoreCountJob() {
        if (distributeLock.getTotalScoreCountLock()) {
            LOG.info("开始启动总评不及格成绩信息统计定时任务");
            Calendar c = Calendar.getInstance();
            // 当前年份
            int schoolYear = c.get(Calendar.YEAR);
            // 当前月份
            int month = c.get(Calendar.MONTH)+1;
            // 当前学期编号
            int semester = 2;
            if (month > 1 && month < 9) {
                semester = 1;
            }
            if(month == 1 ){
                schoolYear = schoolYear - 1;
            }
            scoreJob.totalScoreCountJob(schoolYear, semester);;
        } else {
            LOG.info("启动总评不及格成绩统计任务，获取锁失败");
        }
    }

//    @Scheduled(cron = "0 0/1 * * * ?")
    public void makeUpScoreCountJob() {
        if (distributeLock.getMakeUpScoreCountLock()) {
            LOG.info("开始启动补考不及格成绩信息统计定时任务");
            Calendar c = Calendar.getInstance();
            // 当前年份
            int schoolYear = c.get(Calendar.YEAR);
            scoreJob.makeUpScoreCountJob(schoolYear);;
        } else {
            LOG.info("启动总评不及格成绩统计任务，获取锁失败");
        }
    }

//    @Scheduled(cron = "0 0/1 * * * ?")
    public void scoreFluctuateCountJob() {
        if (distributeLock.getScoreFluctuateCountLock()) {
            LOG.info("开始启动获取之前两学期成绩信息统计定时任务");
            Calendar c = Calendar.getInstance();
            // 当前年份
            int schoolYear = c.get(Calendar.YEAR);
            // 当前月份
            int month = c.get(Calendar.MONTH)+1;
            // 当前学期编号
            int semester = 2;
            if (month > 1 && month < 9) {
                semester = 1;
            }
            if(month == 1 ){
                schoolYear = schoolYear - 1;
            }
            scoreJob.scoreFluctuateCountJob(schoolYear, semester);
        } else {
            LOG.info("启动之前两学期成绩统计任务，获取锁失败");
        }
    }

    @Scheduled(cron = "0 0/10 * * * ?")
    public void updateWarnStateJob() {
        if (distributeLock.updateWarnStateJobLock()) {
        	warnInforJob.updateWarnStateJob();
        }
    }

    @Scheduled(cron = "0 0 0/3 * * ?")
    public void rollCallDayJob() {
        if (distributeLock.getRollCallDayLock()) {
            LOG.info("开始实时监控考勤统计定时任务");
            rollCallDayJob.rollCallDayCountJob();
        } else {
            LOG.info("启动实时监控考勤统计，获取锁失败");
        }
    }

    @Scheduled(cron = "0 0 0/3 * * ?")
    public void teachingScheduleJob() {
        if (distributeLock.getTeachingScheduleLock()) {
            LOG.info("开始实时监控排课统计定时任务");
            teachingScheduleJob.getTeachingScheduleJob();
        } else {
            LOG.info("启动实时监控排课统计，获取锁失败");
        }
    }

    @Scheduled(cron = "0 0 2 * * ?")
    public void WarningSettingsOnAndOffJob() {
        if (distributeLock.getWarningSettingsOnAndOffScheduleLock()) {
            LOG.info("预警设置定时任务开启/关闭");
            warningTypeOnAndOffJob.updateWarningTypeOnAndOff();
        } else {
            LOG.info("预警设置定时任务开启/关闭，获取锁失败");
        }
    }

    @Scheduled(cron = "0 0/15 * * * ?")
    public void SyncDate() {
        if (distributeLock.getSyncClassTeacherLock()) {
            LOG.info("开始启动学生注册报到预警定时任务");
            Long orgId = 138L;//现网桂林理工的学校id
            syncClassTeacher.syncData(orgId);
        } else {
            LOG.info("启动学生注册报到预警，获取锁失败");
        }
    }

}
