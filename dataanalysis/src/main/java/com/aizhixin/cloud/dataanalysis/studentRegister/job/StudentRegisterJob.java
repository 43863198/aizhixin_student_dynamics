package com.aizhixin.cloud.dataanalysis.studentRegister.job;

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
import com.aizhixin.cloud.dataanalysis.alertinformation.service.AlertWarningInformationService;
import com.aizhixin.cloud.dataanalysis.common.constant.AlertTypeConstant;
import com.aizhixin.cloud.dataanalysis.common.constant.DataValidity;
import com.aizhixin.cloud.dataanalysis.common.constant.WarningTypeConstant;
import com.aizhixin.cloud.dataanalysis.common.util.DateUtil;
import com.aizhixin.cloud.dataanalysis.common.util.RestUtil;
import com.aizhixin.cloud.dataanalysis.setup.service.AlarmRuleService;
import com.aizhixin.cloud.dataanalysis.setup.service.AlarmSettingsService;
import com.aizhixin.cloud.dataanalysis.setup.service.ProcessingModeService;

import org.apache.log4j.Logger;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.aizhixin.cloud.dataanalysis.setup.entity.AlarmRule;
import com.aizhixin.cloud.dataanalysis.setup.entity.AlarmSettings;
import com.aizhixin.cloud.dataanalysis.studentRegister.mongoEntity.StudentRegister;
import com.aizhixin.cloud.dataanalysis.studentRegister.mongoRespository.StudentRegisterMongoRespository;

@Component
public class StudentRegisterJob {

	public volatile static boolean flag = true;

	private Logger logger = Logger.getLogger(this.getClass());
	@Autowired
	@Lazy
	private AlarmSettingsService alarmSettingsService;
	@Autowired
	private AlarmRuleService alarmRuleService;
	@Autowired
	private StudentRegisterMongoRespository stuRegisterMongoRespository;
	@Autowired
	private AlertWarningInformationService alertWarningInformationService;

