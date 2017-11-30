package com.aizhixin.cloud.dataanalysis.rollCall.job;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import com.aizhixin.cloud.dataanalysis.alertinformation.entity.WarningInformation;
import com.aizhixin.cloud.dataanalysis.alertinformation.repository.AlertWarningInformationRepository;
import com.aizhixin.cloud.dataanalysis.alertinformation.service.AlertWarningInformationService;
import com.aizhixin.cloud.dataanalysis.common.constant.AlertTypeConstant;
import com.aizhixin.cloud.dataanalysis.common.constant.DataValidity;
import com.aizhixin.cloud.dataanalysis.common.constant.RollCallConstant;
import com.aizhixin.cloud.dataanalysis.common.constant.WarningType;
import com.aizhixin.cloud.dataanalysis.common.util.DateUtil;
import com.aizhixin.cloud.dataanalysis.common.util.RestUtil;
import com.aizhixin.cloud.dataanalysis.rollCall.mongoEntity.RollCall;
import com.aizhixin.cloud.dataanalysis.rollCall.mongoEntity.RollCallCount;
import com.aizhixin.cloud.dataanalysis.rollCall.mongoRespository.RollCallCountMongoRespository;
import com.aizhixin.cloud.dataanalysis.rollCall.mongoRespository.RollCallMongoRespository;
import com.aizhixin.cloud.dataanalysis.setup.service.AlarmRuleService;
import com.aizhixin.cloud.dataanalysis.setup.service.AlarmSettingsService;
import com.aizhixin.cloud.dataanalysis.setup.service.ProcessingModeService;

import org.apache.log4j.Logger;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.aizhixin.cloud.dataanalysis.setup.entity.AlarmRule;
import com.aizhixin.cloud.dataanalysis.setup.entity.AlarmSettings;
import com.aizhixin.cloud.dataanalysis.setup.entity.ProcessingMode;

@Component
public class RollCallJob {

	public volatile static boolean flag = true;

	private Logger logger = Logger.getLogger(this.getClass());
	@Autowired
	private AlarmSettingsService alarmSettingsService;
	@Autowired
	private AlarmRuleService alarmRuleService;
	@Autowired
	private RollCallMongoRespository rollCallMongoRespository;
	@Autowired
	private RollCallCountMongoRespository rollCallCountMongoRespository;
	@Autowired
	private AlertWarningInformationService alertWarningInformationService;
	@Autowired
	private ProcessingModeService processingModeService;
	
	/**
	 * 统计mongo里的本学期考勤数据将汇总的数据存入rollCallCount里
	 */
	public void rollCallCountJob() {

		// 获取预警配置
		List<AlarmSettings> settingsList = alarmSettingsService
				.getAlarmSettingsByType(WarningType.Absenteeism.toString());
		if (null != settingsList && settingsList.size() > 0) {
			
			Calendar c = Calendar.getInstance();
			//当前年份
			int schoolYear = c.get(Calendar.YEAR);
			//当前月份
			int month = c.get(Calendar.MONTH);
			//当前学期编号
			int semester = 2;
			if( month > 2 && month < 9){
				semester = 1;
			}
			
			HashMap<Long, Long> alarmMap = new HashMap<Long,Long>();
			// 按orgId归类告警等级阀值
			for (AlarmSettings settings : settingsList) {

				Long orgId = settings.getOrgId();
				alarmMap.put(orgId, orgId);
			}

			//清除之前考勤统计数据
//			rollCallCountMongoRespository.deleteAll();
			Iterator iter = alarmMap.entrySet().iterator();
			while (iter.hasNext()) {

				List<RollCallCount> rollCallCountList = new ArrayList<RollCallCount>();
				HashMap<String,RollCallCount> rollCallCountMap = new HashMap<String,RollCallCount>();
				Map.Entry entry = (Map.Entry) iter.next();
				Long orgId = (Long) entry.getKey();

				List<RollCall> rollCallList = rollCallMongoRespository.findAllBySchoolYearAndSemesterAndOrgId(schoolYear, semester, orgId);
				for(RollCall rollCall : rollCallList){
					RollCallCount rollCallCount = rollCallCountMap.get(rollCall.getJobNum());
					if(null == rollCallCount){
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
						rollCallCount.setUserName(rollCall.getUserName());
						if(RollCallConstant.OUT_SCHOOL == rollCall.getRollCallResult()){
							rollCallCount.setOutSchoolTimes(1);
						}
						rollCallCountMap.put(rollCall.getJobNum(), rollCallCount);
					}else{
						if(RollCallConstant.OUT_SCHOOL == rollCall.getRollCallResult()){
							rollCallCount.setOutSchoolTimes(rollCallCount.getOutSchoolTimes()+1);
							rollCallCountMap.put(rollCall.getJobNum(), rollCallCount);
						}
					}
				}
				
				Iterator rollCalliter = rollCallCountMap.entrySet().iterator();
				while (rollCalliter.hasNext()) {
					Map.Entry rollCallentry = (Map.Entry) rollCalliter.next();
					RollCallCount rollCallCount = (RollCallCount)rollCallentry.getValue();
					if(rollCallCount.getOutSchoolTimes() > 0){
						rollCallCountList.add(rollCallCount);
					}
				}
				
				rollCallCountMongoRespository.save(rollCallCountList);
			}
		}
	}

