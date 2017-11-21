package com.aizhixin.cloud.dataanalysis.studentRegister.job;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.aizhixin.cloud.dataanalysis.alertinformation.entity.AlertWarningInformation;
import com.aizhixin.cloud.dataanalysis.alertinformation.repository.AlertWarningInformationRepository;

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

import com.aizhixin.cloud.dataanalysis.common.constant.StudentRegisterConstant;
import com.aizhixin.cloud.dataanalysis.common.constant.DataValidity;
import com.aizhixin.cloud.dataanalysis.common.util.DateUtil;
import com.aizhixin.cloud.dataanalysis.setup.entity.AlarmParameter;
import com.aizhixin.cloud.dataanalysis.setup.entity.AlarmSettings;
import com.aizhixin.cloud.dataanalysis.setup.respository.AlarmParameterRespository;
import com.aizhixin.cloud.dataanalysis.studentRegister.mongoEntity.StudentRegister;
import com.aizhixin.cloud.dataanalysis.studentRegister.mongoRespository.StudentRegisterMongoRespository;

@Component
public class StudentRegisterJob {

	private Logger logger = Logger.getLogger(this.getClass());
	@Autowired
	private AlarmParameterRespository alarmParameterRespository;
	@Autowired
	private AlarmSettingsRepository alarmSettingsRepository;
	@Autowired
	private RestUtil restUtil;

	@Autowired
	private StudentRegisterMongoRespository stuRegisterMongoRespository;
	@Autowired
	private AlertWarningInformationRepository alertWarningInformationRepository;


	@Scheduled(cron = "0 0/30 * * * ?")
	public void studenteRegister() {

		this.setWarningType(215L, "Register");//报道注册预警
		this.setWarningType(215L, "redit");//学分预警
		this.setWarningType(215L, "Academic");//学业预警

       for(int i=0;i<10;i++) {
		   this.setAlertWarningInformation(215L, "Register", 10, 3);
	   }
		this.setAlertWarningInformation(215L, "Register", 20, 3);
		for(int i=0;i<5;i++) {
			this.setAlertWarningInformation(215L, "Register", 10, 2);
		}
		this.setAlertWarningInformation(215L, "Register", 20, 2);

		this.setAlertWarningInformation(215L, "redit", 10, 1);



		for(int i=0;i<12;i++) {
			this.setAlertWarningInformation(215L, "Register", 10, 3);
		}
		this.setAlertWarningInformation(215L, "Register", 20, 3);
		for(int i=0;i<2;i++) {
			this.setAlertWarningInformation(215L, "Register", 10, 2);
		}
		this.setAlertWarningInformation(215L, "Register", 20, 2);

		this.setAlertWarningInformation(215L, "redit", 10, 1);


		for(int i=0;i<6;i++) {
			this.setAlertWarningInformation(215L, "Academic", 10, 3);
		}
		this.setAlertWarningInformation(215L, "Academic", 20, 3);
		for(int i=0;i<4;i++) {
			this.setAlertWarningInformation(215L, "Academic", 10, 2);
		}
		this.setAlertWarningInformation(215L, "Academic", 20, 2);

		this.setAlertWarningInformation(215L, "Academic", 10, 1);



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
	}


	public  void setWarningType(Long orgId, String warningType){
		AlarmSettings alarmSettings = alarmSettingsRepository.getAlarmSettingsByOrgId(orgId, warningType, DataValidity.VALID.getState());
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

	public void setAlertWarningInformation(Long orgId,String warningType,int warningState,int warningLevel){
		String url = "http://gateway.aizhixintest.com/org-manager";
		url = url+"/v1/students/list?pageSize=1000&orgId="+orgId;
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
						AlertWarningInformation awinfo = new AlertWarningInformation();
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
						awinfo.setWarningLevel(warningLevel);
						awinfo.setWarningState(warningState);
						awinfo.setWarningType(warningType);
					}
				}
			}


		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
