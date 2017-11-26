package com.aizhixin.cloud.dataanalysis.studentRegister.job;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.aizhixin.cloud.dataanalysis.alertinformation.entity.WarningInformation;
import com.aizhixin.cloud.dataanalysis.alertinformation.repository.AlertWarningInformationRepository;
import com.aizhixin.cloud.dataanalysis.alertinformation.service.AlertWarningInformationService;
import com.aizhixin.cloud.dataanalysis.common.constant.DataValidity;
import com.aizhixin.cloud.dataanalysis.common.constant.StudentRegisterConstant;
import com.aizhixin.cloud.dataanalysis.common.constant.WarningType;
import com.aizhixin.cloud.dataanalysis.common.util.DateUtil;
import com.aizhixin.cloud.dataanalysis.common.util.RestUtil;
import com.aizhixin.cloud.dataanalysis.setup.respository.AlarmParameterRespository;
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
	@Autowired
	private AlarmParameterRespository alarmParameterRespository;

	@Scheduled(cron = "0 0/30 * * * ?")
	public void studenteRegisterJob() {
		
//		List<AlarmParameter> alarmParams = alarmParameterRespository
//				.findAllByDeleteFlagAndAlarmSettings_WarningType(DataValidity.VALID.getState(), WarningType.Register.getValue());
//
//		if (null != alarmParams && alarmParams.size() > 0) {
//			HashMap<Long, ArrayList<AlarmParameter>> alarmMap = new HashMap<Long, ArrayList<AlarmParameter>>();
//			ArrayList<WarningInformation> alertInforList = new ArrayList<WarningInformation>();
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
//							WarningInformation alertInfor = new WarningInformation();
//							alertInfor.setDefendantId(studentRegister.getUserId());
//							alertInfor.setName(studentRegister.getUserName());
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


//	@Scheduled(cron = "0 0/10 * * * ?")
	public void studenteRegister() {
		this.setAlertWarningInformation(213L);
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
