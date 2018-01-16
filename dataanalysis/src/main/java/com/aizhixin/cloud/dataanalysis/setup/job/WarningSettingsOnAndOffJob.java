package com.aizhixin.cloud.dataanalysis.setup.job;

import com.aizhixin.cloud.dataanalysis.alertinformation.dto.WarningSettingsDTO;
import com.aizhixin.cloud.dataanalysis.common.constant.WarningTypeConstant;
import com.aizhixin.cloud.dataanalysis.setup.entity.AlarmSettings;
import com.aizhixin.cloud.dataanalysis.setup.service.AlarmSettingsService;
import com.aizhixin.cloud.dataanalysis.setup.service.WarningTypeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * @author: Created by jianwei.wu
 * @E-mail: wujianwei@aizhixin.com
 * @Date: 2018-01-15
 */
@Component
public class WarningSettingsOnAndOffJob {
    final static private Logger LOG = LoggerFactory.getLogger(WarningSettingsOnAndOffJob.class);
    @Autowired
    private AlarmSettingsService alarmSettingsService;

    public void updateWarningSettingsOnAndOff() {

        Calendar c = Calendar.getInstance();
        // 当前月份
        int month = c.get(Calendar.MONTH)+1;
        // 当前周
        int week = c.get(Calendar.WEEK_OF_MONTH);
        if(month == 9 || month == 3) {
            this.updateAbsenteeismWarningSettings(10);
            if(week >= 3) {
              this.updateWarningSettings(10);
            }
        }else if(month == 2 || month == 8){
            this.updateWarningSettings(20);
            this.updateAbsenteeismWarningSettings(20);
        }else if(month != 9 && month != 3 && month != 2 && month != 8){
            this.updateWarningSettings(10);
            this.updateAbsenteeismWarningSettings(10);
        }
    }

    public void updateWarningSettings(int setupCloseFlag){

        List<String> warningTypeList = new ArrayList<>();
        warningTypeList.add(WarningTypeConstant.PerformanceFluctuation.
                toString()); // 成绩波动预警设置
        warningTypeList.add(WarningTypeConstant.SupplementAchievement
                .toString()); // 补考成绩预警设置
        warningTypeList.add(WarningTypeConstant.TotalAchievement
                .toString()); // 总评成绩预警设置
        warningTypeList.add(WarningTypeConstant.AttendAbnormal
                .toString()); // 修读异常预警设置
        warningTypeList.add(WarningTypeConstant.LeaveSchool
                .toString()); // 退学预警设置

        List<AlarmSettings> alarmSettingsList = alarmSettingsService.getAlarmSettingsByTypeList(warningTypeList);

        for (AlarmSettings as : alarmSettingsList) {
            as.setSetupCloseFlag(setupCloseFlag);
        }
        alarmSettingsService.saveAlarmSettingsList(alarmSettingsList);
    }


    public void updateAbsenteeismWarningSettings(int setupCloseFlag){

        List<String> warningTypeList = new ArrayList<>();
        warningTypeList.add(WarningTypeConstant.Absenteeism.
                toString()); // 旷课预警
        List<AlarmSettings> alarmSettingsList = alarmSettingsService.getAlarmSettingsByTypeList(warningTypeList);

        for (AlarmSettings as : alarmSettingsList) {
            as.setSetupCloseFlag(setupCloseFlag);
        }
        alarmSettingsService.saveAlarmSettingsList(alarmSettingsList);
    }




}
