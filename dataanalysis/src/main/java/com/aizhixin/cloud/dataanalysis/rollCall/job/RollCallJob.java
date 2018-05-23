package com.aizhixin.cloud.dataanalysis.rollCall.job;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.aizhixin.cloud.dataanalysis.alertinformation.entity.WarningInformation;
import com.aizhixin.cloud.dataanalysis.alertinformation.service.AlertWarningInformationService;
import com.aizhixin.cloud.dataanalysis.common.constant.AlertTypeConstant;
import com.aizhixin.cloud.dataanalysis.common.constant.DataValidity;
import com.aizhixin.cloud.dataanalysis.common.constant.RollCallConstant;
import com.aizhixin.cloud.dataanalysis.common.constant.WarningTypeConstant;
import com.aizhixin.cloud.dataanalysis.rollCall.mongoEntity.RollCall;
import com.aizhixin.cloud.dataanalysis.rollCall.mongoEntity.RollCallCount;
import com.aizhixin.cloud.dataanalysis.rollCall.mongoRespository.RollCallCountMongoRespository;
import com.aizhixin.cloud.dataanalysis.rollCall.mongoRespository.RollCallMongoRespository;
import com.aizhixin.cloud.dataanalysis.setup.entity.RuleParameter;
import com.aizhixin.cloud.dataanalysis.setup.service.AlarmSettingsService;
import com.aizhixin.cloud.dataanalysis.setup.service.ProcessingModeService;

import com.aizhixin.cloud.dataanalysis.setup.service.RuleParameterService;
import com.aizhixin.cloud.dataanalysis.setup.service.WarningTypeService;
import org.apache.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.aizhixin.cloud.dataanalysis.setup.entity.AlarmSettings;

@Component
public class RollCallJob {

    public volatile static boolean flag = true;

    private Logger logger = Logger.getLogger(this.getClass());
    @Autowired
    @Lazy
    private AlarmSettingsService alarmSettingsService;
    @Autowired
    private RollCallMongoRespository rollCallMongoRespository;
    @Autowired
    private RollCallCountMongoRespository rollCallCountMongoRespository;
    @Autowired
    private AlertWarningInformationService alertWarningInformationService;
    @Autowired
    private RuleParameterService ruleParameterService;

    /**
     * 统计mongo里的本学期考勤数据将汇总的数据存入rollCallCount里
     */
    public void rollCallCountJob(int schoolYear, int semester) {

        // 获取预警配置
        List<AlarmSettings> settingsList = alarmSettingsService
                .getAlarmSettingsByType(WarningTypeConstant.Absenteeism.toString());
        if (null != settingsList && settingsList.size() > 0) {

//			Calendar c = Calendar.getInstance();
//			//当前年份
//			int schoolYear = c.get(Calendar.YEAR);
//			//当前月份
//			int month = c.get(Calendar.MONTH)+1;
//			//当前学期编号
//			int semester = 2;
//			if( month > 1 && month < 9){
//				semester = 1;
//			}
//			if(month == 1 ){
//				schoolYear = schoolYear - 1;
//			}
            HashMap<Long, Long> alarmMap = new HashMap<Long, Long>();
            // 按orgId归类告警等级阀值
            for (AlarmSettings settings : settingsList) {

                Long orgId = settings.getOrgId();
                alarmMap.put(orgId, orgId);
            }

            //清除之前考勤统计数据
            rollCallCountMongoRespository.deleteAll();
            Iterator iter = alarmMap.entrySet().iterator();
            while (iter.hasNext()) {

                List<RollCallCount> rollCallCountList = new ArrayList<RollCallCount>();
                HashMap<String, RollCallCount> rollCallCountMap = new HashMap<String, RollCallCount>();
                Map.Entry entry = (Map.Entry) iter.next();
                Long orgId = (Long) entry.getKey();

                List<RollCall> rollCallList = rollCallMongoRespository.findAllBySchoolYearAndSemesterAndOrgIdAndRollCallResult(schoolYear, semester, orgId, RollCallConstant.OUT_SCHOOL);
                for (RollCall rollCall : rollCallList) {
                    RollCallCount rollCallCount = rollCallCountMap.get(rollCall.getJobNum());
                    if (null == rollCallCount) {
                        rollCallCount = new RollCallCount();
                        rollCallCount.setClassId(rollCall.getClassId());
                        rollCallCount.setClassName(rollCall.getClassName());
                        rollCallCount.setCollegeId(rollCall.getCollegeId());
                        rollCallCount.setCollegeName(rollCall.getCollegeName());
                        rollCallCount.setGrade(rollCall.getGrade());
                        rollCallCount.setJobNum(rollCall.getJobNum());
                        rollCallCount.setOrgId(orgId);
                        rollCallCount.setProfessionalId(rollCall.getProfessionalId());
                        rollCallCount.setProfessionalName(rollCall.getProfessionalName());
                        rollCallCount.setSchoolYear(schoolYear);
                        rollCallCount.setSemester(semester);
                        rollCallCount.setUserId(rollCall.getUserId());
                        rollCallCount.setUserPhone(rollCall.getUserPhone());
                        rollCallCount.setUserName(rollCall.getUserName());
                        if (RollCallConstant.OUT_SCHOOL == rollCall.getRollCallResult()) {
                            rollCallCount.setOutSchoolTimes(1);
                        }
                        rollCallCountMap.put(rollCall.getJobNum(), rollCallCount);
                    } else {
                        if (RollCallConstant.OUT_SCHOOL == rollCall.getRollCallResult()) {
                            rollCallCount.setOutSchoolTimes(rollCallCount.getOutSchoolTimes() + 1);
                            rollCallCountMap.put(rollCall.getJobNum(), rollCallCount);
                        }
                    }
                }

                Iterator rollCalliter = rollCallCountMap.entrySet().iterator();
                while (rollCalliter.hasNext()) {
                    Map.Entry rollCallentry = (Map.Entry) rollCalliter.next();
                    RollCallCount rollCallCount = (RollCallCount) rollCallentry.getValue();
                    if (rollCallCount.getOutSchoolTimes() > 0) {
                        rollCallCountList.add(rollCallCount);
                    }
                }

                rollCallCountMongoRespository.save(rollCallCountList);
            }
        }
    }