	public void rollCallJob() {

		// 获取预警配置
		List<AlarmSettings> settingsList = alarmSettingsService
				.getAlarmSettingsByType(WarningType.Absenteeism.toString());
		if (null != settingsList && settingsList.size() > 0) {
			
			Calendar c = Calendar.getInstance();
			//当前年份
			int schoolYear = c.get(Calendar.YEAR);
			//当前月份
			int month = c.get(Calendar.MONTH);
			//当前学期编号
			int semester = 2;
			if( month > 2 && month < 9){
				semester = 1;
			}
			
			HashMap<Long, ArrayList<AlarmSettings>> alarmMap = new HashMap<Long, ArrayList<AlarmSettings>>();
			Set<String> warnRuleIdList = new HashSet<String>();
			Set<String> warnSettingsIdList = new HashSet<String>();
			// 按orgId归类告警等级阀值
			for (AlarmSettings settings : settingsList) {

				warnSettingsIdList.add(settings.getId());
				Long orgId = settings.getOrgId();

				String[] warmRuleIds = settings.getRuleSet().split(",");
				for (String warmRuleId : warmRuleIds) {
					if (!StringUtils.isEmpty(warmRuleId)) {
						warnRuleIdList.add(warmRuleId);
					}
				}
				if (null != alarmMap.get(orgId)) {
					ArrayList<AlarmSettings> alarmList = alarmMap.get(orgId);
					alarmList.add(settings);
				} else {
					ArrayList<AlarmSettings> alarmList = new ArrayList<AlarmSettings>();
					alarmList.add(settings);
					alarmMap.put(orgId, alarmList);
				}
			}
			// 预警规则获取
			HashMap<String, AlarmRule> alarmRuleMap = new HashMap<String, AlarmRule>();
			List<AlarmRule> alarmList = alarmRuleService
					.getAlarmRuleByIds(warnRuleIdList);
			for (AlarmRule alarmRule : alarmList) {
				alarmRuleMap.put(alarmRule.getId(), alarmRule);
			}

			Iterator iter = alarmMap.entrySet().iterator();
			while (iter.hasNext()) {

				//更新预警集合
				ArrayList<WarningInformation> alertInforList = new ArrayList<WarningInformation>();
				//撤销预警集合
				List<WarningInformation> removeAlertInforList = new ArrayList<WarningInformation>();
				//新增预警处理信息
				
				// 数据库已生成的处理中预警数据
				HashMap<String, WarningInformation> warnDbMap = new HashMap<String, WarningInformation>();
				// 定时任务产生的新的预警数据
				HashMap<String, WarningInformation> warnMap = new HashMap<String, WarningInformation>();
				Map.Entry entry = (Map.Entry) iter.next();
				Long orgId = (Long) entry.getKey();
				ArrayList<AlarmSettings> val = (ArrayList<AlarmSettings>) entry
						.getValue();

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
								WarningType.Absenteeism.toString(),
								DataValidity.VALID.getState(),
								AlertTypeConstant.ALERT_IN_PROCESS);
				for (WarningInformation warningInfor : warnDbList) {
					warnDbMap
							.put(warningInfor.getJobNumber(), warningInfor);
				}

				if (null != rollCallCountList && rollCallCountList.size() > 0) {
					Date today = new Date();
					for (RollCallCount rollCallCount : rollCallCountList) {
						for (AlarmSettings alarmSettings : val) {
							AlarmRule alarmRule = alarmRuleMap
									.get(alarmSettings.getRuleSet());
							if (null != alarmRule) {
								if (rollCallCount.getOutSchoolTimes() > alarmRule.getRightParameter()) {
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
									alertInfor.setTeachingYear(String.valueOf(rollCallCount
											.getSchoolYear()));
									alertInfor.setWarningLevel(alarmSettings
											.getWarningLevel());
									alertInfor
											.setWarningState(AlertTypeConstant.ALERT_IN_PROCESS);
									alertInfor.setAlarmSettingsId(alarmSettings
											.getId());
									alertInfor
											.setWarningType(WarningType.Absenteeism
													.toString());
									alertInfor.setWarningTime(new Date());
									alertInfor.setOrgId(alarmRule.getOrgId());
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

					alertWarningInformationService.save(alertInforList);
					//

					alertWarningInformationService.save(removeAlertInforList);
				}else{
					//mongo中的报到数据都未产生预警信息则撤销数据库还处于处理中的预警信息
					removeAlertInforList = warnDbList;
					alertWarningInformationService.save(removeAlertInforList);
					//预警处理表同步处理为撤销状态
				}
			}
		}
	}

}