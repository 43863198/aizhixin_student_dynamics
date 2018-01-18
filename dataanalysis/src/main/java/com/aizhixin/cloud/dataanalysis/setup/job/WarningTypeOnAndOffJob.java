package com.aizhixin.cloud.dataanalysis.setup.job;

import com.aizhixin.cloud.dataanalysis.alertinformation.dto.WarningSettingsDTO;
import com.aizhixin.cloud.dataanalysis.common.constant.WarningTypeConstant;
import com.aizhixin.cloud.dataanalysis.setup.entity.AlarmSettings;
import com.aizhixin.cloud.dataanalysis.setup.entity.WarningType;
import com.aizhixin.cloud.dataanalysis.setup.service.AlarmSettingsService;
import com.aizhixin.cloud.dataanalysis.setup.service.WarningTypeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * @author: Created by jianwei.wu
 * @E-mail: wujianwei@aizhixin.com
 * @Date: 2018-01-15
 */
@Component
public class WarningTypeOnAndOffJob {
    final static private Logger LOG = LoggerFactory.getLogger(WarningTypeOnAndOffJob.class);
    @Autowired
    private WarningTypeService warningTypeService;

    public void updateWarningTypeOnAndOff() {

        Calendar c = Calendar.getInstance();
        // 当前月份
        int month = c.get(Calendar.MONTH)+1;
        // 当前周
        int week = c.get(Calendar.WEEK_OF_MONTH);
        //当前星期
        int weekDay = c.get(Calendar.DAY_OF_WEEK);
        if(month == 9 || month == 3) {
            this.updateAbsenteeismWarningType(10);
            if(week >= 3 && Calendar.MONDAY ==weekDay) {
              this.updateWarningType(10);
            }
        }else if(month == 2 || month == 8){
            this.updateWarningType(20);
            this.updateAbsenteeismWarningType(20);
        }else if(month != 9 && month != 3 && month != 2 && month != 8){
            this.updateWarningType(10);
            this.updateAbsenteeismWarningType(10);
        }
    }

    public void updateWarningType(int setupCloseFlag){

        Set<String> typeList = new HashSet<>();
        typeList.add(WarningTypeConstant.PerformanceFluctuation.
                toString()); // 成绩波动预警设置
        typeList.add(WarningTypeConstant.SupplementAchievement
                .toString()); // 补考成绩预警设置
        typeList.add(WarningTypeConstant.TotalAchievement
                .toString()); // 总评成绩预警设置
        typeList.add(WarningTypeConstant.AttendAbnormal
                .toString()); // 修读异常预警设置
        typeList.add(WarningTypeConstant.LeaveSchool
                .toString()); // 退学预警设置
        typeList.add(WarningTypeConstant.Cet
                .toString()); // 英语四六级预警设置


        List<WarningType> warningTypeList = warningTypeService.getWarningTypeByTypeList(typeList);

        for (WarningType tpl : warningTypeList) {
            tpl.setSetupCloseFlag(setupCloseFlag);
        }
        warningTypeService.save(warningTypeList);
    }


    public void updateAbsenteeismWarningType(int setupCloseFlag){

        Set<String> typeList = new HashSet<>();
        typeList.add(WarningTypeConstant.Absenteeism.
                toString()); // 旷课预警
        List<WarningType> warningTypeList = warningTypeService.getWarningTypeByTypeList(typeList);

        for (WarningType tpl : warningTypeList) {
            tpl.setSetupCloseFlag(setupCloseFlag);
        }
        warningTypeService.save(warningTypeList);
    }

    public void updateRegisterWarningType(int setupCloseFlag){

        Set<String> typeList = new HashSet<>();
        typeList.add(WarningTypeConstant.Register.
                toString()); // 迎新报到
        List<WarningType> warningTypeList = warningTypeService.getWarningTypeByTypeList(typeList);

        for (WarningType tpl : warningTypeList) {
            tpl.setSetupCloseFlag(setupCloseFlag);
        }
        warningTypeService.save(warningTypeList);
    }





}
