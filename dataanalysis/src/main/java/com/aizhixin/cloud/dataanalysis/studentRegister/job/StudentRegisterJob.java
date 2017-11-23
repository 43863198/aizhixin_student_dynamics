package com.aizhixin.cloud.dataanalysis.studentRegister.job;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.aizhixin.cloud.dataanalysis.alertinformation.entity.WarningInformation;
import com.aizhixin.cloud.dataanalysis.alertinformation.repository.AlertWarningInformationRepository;

import com.aizhixin.cloud.dataanalysis.alertinformation.service.AlertWarningInformationService;
import com.aizhixin.cloud.dataanalysis.common.constant.WarningType;
import com.aizhixin.cloud.dataanalysis.common.util.RestUtil;
import com.aizhixin.cloud.dataanalysis.setup.respository.AlarmSettingsRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.log4j.Logger;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.aizhixin.cloud.dataanalysis.setup.entity.AlarmSettings;

@Component
public class StudentRegisterJob {
	public volatile static boolean flag = true;
	private Logger logger = Logger.getLogger(this.getClass());
	@Autowired
	private AlarmSettingsRepository alarmSettingsRepository;
	@Autowired
	private RestUtil restUtil;
	@Autowired
	private AlertWarningInformationService  alertWarningInformationService;
	@Autowired
	private AlertWarningInformationRepository  alertWarningInformationRepository;

//	@Scheduled(cron = "0 0/5 * * * ?")
//	public void studenteRegister() {
//        //R为学生注册预警
//		List<AlarmParameter> alarmParams = alarmParameterRespository
//				.findAllByDeleteFlagAndAlarmSettings_WarningType(DataValidity.VALID.getState(), "R");
//固化规则咱不做规则管理和查询
//		List<AlarmParameter> alarmParams = alarmParameterRespository
//				.findAllByDeleteFlagAndAlarmSettings_SetupCloseFlagAndAlarmSettings_TypeOrderByAlarmSettings_idAscLevelDesc(DataValidity.VALID.getState(),AlertTypeConstant.SETUP_ALERTSETTING,
//						AlertTypeConstant.STUDENT_REGISTER);

//		List<AlarmParameter> alarmParams = new ArrayList<AlarmParameter>();

		//固化报到注册规则，之后要改成从数据库获取
//		AlarmSettings aSetting = new AlarmSettings();
//		aSetting.setOrgId(1L);
//		AlarmParameter alaParam = new AlarmParameter();
//		alaParam.setLevel(1);
//		alaParam.setSetParameter(11);
//		alaParam.setAlarmSettings(aSetting);
//		alarmParams.add(alaParam);
//		aSetting = new AlarmSettings();
//		aSetting.setWarningType("Register");
//		aSetting.setOrgId(1L);
//		alaParam = new AlarmParameter();
//		alaParam.setLevel(1);
//		alaParam.setSetParameter(5);
//		alaParam.setAlarmSettings(aSetting);
//		alarmParams.add(alaParam);
//		aSetting = new AlarmSettings();
//		aSetting.setWarningType("Register");
//		aSetting.setOrgId(1L);
//		 alaParam = new AlarmParameter();
//		alaParam.setLevel(1);
//		alaParam.setSetParameter(1);
//		alaParam.setAlarmSettings(aSetting);
//		alarmParams.add(alaParam);
		
//		if (null != alarmParams && alarmParams.size() > 0) {
//			HashMap<Long, ArrayList<AlarmParameter>> alarmMap = new HashMap<Long, ArrayList<AlarmParameter>>();
//			ArrayList<AlertWarningInformation> alertInforList = new ArrayList<AlertWarningInformation>();
//			//按orgId归类告警等级阀值
//			for (AlarmParameter param : alarmParams) {
//				Long orgId = param.getAlarmSettings().getOrgId();
//				if (null != alarmMap.get(orgId)) {
//					ArrayList<AlarmParameter> alarmList = alarmMap.get(orgId);
//					alarmList.add(param);
//				} else {
//					ArrayList<AlarmParameter> alarmList = new ArrayList<AlarmParameter>();
//					alarmList.add(param);
//					alarmMap.put(orgId, alarmList);
//				}
//			}
//			Iterator iter = alarmMap.entrySet().iterator();
//			Date today = new Date();
//			while (iter.hasNext()) {
//				Map.Entry entry = (Map.Entry) iter.next();
//				Long key = (Long)entry.getKey();
//				ArrayList<AlarmParameter> val = (ArrayList<AlarmParameter>)entry.getValue();
//				//按orgId查询未报到的学生信息
//				List<StudentRegister> stuRegisterList = stuRegisterMongoRespository
//						.findAllByOrgIdAndIsregister(key,StudentRegisterConstant.UNREGISTER);
//				for(StudentRegister studentRegister : stuRegisterList){
//					int result = DateUtil.getDaysBetweenDate(studentRegister.getRegisterDate(), today);
//					for(AlarmParameter alarmParam : val){
//						if(result >= alarmParam.getSetParameter()){
//							AlertWarningInformation alertInfor = new AlertWarningInformation();
//							alertInfor.setDefendantId(studentRegister.getStuId());
//							alertInfor.setName(studentRegister.getStuName());
//							alertInfor.setJobNumber(studentRegister.getJobNum());
//							alertInfor.setCollogeId(studentRegister.getCollegeId());
//							alertInfor.setCollogeName(studentRegister.getCollegeName());
//							alertInfor.setTeachingYear(studentRegister.getGrade());
//							alertInfor.setClassId(studentRegister.getClassId());
//							alertInfor.setClassName(studentRegister.getClassName());
//							alertInfor.setProfessionalId(studentRegister.getProfessionalId());
//							alertInfor.setProfessionalName(studentRegister.getProfessionalName());
//							alertInfor.setTeachingYear(studentRegister.getSchoolYear());
//							alertInfor.setWarningLevel(alarmParam.getLevel());
//							alertInfor.setWarningState(10);
//							alertInfor.setWarningType("Register");
//							alertInfor.setWarningTime(new Date());
//							alertInfor.setOrgId(alarmParam.getAlarmSettings().getOrgId());
//							alertInforList.add(alertInfor);
//							break;
//						}else{
//							continue;
//						}
//					}
//				}
//			}
//
//			if(!alertInforList.isEmpty()){
//				alertWarningInformationRepository.save(alertInforList);
//			}
//		}
//	}


