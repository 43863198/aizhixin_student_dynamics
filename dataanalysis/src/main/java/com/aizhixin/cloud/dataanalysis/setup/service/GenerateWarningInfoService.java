package com.aizhixin.cloud.dataanalysis.setup.service;

import com.aizhixin.cloud.dataanalysis.alertinformation.entity.WarningInformation;
import com.aizhixin.cloud.dataanalysis.alertinformation.service.AlertWarningInformationService;
import com.aizhixin.cloud.dataanalysis.rollCall.job.RollCallJob;
import com.aizhixin.cloud.dataanalysis.score.job.ScoreJob;
import com.aizhixin.cloud.dataanalysis.setup.entity.AlarmSettings;
import com.aizhixin.cloud.dataanalysis.setup.entity.RuleParameter;
import com.aizhixin.cloud.dataanalysis.setup.entity.WarningType;
import com.aizhixin.cloud.dataanalysis.studentRegister.job.StudentRegisterJob;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * @author: Created by jianwei.wu
 * @E-mail: wujianwei@aizhixin.com
 * @Date: 2018-05-21
 */
@Service
public class GenerateWarningInfoService {
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
        if(null!=warningTypeList&&warningTypeList.size()>0) {
            for (WarningType wt : warningTypeList) {
                if (wt.getSetupCloseFlag() == 10) {
                    this.warningInfo(wt.getOrgId(), wt.getType(), schoolYear, semester);
                }
            }
        }
    }
    @Async
    public void warningInfo(Long orgId, String type,int schoolYear,int semester ){
        List<AlarmSettings> alarmSettingsList = alarmSettingsService.getAlarmSettingsByOrgIdAndWarningType(orgId,type);
        HashMap<Integer, List<WarningInformation>> restHasMap = new HashMap<>();
        for(AlarmSettings as: alarmSettingsList) {
            if (null != as) {
                String[] ruleIds = as.getRuleSet().split(",");
                List<WarningInformation> gradeList = new ArrayList<>();
                ArrayList<WarningInformation> registerList = null;
                ArrayList<WarningInformation> absenteeismList = null;
                List<WarningInformation> performanceFluctuationList = null;
                List<WarningInformation> supplementAchievementList = null;
                List<WarningInformation> totalAchievementList = null;
                List<WarningInformation> leaveSchoolList = null;
                List<WarningInformation> cetList = null;
                for (String ruleId : ruleIds) {
                    if (!StringUtils.isEmpty(ruleId)) {
                        RuleParameter rp = ruleParameterService.findById(ruleId);

                        if (rp.getRuleName().equals("RegisterEarlyWarning")) {
                            WarningType wt = warningTypeService.getWarningTypeByOrgIdAndType(orgId, type);
                            Date startTime = wt.getStartTime();
                            registerList = studentRegisterJob.studenteRegisterJob(orgId, schoolYear, semester, ruleId, startTime);
                        }
                        if (rp.getRuleName().equals("AbsenteeismEarlyWarning")) {
                            absenteeismList = rollCallJob.rollCallJob(orgId, schoolYear, semester, ruleId);
                        }
                        if (rp.getRuleName().equals("PerformanceFluctuationEarlyWarning")) {
                            performanceFluctuationList = scoreJob.scoreFluctuateJob(orgId, schoolYear, semester, ruleId);
                        }
                        if (rp.getRuleName().equals("SupplementAchievementEarlyWarning")) {
                            supplementAchievementList = scoreJob.makeUpScoreJob(orgId, schoolYear, semester, ruleId);
                        }
                        if (rp.getRuleName().equals("TotalAchievementEarlyWarning")) {
                            totalAchievementList = scoreJob.totalScoreJob(orgId, schoolYear, semester, ruleId);
                        }
                        if (rp.getRuleName().equals("LeaveSchoolEarlyWarning")) {
                            leaveSchoolList = scoreJob.dropOutJob(orgId, schoolYear, semester, ruleId);
                        }
                        if (rp.getRuleName().equals("CetEarlyWarning")) {
                            cetList = scoreJob.cet4ScoreJob(orgId, schoolYear, semester, ruleId);
                        }
                        if (rp.getRuleName().equals("AttendAbnormalEarlyWarning")) {
                            cetList = scoreJob.attendAbnormalJob(orgId, schoolYear, semester, ruleId);
                        }
                    }
                }
                Set<String> studentJobNumberSet = new HashSet<>();
                if (as.getRelationship().equals("与")) {
                    if (null != registerList) {
                        if (studentJobNumberSet.isEmpty()) {
                            for (WarningInformation wi : registerList) {
                                wi.setAlarmSettingsId(as.getId());
                                wi.setWarningLevel(as.getWarningLevel());
                                studentJobNumberSet.add(wi.getJobNumber());
                            }
                            gradeList.addAll(registerList);
                        } else {
                            List<WarningInformation> addList = new ArrayList<>();
                            for (WarningInformation gwi : gradeList) {
                                gwi.setAlarmSettingsId(as.getId());
                                gwi.setWarningLevel(as.getWarningLevel());
                                for (WarningInformation wi : registerList) {
                                    if (gwi.getJobNumber().equals(wi.getJobNumber())) {
                                        gwi.setWarningCondition(gwi.getWarningCondition() + "且" + wi.getWarningCondition());
                                        gwi.setWarningSource(gwi.getWarningSource() + ";" + wi.getWarningSource());
                                        addList.add(gwi);
                                        break;
                                    }
                                }
                            }
                            gradeList = addList;
                        }
                    }
                    if (null != absenteeismList) {
                        if (studentJobNumberSet.isEmpty()) {
                            for (WarningInformation wi : absenteeismList) {
                                wi.setAlarmSettingsId(as.getId());
                                wi.setWarningLevel(as.getWarningLevel());
                                studentJobNumberSet.add(wi.getJobNumber());
                            }
                            gradeList.addAll(absenteeismList);
                        } else {
                            List<WarningInformation> addList = new ArrayList<>();
                            for (WarningInformation gwi : gradeList) {
                                gwi.setAlarmSettingsId(as.getId());
                                gwi.setWarningLevel(as.getWarningLevel());
                                for (WarningInformation wi : absenteeismList) {
                                    if (gwi.getJobNumber().equals(wi.getJobNumber())) {
                                        gwi.setWarningCondition(gwi.getWarningCondition() + "且" + wi.getWarningCondition());
                                        gwi.setWarningSource(gwi.getWarningSource() + ";" + wi.getWarningSource());
                                        addList.add(gwi);
                                        break;
                                    }
                                }
                            }
                            gradeList = addList;
                        }
                    }
                    if (null != performanceFluctuationList) {
                        if (studentJobNumberSet.isEmpty()) {
                            for (WarningInformation wi : performanceFluctuationList) {
                                wi.setAlarmSettingsId(as.getId());
                                wi.setWarningLevel(as.getWarningLevel());
                                studentJobNumberSet.add(wi.getJobNumber());
                            }
                            gradeList.addAll(performanceFluctuationList);
                        } else {
                            List<WarningInformation> addList = new ArrayList<>();
                            for (WarningInformation gwi : gradeList) {
                                for (WarningInformation wi : performanceFluctuationList) {
                                    gwi.setAlarmSettingsId(as.getId());
                                    gwi.setWarningLevel(as.getWarningLevel());
                                    if (gwi.getJobNumber().equals(wi.getJobNumber())) {
                                        gwi.setWarningCondition(gwi.getWarningCondition() + "且" + wi.getWarningCondition());
                                        gwi.setWarningSource(gwi.getWarningSource() + ";" + wi.getWarningSource());
                                        addList.add(gwi);
                                        break;
                                    }
                                }
                            }
                            gradeList = addList;
                        }
                    }
                    if (null != supplementAchievementList) {
                        if (studentJobNumberSet.isEmpty()) {
                            for (WarningInformation wi : supplementAchievementList) {
                                wi.setAlarmSettingsId(as.getId());
                                wi.setWarningLevel(as.getWarningLevel());
                                studentJobNumberSet.add(wi.getJobNumber());
                            }
                            gradeList.addAll(supplementAchievementList);
                        } else {
                            List<WarningInformation> addList = new ArrayList<>();
                            for (WarningInformation gwi : gradeList) {
                                gwi.setAlarmSettingsId(as.getId());
                                gwi.setWarningLevel(as.getWarningLevel());
                                for (WarningInformation wi : supplementAchievementList) {
                                    if (gwi.getJobNumber().equals(wi.getJobNumber())) {
                                        gwi.setWarningCondition(gwi.getWarningCondition() + "且" + wi.getWarningCondition());
                                        gwi.setWarningSource(gwi.getWarningSource() + ";" + wi.getWarningSource());
                                        addList.add(gwi);
                                        break;
                                    }
                                }
                            }
                            gradeList = addList;
                        }
                    }
                    if (null != totalAchievementList) {
                        if (studentJobNumberSet.isEmpty()) {
                            for (WarningInformation wi : totalAchievementList) {
                                wi.setAlarmSettingsId(as.getId());
                                wi.setWarningLevel(as.getWarningLevel());
                                studentJobNumberSet.add(wi.getJobNumber());
                            }
                            gradeList.addAll(totalAchievementList);
                        } else {
                            List<WarningInformation> addList = new ArrayList<>();
                            for (WarningInformation gwi : gradeList) {
                                gwi.setAlarmSettingsId(as.getId());
                                gwi.setWarningLevel(as.getWarningLevel());
                                for (WarningInformation wi : totalAchievementList) {
                                    if (gwi.getJobNumber().equals(wi.getJobNumber())) {
                                        gwi.setWarningCondition(gwi.getWarningCondition() + "且" + wi.getWarningCondition());
                                        gwi.setWarningSource(gwi.getWarningSource() + ";" + wi.getWarningSource());
                                        addList.add(gwi);
                                        break;
                                    }
                                }
                            }
                            gradeList = addList;
                        }
                    }
                    if (null != leaveSchoolList) {
                        if (studentJobNumberSet.isEmpty()) {
                            for (WarningInformation wi : leaveSchoolList) {
                                wi.setAlarmSettingsId(as.getId());
                                wi.setWarningLevel(as.getWarningLevel());
                                studentJobNumberSet.add(wi.getJobNumber());
                            }
                            gradeList.addAll(leaveSchoolList);
                        } else {
                            List<WarningInformation> addList = new ArrayList<>();
                            for (WarningInformation gwi : gradeList) {
                                gwi.setAlarmSettingsId(as.getId());
                                gwi.setWarningLevel(as.getWarningLevel());
                                for (WarningInformation wi : leaveSchoolList) {
                                    if (gwi.getJobNumber().equals(wi.getJobNumber())) {
                                        gwi.setWarningCondition(gwi.getWarningCondition() + "且" + wi.getWarningCondition());
                                        gwi.setWarningSource(gwi.getWarningSource() + ";" + wi.getWarningSource());
                                        addList.add(gwi);
                                        break;
                                    }
                                }
                            }
                            gradeList = addList;
                        }
                    }
                    if (null != cetList) {
                        if (studentJobNumberSet.isEmpty()) {
                            for (WarningInformation wi : cetList) {
                                wi.setAlarmSettingsId(as.getId());
                                wi.setWarningLevel(as.getWarningLevel());
                                studentJobNumberSet.add(wi.getJobNumber());
                            }
                            gradeList.addAll(cetList);
                        } else {
                            List<WarningInformation> addList = new ArrayList<>();
                            for (WarningInformation gwi : gradeList) {
                                gwi.setAlarmSettingsId(as.getId());
                                gwi.setWarningLevel(as.getWarningLevel());
                                for (WarningInformation wi : cetList) {
                                    if (gwi.getJobNumber().equals(wi.getJobNumber())) {
                                        gwi.setWarningCondition(gwi.getWarningCondition() + "且" + wi.getWarningCondition());
                                        gwi.setWarningSource(gwi.getWarningSource() + ";" + wi.getWarningSource());
                                        addList.add(gwi);
                                        break;
                                    }
                                }
                            }
                            gradeList = addList;
                        }
                    }
                } else {
                    if (null != registerList && gradeList.isEmpty()) {
                        for (WarningInformation gwi : gradeList) {
                            gwi.setAlarmSettingsId(as.getId());
                            gwi.setWarningLevel(as.getWarningLevel());
                            for (WarningInformation wi : registerList) {
                                if (gwi.getJobNumber().equals(wi.getJobNumber())) {
                                    gwi.setWarningCondition(gwi.getWarningCondition() + "或" + wi.getWarningCondition());
                                    gwi.setWarningSource(gwi.getWarningSource() + ";" + wi.getWarningSource());
                                    registerList.remove(wi);
                                }
                            }
                        }
                        gradeList.addAll(registerList);
                    }
                    if (null != absenteeismList && absenteeismList.isEmpty()) {
                        for (WarningInformation gwi : gradeList) {
                            gwi.setAlarmSettingsId(as.getId());
                            gwi.setWarningLevel(as.getWarningLevel());
                            for (WarningInformation wi : absenteeismList) {
                                if (gwi.getJobNumber().equals(wi.getJobNumber())) {
                                    gwi.setWarningCondition(gwi.getWarningCondition() + "或" + wi.getWarningCondition());
                                    gwi.setWarningSource(gwi.getWarningSource() + ";" + wi.getWarningSource());
                                    absenteeismList.remove(wi);
                                }
                            }
                        }
                        gradeList.addAll(absenteeismList);

                    }
                    if (null != performanceFluctuationList && performanceFluctuationList.isEmpty()) {
                        for (WarningInformation gwi : gradeList) {
                            gwi.setAlarmSettingsId(as.getId());
                            gwi.setWarningLevel(as.getWarningLevel());
                            for (WarningInformation wi : performanceFluctuationList) {
                                if (gwi.getJobNumber().equals(wi.getJobNumber())) {
                                    gwi.setWarningCondition(gwi.getWarningCondition() + "或" + wi.getWarningCondition());
                                    gwi.setWarningSource(gwi.getWarningSource() + ";" + wi.getWarningSource());
                                    performanceFluctuationList.remove(wi);
                                }
                            }
                        }
                        gradeList.addAll(performanceFluctuationList);

                    }
                    if (null != supplementAchievementList && supplementAchievementList.isEmpty()) {
                        for (WarningInformation gwi : gradeList) {
                            for (WarningInformation wi : supplementAchievementList) {
                                if (gwi.getJobNumber().equals(wi.getJobNumber())) {
                                    gwi.setAlarmSettingsId(as.getId());
                                    gwi.setWarningCondition(gwi.getWarningCondition() + "或" + wi.getWarningCondition());
                                    gwi.setWarningSource(gwi.getWarningSource() + ";" + wi.getWarningSource());
                                    supplementAchievementList.remove(wi);
                                }
                            }
                        }
                        gradeList.addAll(supplementAchievementList);

                    }
                    if (null != totalAchievementList && totalAchievementList.isEmpty()) {
                        for (WarningInformation gwi : gradeList) {
                            gwi.setAlarmSettingsId(as.getId());
                            gwi.setWarningLevel(as.getWarningLevel());
                            for (WarningInformation wi : totalAchievementList) {
                                if (gwi.getJobNumber().equals(wi.getJobNumber())) {
                                    gwi.setWarningCondition(gwi.getWarningCondition() + "或" + wi.getWarningCondition());
                                    gwi.setWarningSource(gwi.getWarningSource() + ";" + wi.getWarningSource());
                                    totalAchievementList.remove(wi);
                                }
                            }
                        }
                        gradeList.addAll(totalAchievementList);

                    }
                    if (null != leaveSchoolList && leaveSchoolList.isEmpty()) {
                        for (WarningInformation gwi : gradeList) {
                            gwi.setAlarmSettingsId(as.getId());
                            gwi.setWarningLevel(as.getWarningLevel());
                            for (WarningInformation wi : leaveSchoolList) {
                                if (gwi.getJobNumber().equals(wi.getJobNumber())) {
                                    gwi.setWarningCondition(gwi.getWarningCondition() + "或" + wi.getWarningCondition());
                                    gwi.setWarningSource(gwi.getWarningSource() + ";" + wi.getWarningSource());
                                    leaveSchoolList.remove(wi);
                                }
                            }
                        }
                        gradeList.addAll(leaveSchoolList);

                    }
                    if (null != cetList && cetList.isEmpty()) {
                        for (WarningInformation gwi : gradeList) {
                            gwi.setAlarmSettingsId(as.getId());
                            gwi.setWarningLevel(as.getWarningLevel());
                            for (WarningInformation wi : cetList) {
                                if (gwi.getJobNumber().equals(wi.getJobNumber())) {
                                    gwi.setWarningCondition(gwi.getWarningCondition() + "或" + wi.getWarningCondition());
                                    gwi.setWarningSource(gwi.getWarningSource() + ";" + wi.getWarningSource());
                                    cetList.remove(wi);
                                }
                            }
                        }
                        gradeList.addAll(cetList);
                    }

                }

            restHasMap.put(as.getWarningLevel(), gradeList);
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
