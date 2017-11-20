package com.aizhixin.cloud.dataanalysis.studentRegister.job;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.aizhixin.cloud.dataanalysis.alertinformation.entity.AlertWarningInformation;
import com.aizhixin.cloud.dataanalysis.alertinformation.repository.AlertWarningInformationRepository;

import com.aizhixin.cloud.dataanalysis.common.constant.WarningType;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.aizhixin.cloud.dataanalysis.common.constant.StudentRegisterConstant;
import com.aizhixin.cloud.dataanalysis.common.core.DataValidity;
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
	private StudentRegisterMongoRespository stuRegisterMongoRespository;
	@Autowired
	private AlertWarningInformationRepository alertWarningInformationRepository;


	@Scheduled(cron = "0 0/30 * * * ?")
	public void studenteRegister() {
        //R为学生注册预警
		List<AlarmParameter> alarmParams = alarmParameterRespository
				.findAllByDeleteFlagAndAlarmSettings_WarningType(DataValidity.VALID.getState(), "R");
//固化规则咱不做规则管理和查询
//		List<AlarmParameter> alarmParams = alarmParameterRespository
//				.findAllByDeleteFlagAndAlarmSettings_SetupCloseFlagAndAlarmSettings_TypeOrderByAlarmSettings_idAscLevelDesc(DataValidity.VALID.getState(),AlertTypeConstant.SETUP_ALERTSETTING,
//						AlertTypeConstant.STUDENT_REGISTER);

//		List<AlarmParameter> alarmParams = new ArrayList<AlarmParameter>();

		//固化报到注册规则，之后要改成从数据库获取
		AlarmSettings aSetting = new AlarmSettings();
		aSetting.setName(WarningType.Register.getValue());
		aSetting.setOrgId(1L);
		AlarmParameter alaParam = new AlarmParameter();
		alaParam.setLevel(3);
		alaParam.setSetParameter(11);
		alaParam.setAlarmSettings(aSetting);
		alarmParams.add(alaParam);
		aSetting = new AlarmSettings();
		aSetting.setWarningType("Register");
		aSetting.setName(WarningType.Register.getValue());
		aSetting.setOrgId(1L);
		alaParam = new AlarmParameter();
		alaParam.setLevel(2);
		alaParam.setSetParameter(5);
		alaParam.setAlarmSettings(aSetting);
		alarmParams.add(alaParam);
		aSetting = new AlarmSettings();
		aSetting.setWarningType("Register");
		aSetting.setName(WarningType.Register.getValue());
		aSetting.setOrgId(1L);
		 alaParam = new AlarmParameter();
		alaParam.setLevel(1);
		alaParam.setSetParameter(1);
		alaParam.setAlarmSettings(aSetting);
		alarmParams.add(alaParam);
		
		if (null != alarmParams && alarmParams.size() > 0) {
			HashMap<Long, ArrayList<AlarmParameter>> alarmMap = new HashMap<Long, ArrayList<AlarmParameter>>();
			ArrayList<AlertWarningInformation> alertInforList = new ArrayList<AlertWarningInformation>();
			//按orgId归类告警等级阀值
			for (AlarmParameter param : alarmParams) {
				Long orgId = param.getAlarmSettings().getOrgId();
				if (null != alarmMap.get(orgId)) {
					ArrayList<AlarmParameter> alarmList = alarmMap.get(orgId);
					alarmList.add(param);
				} else {
					ArrayList<AlarmParameter> alarmList = new ArrayList<AlarmParameter>();
					alarmList.add(param);
					alarmMap.put(orgId, alarmList);
				}
			}
			Iterator iter = alarmMap.entrySet().iterator();
			Date today = new Date();
			while (iter.hasNext()) {
				Map.Entry entry = (Map.Entry) iter.next();
				Long key = (Long)entry.getKey();
				ArrayList<AlarmParameter> val = (ArrayList<AlarmParameter>)entry.getValue();
				//按orgId查询未报到的学生信息
				List<StudentRegister> stuRegisterList = stuRegisterMongoRespository
						.findAllByOrgIdAndIsregister(key,StudentRegisterConstant.UNREGISTER);
				for(StudentRegister studentRegister : stuRegisterList){
					int result = DateUtil.getDaysBetweenDate(studentRegister.getRegisterDate(), today);
					for(AlarmParameter alarmParam : val){
						if(result >= alarmParam.getSetParameter()){
							AlertWarningInformation alertInfor = new AlertWarningInformation();
							alertInfor.setDefendantId(studentRegister.getStuId());
							alertInfor.setName(studentRegister.getStuName());
							alertInfor.setJobNumber(studentRegister.getJobNum());
							alertInfor.setCollogeId(studentRegister.getCollegeId());
							alertInfor.setCollogeName(studentRegister.getCollegeName());
							alertInfor.setTeachingYear(studentRegister.getGrade());
							alertInfor.setClassId(studentRegister.getClassId());
							alertInfor.setClassName(studentRegister.getClassName());
							alertInfor.setProfessionalId(studentRegister.getProfessionalId());
							alertInfor.setProfessionalName(studentRegister.getProfessionalName());
							alertInfor.setTeachingYear(studentRegister.getSchoolYear());
							alertInfor.setWarningLevel(alarmParam.getLevel());
							alertInfor.setWarningType("Register");
							alertInfor.setWarningTime(new Date());
							alertInfor.setOrgId(alarmParam.getAlarmSettings().getOrgId());
							alertInforList.add(alertInfor);
							break;
						}else{
							continue;
						}
					}
				}
			}
			
			if(!alertInforList.isEmpty()){
				alertWarningInformationRepository.save(alertInforList);
			}
		}
	}
}