	public void studenteRegisterJob() {

		// 获取预警配置
		List<AlarmSettings> settingsList = alarmSettingsService
				.getAlarmSettingsByType(WarningTypeConstant.Register.toString());
		if (null != settingsList && settingsList.size() > 0) {
			HashMap<Long, ArrayList<AlarmSettings>> alarmMap = new HashMap<Long, ArrayList<AlarmSettings>>();

			
			Calendar c = Calendar.getInstance();
			// 当前年份
			int schoolYear = c.get(Calendar.YEAR);
			// 当前月份
			int month = c.get(Calendar.MONTH);
			// 当前学期编号
			int semester = 2;
			if (month > 1 && month < 9) {
				semester = 1;
			}
			
			
			Set<String> warnRuleIdList = new HashSet<String>();
			Set<String> warnSettingsIdList = new HashSet<String>();
			// 按orgId归类告警等级阀值
			for (AlarmSettings settings : settingsList) {

				warnSettingsIdList.add(settings.getId());
				Long orgId = settings.getOrgId();

				if(StringUtils.isEmpty(settings.getRuleSet())){
					continue;
				}
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

				// 预警处理配置获取 暂不初始化预警处理信息 2017.11.28
//				HashMap<Integer, ProcessingMode> processingModeMap = new HashMap<Integer, ProcessingMode>();
//				List<ProcessingMode> processingModeList = processingModeService
//						.getProcessingModeBywarningTypeId(orgId,
//								WarningType.Register.toString());
//				for(ProcessingMode processingMode: processingModeList){
//					processingModeMap.put(processingMode.getWarningLevel(), processingMode);
//				}
				
				// 按orgId查询未报到的学生信息
				// List<StudentRegister> stuRegisterList =
				// stuRegisterMongoRespository
				// .findAllByOrgIdAndIsregister(key,StudentRegisterConstant.UNREGISTER);
				List<StudentRegister> stuRegisterList = stuRegisterMongoRespository
						.findAllByOrgIdAndActualRegisterDateIsNull(orgId);
				
				// 数据库已生成的处理中预警数据
				List<WarningInformation> warnDbList = alertWarningInformationService
						.getWarnInforByState(orgId,
								WarningTypeConstant.Register.toString(),
								DataValidity.VALID.getState(),
								AlertTypeConstant.ALERT_IN_PROCESS);
				for (WarningInformation warningInfor : warnDbList) {
					warnDbMap
							.put(warningInfor.getJobNumber(), warningInfor);
				}

				if (null != stuRegisterList && stuRegisterList.size() > 0) {
					Date today = new Date();
					for (StudentRegister studentRegister : stuRegisterList) {
						int result = DateUtil.getDaysBetweenDate(
								studentRegister.getRegisterDate(), today);
						for (AlarmSettings alarmSettings : val) {
							AlarmRule alarmRule = alarmRuleMap
									.get(alarmSettings.getRuleSet());
							if (null != alarmRule) {
								if (result >= Float.parseFloat(alarmRule.getRightParameter())) {
									WarningInformation alertInfor = new WarningInformation();
									String alertId = UUID.randomUUID()
											.toString();
									alertInfor.setId(alertId);
									alertInfor.setDefendantId(studentRegister
											.getUserId());
									alertInfor.setName(studentRegister
											.getUserName());
									alertInfor.setJobNumber(studentRegister
											.getJobNum());
									alertInfor.setCollogeId(studentRegister
											.getCollegeId());
									alertInfor.setCollogeName(studentRegister
											.getCollegeName());
									alertInfor.setClassId(studentRegister
											.getClassId());
									alertInfor.setClassName(studentRegister
											.getClassName());
									alertInfor
											.setProfessionalId(studentRegister
													.getProfessionalId());
									alertInfor
											.setProfessionalName(studentRegister
													.getProfessionalName());
									alertInfor.setSemester(semester);
									alertInfor.setTeacherYear(schoolYear);
									alertInfor.setWarningLevel(alarmSettings
											.getWarningLevel());
									alertInfor
											.setWarningState(AlertTypeConstant.ALERT_IN_PROCESS);
									alertInfor.setAlarmSettingsId(alarmSettings
											.getId());
									alertInfor
											.setWarningType(WarningTypeConstant.Register
													.toString());
									alertInfor.setWarningCondition("报到注册截至时间超过天数："+result);
									alertInfor.setWarningTime(new Date());
									alertInfor.setPhone(studentRegister.getUserPhone());
									alertInfor.setOrgId(alarmRule.getOrgId());
									alertInforList.add(alertInfor);
									warnMap.put(alertInfor.getJobNumber(),
											alertInfor);

									//新预警生成预警处理表数据  暂不初始化预警处理信息 2017.11.28
//									if(null == warnDbMap.get(studentRegister.getJobNum())){
//										ProcessingMode processingMode = processingModeMap.get(alarmSettings
//											.getWarningLevel());
//										if(null != processingMode){
//											
//										}
//									}
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

//	 @Scheduled(cron = "0 0/3 * * * ?")
	public void studenteRegister() {
		this.setAlertWarningInformation(218L);
	}

	public void setAlertWarningInformation(Long orgId) {
		String url = "http://gateway.aizhixintest.com/org-manager";
		url = url + "/v1/students/list?pageSize=1000&orgId=" + orgId;
		List<WarningInformation> awinfoList = new ArrayList<>();
		try {
			String respone = new RestUtil().getData(url, null);
			if (null != respone) {
				JSONObject json = JSONObject.fromObject(respone);
				Object data = json.get("data");
				if (null != data) {
					JSONArray jsonArray = JSONArray.fromObject(data);
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject jsonObject = jsonArray.getJSONObject(i);
						Long defendantId = jsonObject.getLong("id");
						String name = jsonObject.getString("name");
						String jobNumber = jsonObject.getString("jobNumber");
						String collegeName = jsonObject
								.getString("collegeName");
						Long collegeId = jsonObject.getLong("collegeId");
						String professionalName = jsonObject
								.getString("professionalName");
						Long professionalId = jsonObject
								.getLong("professionalId");
						String classesName = jsonObject
								.getString("classesName");
						Long classesId = jsonObject.getLong("classesId");
						String phone = jsonObject.getString("phone");
						String teachingYear = jsonObject
								.getString("teachingYear");
						String warningType = "";
						if (i % 2 == 0) {
							warningType = "Register";
						} else if (i % 3 == 0) {
							warningType = "LeaveSchool";
						} else if (i % 5 == 0) {
							warningType = "AttendAbnormal";
						} else if (i % 7 == 0) {
							warningType = "Absenteeism";
						} else if (i % 9 == 0) {
							warningType = "TotalAchievement";
						} else if (i % 11 == 0) {
							warningType = "SupplementAchievement";
						} else if (i % 13 == 0) {
							warningType = "PerformanceFluctuation";
						} else {
							warningType = "Cet";
						}
						WarningInformation awinfo = null;
						List<WarningInformation> awinfos = alertWarningInformationService
								.getawinfoByDefendantId(orgId, warningType,
										defendantId);
						if (null != awinfos) {
							if (awinfos.size() == 1) {
								awinfo = awinfos.get(0);
							} else if (awinfos.size() > 1) {
								logger.info("学号为"
										+ defendantId
										+ "学生的"
										+ WarningTypeConstant.valueOf(warningType)
												.getValue() + "数据重复异常！");
								continue;
							} else {
								awinfo = new WarningInformation();
							}
						} else {
							awinfo = new WarningInformation();
						}
						awinfo.setOrgId(orgId);
						if (null != defendantId) {
							awinfo.setDefendantId(defendantId);
						}
						if (null != name) {
							awinfo.setName(name);
						}
						if (null != jobNumber) {
							awinfo.setJobNumber(jobNumber);
						}
						if (null != collegeName) {
							awinfo.setCollogeName(collegeName);
						}
						if (null != collegeId) {
							awinfo.setCollogeId(collegeId);
						}
						if (null != professionalName) {
							awinfo.setProfessionalName(professionalName);
						}
						if (null != professionalId) {
							awinfo.setProfessionalId(professionalId);
						}
						if (null != classesName) {
							awinfo.setClassName(classesName);
						}
						if (null != classesId) {
							awinfo.setClassId(classesId);
						}
						if (null != phone) {
							awinfo.setPhone(phone);
						}
						if (null != teachingYear) {
							awinfo.setTeacherYear(Integer.parseInt(teachingYear));
						}
						if (i % 77 == 0) {
							awinfo.setWarningLevel(1);
						} else if (i % 55 == 0) {
							awinfo.setWarningLevel(2);
						} else {
							awinfo.setWarningLevel(3);
						}
						if (i % 9 == 0) {
							awinfo.setWarningState(20);
						} else {
							awinfo.setWarningState(10);
						}
						awinfo.setWarningType(warningType);
						awinfo.setCreatedDate(new Date());
						awinfoList.add(awinfo);
					}
					alertWarningInformationService.save(awinfoList);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
