package com.aizhixin.cloud.dataanalysis.studentRegister.job;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.aizhixin.cloud.dataanalysis.alertinformation.entity.AlertWarningInformation;
import com.aizhixin.cloud.dataanalysis.alertinformation.repository.AlertWarningInformationRepository;
import com.aizhixin.cloud.dataanalysis.alertinformation.service.AlertWarningInformationService;
import com.aizhixin.cloud.dataanalysis.common.constant.DataValidity;
import com.aizhixin.cloud.dataanalysis.common.constant.StudentRegisterConstant;
import com.aizhixin.cloud.dataanalysis.common.constant.WarningType;
import com.aizhixin.cloud.dataanalysis.common.util.DateUtil;
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

import com.aizhixin.cloud.dataanalysis.setup.entity.AlarmParameter;
import com.aizhixin.cloud.dataanalysis.setup.entity.AlarmSettings;
import com.aizhixin.cloud.dataanalysis.studentRegister.mongoEntity.StudentRegister;
import com.aizhixin.cloud.dataanalysis.studentRegister.mongoRespository.StudentRegisterMongoRespository;

@Component
public class StudentRegisterJob {
	public volatile static boolean flag = true;
	private Logger logger = Logger.getLogger(this.getClass());
	@Autowired
	private AlarmSettingsRepository alarmSettingsRepository;
	@Autowired
	private RestUtil restUtil;
	@Autowired
	private StudentRegisterMongoRespository stuRegisterMongoRespository;
	@Autowired
	private AlertWarningInformationService  alertWarningInformationService;
	@Autowired
	private AlertWarningInformationRepository  alertWarningInformationRepository;

	@Scheduled(cron = "0 0/5 * * * ?")
	public void studenteRegisterJob() {
        //R为学生注册预警
//		List<AlarmParameter> alarmParams = alarmParameterRespository
//				.findAllByDeleteFlagAndAlarmSettings_WarningType(DataValidity.VALID.getState(), WarningType.Register.getValue());
		//固化规则咱不做规则管理和查询
//		List<AlarmParameter> alarmParams = alarmParameterRespository
//				.findAllByDeleteFlagAndAlarmSettings_SetupCloseFlagAndAlarmSettings_TypeOrderByAlarmSettings_idAscLevelDesc(DataValidity.VALID.getState(),AlertTypeConstant.SETUP_ALERTSETTING,
//						AlertTypeConstant.STUDENT_REGISTER);

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
//							alertInfor.setWarningType(WarningType.Register.getValue());
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
	}


	@Scheduled(cron = "0 0/30 * * * ?")
	public void studenteRegister() {
		if(flag) {
			this.setAlertWarningInformation(213L);
		}else {
			logger.info("处理中！");
		}
	}

	public  void setWarningType(Long orgId, String warningType){
//		AlarmSettings alarmSettings = alarmSettingsRepository.getAlarmSettingsByOrgId(orgId, warningType, DataValidity.VALID.getState());
		AlarmSettings alarmSettings = new AlarmSettings();
		alarmSettings.setWarningType(warningType);
		if(warningType.equals("Register")) {
			alarmSettings.setWarningStandard("延迟报道注册天数5天以内为三级预警，延迟11天内为二级预警，延迟大于13天为一级预警");
			alarmSettings.setWarningCondition("延迟报道注册天数5天以内为三级预警，延迟11天内为二级预警，延迟大于14天为一级预警");
		}else if(warningType.equals("redit")){
			alarmSettings.setWarningStandard("成绩波动在班级名次在5名内波动");
			alarmSettings.setWarningCondition("成绩波动在班级排名波动大于6");
		}else if(warningType.equals("Academic")){
			alarmSettings.setWarningStandard("挂科次数在4次以为");
			alarmSettings.setWarningCondition("挂科次数超过4次");
		}
		alarmSettings.setOrgId(orgId);
		alarmSettings.setCreatedDate(new Date());
		alarmSettings.setSetupCloseFlag(10);
		alarmSettingsRepository.save(alarmSettings);

	}

	public void setAlertWarningInformation(Long orgId){
		flag = false;
		String url = "http://gateway.aizhixintest.com/org-manager";
		url = url+"/v1/students/list?pageSize=10000&orgId="+orgId;
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
						if(i%2==0) {
							warningType = "Register";
						}else if(i%3==0){
							warningType = "redit";
						}else {
							warningType = "Academic";
						}
						AlertWarningInformation awinfo = null;
//						List<AlertWarningInformation> awinfoOrg = alertWarningInformationService.getawinfoByOrgIdAndWarningType(orgId,warningType);
//                        if(null!=awinfoOrg&&awinfoOrg.size()>0){
//							List<AlertWarningInformation> updata = new ArrayList<>();
//							for (AlertWarningInformation aw: awinfoOrg){
//                                if(aw.getDefendantId().equals(defendantId)&&warningType.equals(aw.getWarningType())){
//									updata.add()
//								}
//							}
//
//						}


						List<AlertWarningInformation> awinfoList = alertWarningInformationService.getawinfoByDefendantId(orgId,warningType,defendantId);
						if(null!=awinfoList){
							if(awinfoList.size()==1){
								awinfo = awinfoList.get(0);
							}else if(awinfoList.size()>1) {
								logger.info("学号为" + defendantId + "学生的" + WarningType.valueOf(warningType).getValue() + "数据重复异常！");
								continue;
							}else {
								awinfo = new AlertWarningInformation();
							}
						}else {
							awinfo = new AlertWarningInformation();
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
						alertWarningInformationRepository.save(awinfo);
					}
				}
			}
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