    /**
     * 旷课预警（AbsenteeismEarlyWarning）
     */
    public ArrayList<WarningInformation> rollCallJob(Long orgId, int schoolYear, int semester, String ruleId) {

        ArrayList<WarningInformation> returnList = new ArrayList<WarningInformation>();

//		// 获取的预警类型
//		List<WarningType> warningTypeList = warningTypeService.getAllWarningTypeList();
//
//		//已经开启次预警类型的组织
//		Set<Long> orgIdSet = new HashSet<>();
//		for (WarningType wt : warningTypeList) {
//			if (wt.getSetupCloseFlag() == 10) {
//				orgIdSet.add(wt.getOrgId());
//			}
//		}

//		// 获取预警配置
//		List<AlarmSettings> settingsList = alarmSettingsService
//				.getAlarmSettingsByType(WarningTypeConstant.Absenteeism.toString());
//		if (null != settingsList && settingsList.size() > 0) {

//			Calendar c = Calendar.getInstance();
//			//当前年份
//			int schoolYear = c.get(Calendar.YEAR);
//			//当前月份
//			int month = c.get(Calendar.MONTH)+1;
//			//当前学期编号
//			int semester = 2;
//			if( month > 1 && month < 9){
//				semester = 1;
//			}
//			if(month == 1 ){
//				schoolYear = schoolYear - 1;
//			}
//			HashMap<Long, ArrayList<AlarmSettings>> alarmMap = new HashMap<Long, ArrayList<AlarmSettings>>();
//			Set<String> ruleIdList = new HashSet<String>();
//			Set<String> warnSettingsIdList = new HashSet<String>();
//			// 按orgId归类告警等级阀值
//			for (AlarmSettings settings : settingsList) {
//				if (orgIdSet.contains(settings.getOrgId())&&settings.getSetupCloseFlag()==10) {
//					warnSettingsIdList.add(settings.getId());
//					Long orgId = settings.getOrgId();
//
//					if (StringUtils.isEmpty(settings.getRuleSet())) {
//						continue;
//					}
//					String[] warmRuleIds = settings.getRuleSet().split(",");
//					for (String ruleId : warmRuleIds) {
//						if (!StringUtils.isEmpty(ruleId)) {
//							RuleParameter rp = ruleParameterService.findById(ruleId);
//							if(rp.getRuleName().equals(ruleName)) {
//								ruleIdList.add(ruleId);
//							}
//						}
//					}
//					if (null != alarmMap.get(orgId)) {
//						ArrayList<AlarmSettings> alarmList = alarmMap.get(orgId);
//						alarmList.add(settings);
//					} else {
//						ArrayList<AlarmSettings> alarmList = new ArrayList<AlarmSettings>();
//						alarmList.add(settings);
//						alarmMap.put(orgId, alarmList);
//					}
//				}
//			}

//			// 预警规则获取
//			HashMap<String, RuleParameter> ruleParameterMap = new HashMap<String, RuleParameter>();
//			List<RuleParameter> alarmList = ruleParameterService.getRuleParameterByIds(ruleIdList);
//			for (RuleParameter rp : alarmList) {
//				if(rp.getRuleName().equals(ruleName)) {
//					ruleParameterMap.put(rp.getId(), rp);
//				}
//			}
//			Iterator iter = alarmMap.entrySet().iterator();
//			while (iter.hasNext()) {

        //更新预警集合
        ArrayList<WarningInformation> alertInforList = new ArrayList<WarningInformation>();
        //撤销预警集合
        List<WarningInformation> removeAlertInforList = new ArrayList<WarningInformation>();
        //新增预警处理信息

        // 数据库已生成的处理中预警数据
        HashMap<String, WarningInformation> warnDbMap = new HashMap<String, WarningInformation>();
        // 定时任务产生的新的预警数据
        HashMap<String, WarningInformation> warnMap = new HashMap<String, WarningInformation>();
//				Map.Entry entry = (Map.Entry) iter.next();
//				Long orgId = (Long) entry.getKey();
//				ArrayList<AlarmSettings> val = (ArrayList<AlarmSettings>) entry
//						.getValue();

        // 预警处理配置获取
//				HashMap<String, ProcessingMode> processingModeMap = new HashMap<String, ProcessingMode>();
//				List<ProcessingMode> processingModeList = processingModeService
//						.getProcessingModeBywarningTypeId(orgId,
//								WarningType.Absenteeism.toString());
        // 按orgId查询未报到的学生信息
        List<RollCallCount> rollCallCountList = rollCallCountMongoRespository.findAllBySchoolYearAndSemesterAndOrgId(schoolYear, semester, orgId);

        // 数据库已生成的处理中预警数据
        List<WarningInformation> warnDbList = alertWarningInformationService
                .getWarnInforByState(orgId,
                        WarningTypeConstant.Absenteeism.toString(),
                        DataValidity.VALID.getState(),
                        AlertTypeConstant.ALERT_IN_PROCESS);
        for (WarningInformation warningInfor : warnDbList) {
            warnDbMap
                    .put(warningInfor.getJobNumber(), warningInfor);
        }

        if (null != rollCallCountList && rollCallCountList.size() > 0) {
            Date today = new Date();
            for (RollCallCount rollCallCount : rollCallCountList) {
                RuleParameter ruleParameter = ruleParameterService.findById(ruleId);
                if (null != ruleParameter) {
                    if (rollCallCount.getOutSchoolTimes() >= Float.parseFloat(ruleParameter.getRightParameter())) {
                        WarningInformation alertInfor = new WarningInformation();
                        String alertId = UUID.randomUUID()
                                .toString();
                        alertInfor.setId(alertId);
                        alertInfor.setDefendantId(rollCallCount
                                .getUserId());
                        alertInfor.setName(rollCallCount
                                .getUserName());
                        alertInfor.setJobNumber(rollCallCount
                                .getJobNum());
                        alertInfor.setCollogeId(rollCallCount
                                .getCollegeId());
                        alertInfor.setCollogeName(rollCallCount
                                .getCollegeName());
                        alertInfor.setClassId(rollCallCount
                                .getClassId());
                        alertInfor.setClassName(rollCallCount
                                .getClassName());
                        alertInfor
                                .setProfessionalId(rollCallCount
                                        .getProfessionalId());
                        alertInfor
                                .setProfessionalName(rollCallCount
                                        .getProfessionalName());
                        alertInfor.setTeacherYear(rollCallCount
                                .getSchoolYear());
                        alertInfor.setSemester(rollCallCount.getSemester());
//										alertInfor.setWarningLevel(alarmSettings
//												.getWarningLevel());
                        alertInfor
                                .setWarningState(AlertTypeConstant.ALERT_IN_PROCESS);
//										alertInfor.setAlarmSettingsId(asId
//												);
                        alertInfor
                                .setWarningType(WarningTypeConstant.Absenteeism
                                        .toString());
                        alertInfor.setWarningTime(new Date());
                        alertInfor.setWarningCondition("本学期累计旷课次数为:" + rollCallCount.getOutSchoolTimes());
                        alertInfor.setPhone(rollCallCount.getUserPhone());
                        alertInfor.setOrgId(ruleParameter.getOrgId());
                        alertInforList.add(alertInfor);
                        warnMap.put(alertInfor.getJobNumber(),
                                alertInfor);


                        break;
                    } else {
                        continue;
                    }
                }
            }
        }
        if (!alertInforList.isEmpty()) {

            // 数据库里有的预警更新预警信息(id和预警时间不变其他的信息以新的预警信息为主)
            for (WarningInformation warningInfor : alertInforList) {
                WarningInformation warnDbInfor = warnDbMap
                        .get(warningInfor.getJobNumber());
                if (null != warnDbInfor) {
                    warningInfor.setId(warnDbInfor.getId());
                    warningInfor.setWarningTime(warnDbInfor
                            .getWarningTime());
                }
            }
            // 数据库中存在新产生的预警不存在的预警直接系统处理成已处理状态
            for (WarningInformation warningDbInfor : warnDbList) {
                WarningInformation warnInfor = warnMap
                        .get(warningDbInfor.getJobNumber());
                if (null == warnInfor) {
                    warningDbInfor
                            .setWarningState(AlertTypeConstant.ALERT_PROCESSED);
                    removeAlertInforList.add(warningDbInfor);
                }
            }

//					alertWarningInformationService.save(alertInforList);
//					alertWarningInformationService.save(removeAlertInforList);
            returnList.addAll(alertInforList);
            returnList.addAll(removeAlertInforList);
        } else {
            //mongo中的报到数据都未产生预警信息则撤销数据库还处于处理中的预警信息
            removeAlertInforList = warnDbList;
            alertWarningInformationService.save(removeAlertInforList);
            //预警处理表同步处理为撤销状态
            returnList.addAll(removeAlertInforList);
        }
        return returnList;
    }

}
