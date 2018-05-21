package com.aizhixin.cloud.dataanalysis.setup.job;

import com.aizhixin.cloud.dataanalysis.alertinformation.entity.WarningInformation;
import com.aizhixin.cloud.dataanalysis.alertinformation.service.AlertWarningInformationService;
import com.aizhixin.cloud.dataanalysis.common.constant.WarningTypeConstant;
import com.aizhixin.cloud.dataanalysis.rollCall.job.RollCallJob;
import com.aizhixin.cloud.dataanalysis.score.job.ScoreJob;
import com.aizhixin.cloud.dataanalysis.setup.entity.AlarmSettings;
import com.aizhixin.cloud.dataanalysis.setup.entity.RuleParameter;
import com.aizhixin.cloud.dataanalysis.setup.entity.WarningType;
import com.aizhixin.cloud.dataanalysis.setup.service.AlarmSettingsService;
import com.aizhixin.cloud.dataanalysis.setup.service.RuleParameterService;
import com.aizhixin.cloud.dataanalysis.setup.service.WarningTypeService;
import com.aizhixin.cloud.dataanalysis.studentRegister.job.StudentRegisterJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

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
    @Autowired
    private AlarmSettingsService alarmSettingsService;
    @Autowired
    private RuleParameterService ruleParameterService;
    @Autowired
    private StudentRegisterJob studentRegisterJob;
    @Autowired
    private RollCallJob rollCallJob;
    @Autowired
    private ScoreJob scoreJob;
    @Autowired
    private AlertWarningInformationService warningInformationService;


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
        }else {
            if(month == 2 || month == 8){
                this.updateWarningType(20);
                this.updateAbsenteeismWarningType(20);
            }else{
                this.updateWarningType(10);
                this.updateAbsenteeismWarningType(10);
            }
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


    public AlarmSettingsService getAlarmSettingsService() {
        return alarmSettingsService;
    }

    public AlertWarningInformationService getWarningInformationService() {
        return warningInformationService;
    }

    /**********************************************************************************************/

    public void warningJob() {
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
        //获取的预警类型
        List<WarningType> warningTypeList = warningTypeService.getAllWarningTypeList();
        //已经开启次预警类型的组织
        Set<Long> orgIdSet = new HashSet<>();
        for (WarningType wt : warningTypeList) {
            if (wt.getSetupCloseFlag() == 10) {
               this.warningInfo(wt.getOrgId(),wt.getType(),schoolYear,semester);
            }
        }
    }
    @Async
    public void warningInfo(Long orgId, String type,int schoolYear,int semester ){
        List<AlarmSettings> alarmSettingsList = alarmSettingsService.getAlarmSettingsByOrgIdAndWarningType(orgId,type);
        HashMap<Integer, List<WarningInformation>> restHasMap = new HashMap<>();
        for(AlarmSettings as: alarmSettingsList){
            if(null!=as){
                String[] ruleIds = as.getRuleSet().split(",");
                List<WarningInformation> gradeList = new ArrayList<>();
                List<WarningInformation> infoList = new ArrayList<>();
                ArrayList<WarningInformation> registerList = null;
                ArrayList<WarningInformation> absenteeismList = null;
                List<WarningInformation>  performanceFluctuationList = null;
                List<WarningInformation>  supplementAchievementList = null;
                List<WarningInformation>  totalAchievementList = null;
                List<WarningInformation>  leaveSchoolList = null;
                List<WarningInformation>  cetList = null;
                for (String ruleId : ruleIds) {
                    if (!StringUtils.isEmpty(ruleId)) {
                        RuleParameter rp = ruleParameterService.findById(ruleId);

                        if(rp.getRuleName().equals("RegisterEarlyWarning")) {
                            WarningType wt = warningTypeService.getWarningTypeByOrgIdAndType(orgId, type);
                            Date startTime = wt.getStartTime();
                            registerList = studentRegisterJob.studenteRegisterJob(orgId, schoolYear, semester, as.getId(), ruleId, startTime);
                            gradeList.addAll(registerList);
                        }
                        if(rp.getRuleName().equals("AbsenteeismEarlyWarning")){
                            absenteeismList = rollCallJob.rollCallJob(orgId, schoolYear, semester, as.getId(), ruleId);
                            gradeList.addAll(absenteeismList);
                        }
                        if(rp.getRuleName().equals("PerformanceFluctuationEarlyWarning")){
                              performanceFluctuationList = scoreJob.scoreFluctuateJob(orgId, schoolYear, semester,  as.getId(), ruleId);
                            gradeList.addAll(performanceFluctuationList);
                        }
                        if(rp.getRuleName().equals("SupplementAchievementEarlyWarning")){
                              supplementAchievementList = scoreJob.makeUpScoreJob(orgId, schoolYear, semester, as.getId(), ruleId);
                            gradeList.addAll(supplementAchievementList);
                        }
                        if(rp.getRuleName().equals("TotalAchievementEarlyWarning")){
                            totalAchievementList = scoreJob.totalScoreJob(orgId, schoolYear, semester,  as.getId(),ruleId);
                            gradeList.addAll(totalAchievementList);
                        }
                        if(rp.getRuleName().equals("LeaveSchoolEarlyWarning")){
                             leaveSchoolList = scoreJob.dropOutJob(orgId, schoolYear, semester, as.getId(), ruleId);
                            gradeList.addAll(leaveSchoolList);
                        }
                        if(rp.getRuleName().equals("CetEarlyWarning")){
                              cetList = scoreJob.cet4ScoreJob(orgId, schoolYear, semester, as.getId(), ruleId);
                            gradeList.addAll(cetList);
                        }
                    }
                }
                Set<String> jobNumberSet = new HashSet<>();
                for(WarningInformation wf : gradeList){
                    jobNumberSet.add(wf.getJobNumber());
                }
                if(as.getRelationship().equals("与")){
                    if(null!=registerList) {
                        for (WarningInformation wi : registerList) {
                            if (jobNumberSet.contains(wi.getJobNumber())) {
                                infoList.add(wi);
                            }
                        }
                    }
                    if(null!=absenteeismList) {
                        for (WarningInformation wi : registerList) {
                            if (jobNumberSet.contains(wi.getJobNumber())) {
                                infoList.add(wi);
                            }
                        }
                    }
                    if(null!=performanceFluctuationList) {
                        for (WarningInformation wi : registerList) {
                            if (jobNumberSet.contains(wi.getJobNumber())) {
                                infoList.add(wi);
                            }
                        }
                    }
                    if(null!=supplementAchievementList) {
                        for (WarningInformation wi : registerList) {
                            if (jobNumberSet.contains(wi.getJobNumber())) {
                                infoList.add(wi);
                            }
                        }
                    }
                    if(null!=totalAchievementList) {
                        for (WarningInformation wi : registerList) {
                            if (jobNumberSet.contains(wi.getJobNumber())) {
                                infoList.add(wi);
                            }
                        }
                    }
                    if(null!=leaveSchoolList) {
                        for (WarningInformation wi : registerList) {
                            if (jobNumberSet.contains(wi.getJobNumber())) {
                                infoList.add(wi);
                            }
                        }
                    }
                    if(null!=cetList) {
                        for (WarningInformation wi : registerList) {
                            if (jobNumberSet.contains(wi.getJobNumber())) {
                                infoList.add(wi);
                            }
                        }
                    }
                }
                if(as.getRelationship().equals("或")){
                    infoList.addAll(gradeList);
                }
                restHasMap.put(as.getWarningLevel(),infoList);
            }
        }
        //按照预警等级去重
        LinkedList<WarningInformation> resList = new LinkedList<>();
        Set<String> jobNumber = new HashSet<>();
        if(restHasMap.containsKey(1)){
            for(WarningInformation w : restHasMap.get(1)){
                w.setWarningLevel(1);
                jobNumber.add(w.getJobNumber());
                resList.add(w);
            }
        }
        if(restHasMap.containsKey(2)){
            for(WarningInformation w : restHasMap.get(1)){
                if(!jobNumber.contains(w.getJobNumber())) {
                    w.setWarningLevel(2);
                    jobNumber.add(w.getJobNumber());
                    resList.add(w);
                }
            }
        }
        if(restHasMap.containsKey(3)){
            for(WarningInformation w : restHasMap.get(3)){
                if(!jobNumber.contains(w.getJobNumber())) {
                    w.setWarningLevel(3);
                    jobNumber.add(w.getJobNumber());
                    resList.add(w);
                }
            }
        }
        warningInformationService.save(resList);
    }
}