	@Scheduled(cron = "0 0/50 * * * ?")
	public void studenteRegister() {
		this.setAlertWarningInformation(213L);
	}

	public  void setWarningType(Long orgId){
		List<AlarmSettings> alarmSettingsList = new ArrayList<>();
		AlarmSettings alarmSettings1 = new AlarmSettings();
		alarmSettings1.setWarningType("Register");
		alarmSettings1.setWarningStandard("延迟报道注册天数5天以内为三级预警，延迟11天内为二级预警，延迟大于13天为一级预警");
		alarmSettings1.setWarningCondition("延迟报道注册天数5天以内为三级预警，延迟11天内为二级预警，延迟大于14天为一级预警");
		alarmSettings1.setOrgId(orgId);
		alarmSettings1.setCreatedDate(new Date());
		alarmSettings1.setSetupCloseFlag(10);
		alarmSettingsList.add(alarmSettings1);
		AlarmSettings alarmSettings2 = new AlarmSettings();
		alarmSettings2.setWarningType("LeaveSchool");
		alarmSettings2.setWarningStandard("退学预警标准");
		alarmSettings2.setWarningCondition("退学预警标准条件");
		alarmSettings2.setOrgId(orgId);
		alarmSettings2.setCreatedDate(new Date());
		alarmSettings2.setSetupCloseFlag(10);
		alarmSettingsList.add(alarmSettings2);
		AlarmSettings alarmSettings3 = new AlarmSettings();
		alarmSettings3.setWarningType("AttendAbnormal");
		alarmSettings3.setWarningStandard("修读异常预警标准");
		alarmSettings3.setWarningCondition("修读异常预警条件");
		alarmSettings3.setOrgId(orgId);
		alarmSettings3.setCreatedDate(new Date());
		alarmSettings3.setSetupCloseFlag(10);
		alarmSettingsList.add(alarmSettings3);
		AlarmSettings alarmSettings4 = new AlarmSettings();
		alarmSettings4.setWarningType("AttendAbnormal");
		alarmSettings4.setWarningStandard("旷课预警标准");
		alarmSettings4.setWarningCondition("旷课预警条件");
		alarmSettings4.setOrgId(orgId);
		alarmSettings4.setCreatedDate(new Date());
		alarmSettings4.setSetupCloseFlag(10);
		alarmSettingsList.add(alarmSettings4);
		AlarmSettings alarmSettings5 = new AlarmSettings();
		alarmSettings5.setWarningType("TotalAchievement");
		alarmSettings5.setWarningStandard("总评成绩预警标准");
		alarmSettings5.setWarningCondition("总评成绩预警条件");
		alarmSettings5.setOrgId(orgId);
		alarmSettings5.setCreatedDate(new Date());
		alarmSettings5.setSetupCloseFlag(10);
		alarmSettingsList.add(alarmSettings5);
		AlarmSettings alarmSettings7 = new AlarmSettings();
		alarmSettings7.setWarningType("SupplementAchievement");
		alarmSettings7.setWarningStandard("补考成绩预警标准");
		alarmSettings7.setWarningCondition("补考成绩预警条件");
		alarmSettings7.setOrgId(orgId);
		alarmSettings7.setCreatedDate(new Date());
		alarmSettings7.setSetupCloseFlag(10);
		alarmSettingsList.add(alarmSettings7);
		AlarmSettings alarmSettings8 = new AlarmSettings();
		alarmSettings8.setWarningType("PerformanceFluctuation");
		alarmSettings8.setWarningStandard("成绩波动预警标准");
		alarmSettings8.setWarningCondition("成绩波动预警条件");
		alarmSettings8.setOrgId(orgId);
		alarmSettings8.setCreatedDate(new Date());
		alarmSettings8.setSetupCloseFlag(10);
		alarmSettingsList.add(alarmSettings8);
		AlarmSettings alarmSettings9 = new AlarmSettings();
		alarmSettings9.setWarningType("Cet");
		alarmSettings9.setWarningStandard("学生四六级英语预警标准");
		alarmSettings9.setWarningCondition("学生四六级英语预警条件");
		alarmSettings9.setOrgId(orgId);
		alarmSettings9.setCreatedDate(new Date());
		alarmSettings9.setSetupCloseFlag(10);
		alarmSettingsList.add(alarmSettings9);
		alarmSettingsRepository.save(alarmSettingsList);

	}

