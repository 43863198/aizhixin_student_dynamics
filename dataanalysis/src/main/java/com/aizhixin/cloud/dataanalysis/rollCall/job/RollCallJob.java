package com.aizhixin.cloud.dataanalysis.rollCall.job;

import java.util.ArrayList;
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
import com.aizhixin.cloud.dataanalysis.common.constant.WarningType;
import com.aizhixin.cloud.dataanalysis.common.util.DateUtil;
import com.aizhixin.cloud.dataanalysis.common.util.RestUtil;
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
import com.aizhixin.cloud.dataanalysis.studentRegister.mongoEntity.StudentRegister;
import com.aizhixin.cloud.dataanalysis.studentRegister.mongoRespository.StudentRegisterMongoRespository;

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
	private AlertWarningInformationService alertWarningInformationService;
	@Autowired
	private ProcessingModeService processingModeService;

//	public void rollCallJob() {
//
//		// 获取预警配置
//		List<AlarmSettings> settingsList = alarmSettingsService
//				.getAlarmSettingsByType(WarningType.Absenteeism.toString());
//		if (null != settingsList && settingsList.size() > 0) {
//			HashMap<Long, ArrayList<AlarmSettings>> alarmMap = new HashMap<Long, ArrayList<AlarmSettings>>();
//
//			Set<String> warnRuleIdList = new HashSet<String>();
//			Set<String> warnSettingsIdList = new HashSet<String>();
//			// 按orgId归类告警等级阀值
//			for (AlarmSettings settings : settingsList) {
//
//				warnSettingsIdList.add(settings.getId());
//				Long orgId = settings.getOrgId();
//
//				String[] warmRuleIds = settings.getRuleSet().split(",");
//				for (String warmRuleId : warmRuleIds) {
//					if (!StringUtils.isEmpty(warmRuleId)) {
//						warnRuleIdList.add(warmRuleId);
//					}
//				}
//				if (null != alarmMap.get(orgId)) {
//					ArrayList<AlarmSettings> alarmList = alarmMap.get(orgId);
//					alarmList.add(settings);
//				} else {
//					ArrayList<AlarmSettings> alarmList = new ArrayList<AlarmSettings>();
//					alarmList.add(settings);
//					alarmMap.put(orgId, alarmList);
//				}
//			}
//			// 预警规则获取
//			HashMap<String, AlarmRule> alarmRuleMap = new HashMap<String, AlarmRule>();
//			List<AlarmRule> alarmList = alarmRuleService
//					.getAlarmRuleByIds(warnRuleIdList);
//			for (AlarmRule alarmRule : alarmList) {
//				alarmRuleMap.put(alarmRule.getId(), alarmRule);
//			}
//
//			Iterator iter = alarmMap.entrySet().iterator();
//			while (iter.hasNext()) {
//
//				//更新预警集合
//				ArrayList<WarningInformation> alertInforList = new ArrayList<WarningInformation>();
//				//撤销预警集合
//				List<WarningInformation> removeAlertInforList = new ArrayList<WarningInformation>();
//				//新增预警处理信息
//				
//				// 数据库已生成的处理中预警数据
//				HashMap<String, WarningInformation> warnDbMap = new HashMap<String, WarningInformation>();
//				// 定时任务产生的新的预警数据
//				HashMap<String, WarningInformation> warnMap = new HashMap<String, WarningInformation>();
//				Map.Entry entry = (Map.Entry) iter.next();
//				Long orgId = (Long) entry.getKey();
//				ArrayList<AlarmSettings> val = (ArrayList<AlarmSettings>) entry
//						.getValue();
//
//				// 预警处理配置获取
//				HashMap<String, ProcessingMode> processingModeMap = new HashMap<String, ProcessingMode>();
//				List<ProcessingMode> processingModeList = processingModeService
//						.getProcessingModeBywarningTypeId(orgId,
//								WarningType.Absenteeism.toString());
//				// 按orgId查询未报到的学生信息
//				List<StudentRegister> stuRegisterList = stuRegisterMongoRespository
//						.findAllByOrgIdAndActualRegisterDateIsNull(orgId);
//				
//				// 数据库已生成的处理中预警数据
//				List<WarningInformation> warnDbList = alertWarningInformationService
//						.getWarnInforByState(orgId,
//								WarningType.Absenteeism.toString(),
//								DataValidity.VALID.getState(),
//								AlertTypeConstant.ALERT_IN_PROCESS);
//				for (WarningInformation warningInfor : warnDbList) {
//					warnDbMap
//							.put(warningInfor.getJobNumber(), warningInfor);
//				}
//
//				if (null != stuRegisterList && stuRegisterList.size() > 0) {
//					Date today = new Date();
//					for (StudentRegister studentRegister : stuRegisterList) {
//						int result = DateUtil.getDaysBetweenDate(
//								studentRegister.getRegisterDate(), today);
//						for (AlarmSettings alarmSettings : val) {
//							AlarmRule alarmRule = alarmRuleMap
//									.get(alarmSettings.getRuleSet());
//							if (null != alarmRule) {
//								if (result >= alarmRule.getRightParameter()) {
//									WarningInformation alertInfor = new WarningInformation();
//									String alertId = UUID.randomUUID()
//											.toString();
//									alertInfor.setId(alertId);
//									alertInfor.setDefendantId(studentRegister
//											.getUserId());
//									alertInfor.setName(studentRegister
//											.getUserName());
//									alertInfor.setJobNumber(studentRegister
//											.getJobNum());
//									alertInfor.setCollogeId(studentRegister
//											.getCollegeId());
//									alertInfor.setCollogeName(studentRegister
//											.getCollegeName());
//									alertInfor.setTeachingYear(studentRegister
//											.getGrade());
//									alertInfor.setClassId(studentRegister
//											.getClassId());
//									alertInfor.setClassName(studentRegister
//											.getClassName());
//									alertInfor
//											.setProfessionalId(studentRegister
//													.getProfessionalId());
//									alertInfor
//											.setProfessionalName(studentRegister
//													.getProfessionalName());
//									alertInfor.setTeachingYear(studentRegister
//											.getSchoolYear());
//									alertInfor.setWarningLevel(alarmSettings
//											.getWarningLevel());
//									alertInfor
//											.setWarningState(AlertTypeConstant.ALERT_IN_PROCESS);
//									alertInfor.setAlarmSettingsId(alarmSettings
//											.getId());
//									alertInfor
//											.setWarningType(WarningType.Absenteeism
//													.toString());
//									alertInfor.setWarningTime(new Date());
//									alertInfor.setOrgId(alarmRule.getOrgId());
//									alertInforList.add(alertInfor);
//									warnMap.put(alertInfor.getJobNumber(),
//											alertInfor);
//
//									// 生成预警处理表数据
//									if(null != warnDbMap.get(studentRegister.getJobNum())){
//										
//									}
//
//									break;
//								} else {
//									continue;
//								}
//							}
//						}
//					}
//				}
//				if (!alertInforList.isEmpty()) {
//
//					// 数据库里有的预警更新预警信息(id和预警时间不变其他的信息以新的预警信息为主)
//					for (WarningInformation warningInfor : alertInforList) {
//						WarningInformation warnDbInfor = warnDbMap
//								.get(warningInfor.getJobNumber());
//						if (null != warnDbInfor) {
//							warningInfor.setId(warnDbInfor.getId());
//							warningInfor.setWarningTime(warnDbInfor
//									.getWarningTime());
//						}
//					}
//					// 数据库中存在新产生的预警不存在的预警直接系统处理成已处理状态
//					for (WarningInformation warningDbInfor : warnDbList) {
//						WarningInformation warnInfor = warnMap
//								.get(warningDbInfor.getJobNumber());
//						if (null == warnInfor) {
//							warningDbInfor
//									.setWarningState(AlertTypeConstant.ALERT_PROCESSED);
//							removeAlertInforList.add(warningDbInfor);
//						}
//					}
//
//					alertWarningInformationService.save(alertInforList);
//					//
//
//					alertWarningInformationService.save(removeAlertInforList);
//				}else{
//					//mongo中的报到数据都未产生预警信息则撤销数据库还处于处理中的预警信息
//					removeAlertInforList = warnDbList;
//					alertWarningInformationService.save(removeAlertInforList);
//					//预警处理表同步处理为撤销状态
//				}
//			}
//		}
//	}

}