	public void setAlertWarningInformation(Long orgId){
		String url = "http://gateway.aizhixintest.com/org-manager";
		url = url+"/v1/students/list?pageSize=1000&orgId="+orgId;
		List<WarningInformation> awinfoList = new ArrayList<>();
		try {
			String respone = new RestUtil().getData(url, null);
			if(null!=respone){
				JSONObject json = JSONObject.fromObject(respone);
				Object data = json.get("data");
				if(null!=data){
					JSONArray jsonArray = JSONArray.fromObject(data);
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject jsonObject = jsonArray.getJSONObject(i);
						Long defendantId = jsonObject.getLong("id");
						String name = jsonObject.getString("name");
						String jobNumber = jsonObject.getString("jobNumber");
						String collegeName = jsonObject.getString("collegeName");
						Long collegeId = jsonObject.getLong("collegeId");
						String professionalName = jsonObject.getString("professionalName");
						Long professionalId = jsonObject.getLong("professionalId");
						String classesName = jsonObject.getString("classesName");
						Long classesId = jsonObject.getLong("classesId");
						String phone = jsonObject.getString("phone");
						String teachingYear = jsonObject.getString("teachingYear");
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
						List<WarningInformation> awinfos = alertWarningInformationService.getawinfoByDefendantId(orgId,warningType,defendantId);
						if(null!=awinfos){
							if(awinfos.size()==1){
								awinfo = awinfos.get(0);
							}else if(awinfos.size()>1) {
								logger.info("学号为" + defendantId + "学生的" + WarningType.valueOf(warningType).getValue() + "数据重复异常！");
								continue;
							}else {
								awinfo = new WarningInformation();
							}
						}else {
							awinfo = new WarningInformation();
						}
						awinfo.setOrgId(orgId);
						if(null!=defendantId) {
							awinfo.setDefendantId(defendantId);
						}
						if(null!=name){
							awinfo.setName(name);
						}
						if(null!=jobNumber){
							awinfo.setJobNumber(jobNumber);
						}
						if(null!=collegeName){
							awinfo.setCollogeName(collegeName);
						}
						if(null!=collegeId){
							awinfo.setCollogeId(collegeId);
						}
						if(null!=professionalName){
							awinfo.setProfessionalName(professionalName);
						}
						if(null!=professionalId){
							awinfo.setProfessionalId(professionalId);
						}
						if(null!=classesName){
							awinfo.setClassName(classesName);
						}
						if(null!=classesId){
							awinfo.setClassId(classesId);
						}
						if(null!=phone){
							awinfo.setPhone(phone);
						}
						if(null!=teachingYear){
							awinfo.setTeachingYear(teachingYear);
						}
						if(i%77==0) {
							awinfo.setWarningLevel(1);
						}else if(i%55==0) {
							awinfo.setWarningLevel(2);
						}else {
							awinfo.setWarningLevel(3);
						}
						if(i%9==0){
							awinfo.setWarningState(20);
						}else {
							awinfo.setWarningState(10);
						}
						awinfo.setWarningType(warningType);
						awinfo.setCreatedDate(new Date());
						awinfoList.add(awinfo);
					}
					alertWarningInformationRepository.save(awinfoList);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
